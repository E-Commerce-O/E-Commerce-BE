package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.component.MessageProvider;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.entity.Image;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.respository.ImageRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("upload")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUploadController {
    MessageProvider messageProvider;
    String CATBOX_URL = "https://catbox.moe/user/api.php";
    String CLIENT_ID = "dd8ce706a76465e";
    int IMAGE_WIDTH = 700;//px
    ImageRepository imageRepository;
    @NonFinal
    @Value("${file.upload-dir}")
    protected String uploadDir;
    static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            ".jpeg",
            ".jpg",
            ".png",
            ".gif",
            ".bmp",
            ".tiff",
            ".tif",
            ".webp",
            ".heif",
            ".heic"
    );

    @PostMapping
    public ApiResponse uploadFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new AppException(messageProvider,ErrorCode.IMAGE_REQUIRED);
        }
        String imageUrl = uploadToCatbox(file);
        if (imageUrl != null) {
            return new ApiResponse<>(imageUrl);
        } else {
            throw new AppException(messageProvider,ErrorCode.SERVER_ERROR);
        }
    }

    private String uploadImageAndGetLink(MultipartFile imageFile) {
        try {
            // Tạo URL đến API Imgur
            URL url = new URL("https://api.imgur.com/3/upload");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Cấu hình request
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            String boundary = "---------------------------" + System.currentTimeMillis();
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // Lấy output stream từ kết nối
            OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);

            // Gửi file ảnh
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"image\"; filename=\"").append(imageFile.getOriginalFilename())
                    .append("\"\r\n");
            writer.append("Content-Type: ").append(imageFile.getContentType()).append("\r\n");
            writer.append("\r\n");
            writer.flush();

            // Đọc dữ liệu từ InputStream của file ảnh và ghi vào output stream
            InputStream inputStream = imageFile.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            // Kết thúc phần gửi file
            writer.append("\r\n");
            writer.append("--").append(boundary).append("--\r\n");
            writer.close();
            outputStream.close();

            // Lấy mã phản hồi từ server
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Đọc phản hồi và trích xuất liên kết từ JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                JSONObject json = new JSONObject(response.toString()).getJSONObject("data");

                return json.getString("link");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý ngoại lệ nếu cần
        }
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        Image imageEntity = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/jpg"); // Hoặc kiểu MIME tương ứng với ảnh

        return new ResponseEntity<>(imageEntity.getImageData(), headers, HttpStatus.OK);
    }

    //    @PostMapping("/catbox")
    public String uploadToCatbox(@RequestParam("file") MultipartFile file) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("fileToUpload", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
            body.add("reqtype", "fileupload");

            ResponseEntity<String> response = restTemplate.postForEntity(CATBOX_URL, body, String.class);
            return response.getBody(); // Trả về link hoặc thông báo từ Catbox
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }

    @PostMapping("/db")
    public ApiResponse uploadFileDB(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty() || file.getBytes().length == 0) throw new AppException(messageProvider,ErrorCode.FILE_IS_EMPTY);
            if (!isImageFile(file.getOriginalFilename())) throw new AppException(messageProvider,ErrorCode.FILE_ISNT_IMAGE);
            BufferedImage bufferedImage = getBufferedImage(file);
            log.info("bufferedImage is null: "+bufferedImage);
            log.info("Original size: "+file.getBytes().length);
            log.info("New size: "+ compressFile(resizeImage(bufferedImage, IMAGE_WIDTH)).length);
            Image imageEntity = new Image();
            imageEntity.setImageData(compressFile(resizeImage(bufferedImage, IMAGE_WIDTH)));
            imageEntity.setImageName(file.getOriginalFilename());
            imageRepository.save(imageEntity);
            return new ApiResponse("/identity/upload/" + imageEntity.getId());
        } catch (IOException e) {
            throw new AppException(messageProvider,ErrorCode.SERVER_ERROR);
        }
    }
    @PostMapping("/resize/{imageWidth}")
    public ApiResponse uploadFileResize(@RequestParam("file") MultipartFile file, @PathVariable int imageWidth) {
        try {
            if (file.isEmpty() || file.getBytes().length == 0) throw new AppException(messageProvider,ErrorCode.FILE_IS_EMPTY);
            if (!isImageFile(file.getOriginalFilename())) throw new AppException(messageProvider,(ErrorCode.FILE_ISNT_IMAGE));
            BufferedImage bufferedImage = getBufferedImage(file);
            log.info("Original size: "+file.getBytes().length);
            log.info("New size: "+ compressFile(resizeImage(bufferedImage, imageWidth)).length);
            Image imageEntity = new Image();
            imageEntity.setImageData(compressFile(resizeImage(bufferedImage, imageWidth)));
            imageEntity.setImageName(file.getOriginalFilename());
            imageRepository.save(imageEntity);
            return new ApiResponse("/identity/upload/" + imageEntity.getId());
        } catch (IOException e) {
            throw new AppException(messageProvider,ErrorCode.SERVER_ERROR);
        }
    }
    @PostMapping("/keep-size")
    public ApiResponse uploadFileKeepSize(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty() || file.getBytes().length == 0) throw new AppException(messageProvider,ErrorCode.FILE_IS_EMPTY);
            if (!isImageFile(file.getOriginalFilename())) throw new AppException(messageProvider,(ErrorCode.FILE_ISNT_IMAGE));
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            log.info("Original size: "+file.getBytes().length);
            log.info("New size: "+ compressFile(resizeImage(bufferedImage, -1)).length);
            Image imageEntity = new Image();
            imageEntity.setImageData(compressFile(resizeImage(bufferedImage, -1)));
            imageEntity.setImageName(file.getOriginalFilename());
            imageRepository.save(imageEntity);
            return new ApiResponse("/identity/upload/" + imageEntity.getId());
        } catch (IOException e) {
            throw new AppException(messageProvider,ErrorCode.SERVER_ERROR);
        }
    }
    private boolean isImageFile(String fileName) {
        for (String ext : ALLOWED_CONTENT_TYPES) {
            if (fileName.endsWith(ext)) return true;
        }
        return false;
    }
    private byte[] compressFile(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.8f); // Thử giảm xuống 0.5f nếu cần

        writer.setOutput(ImageIO.createImageOutputStream(baos));
        writer.write(null, new IIOImage(bufferedImage, null, null), param);
        writer.dispose();

        return baos.toByteArray();
    }

    private BufferedImage resizeImage(BufferedImage bufferedImage, int imageWidth) throws IOException {
        double newRate = imageWidth > 0 ? bufferedImage.getWidth() / (double) imageWidth : 1;
        newRate = Math.max(1, newRate);
        int newWidth = (int) (bufferedImage.getWidth() / newRate);
        int newHeight = (int) (bufferedImage.getHeight() / newRate);

        // Kiểm tra kích thước mới
        if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("Kích thước ảnh không hợp lệ sau khi resize");
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bufferedImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resizedImage;
    }
    BufferedImage getBufferedImage(MultipartFile file) throws IOException {
        BufferedImage bufferedImage;
        if (file.getOriginalFilename().endsWith(".webp")){
            bufferedImage = convertWebPToBufferedImage(file);
        }else{
            bufferedImage = ImageIO.read(file.getInputStream());
        }

        return bufferedImage;
    }
    public static BufferedImage convertWebPToBufferedImage(MultipartFile file) throws IOException {
        BufferedImage bufferedImage;
        try (ImageInputStream input = ImageIO.createImageInputStream(file.getInputStream())) {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("webp");
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(input);
                bufferedImage = reader.read(0);
            } else {
                throw new IOException("Không tìm thấy ImageReader cho định dạng webp");
            }
        }
        return bufferedImage;
    }
}
