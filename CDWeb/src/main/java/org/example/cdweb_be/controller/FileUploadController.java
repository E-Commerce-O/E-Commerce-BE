package org.example.cdweb_be.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("upload")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileUploadController {
    String CATBOX_URL = "https://catbox.moe/user/api.php";
    String CLIENT_ID = "dd8ce706a76465e";
    ImageRepository imageRepository;
    @NonFinal
    @Value("${file.upload-dir}")
    protected String uploadDir;
    @PostMapping
    public ApiResponse uploadFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw  new AppException(ErrorCode.IMAGE_REQUIRED);
        }
        String imageUrl = uploadToCatbox(file);
        if (imageUrl != null) {
            return new ApiResponse<>(imageUrl);
        } else {
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
//        try {
//            Image imageEntity = new Image();
//            imageEntity.setImageData(file.getBytes());
//            imageEntity.setImageName(file.getOriginalFilename());
//            imageRepository.save(imageEntity);
//            return new ApiResponse("Image uploaded successfully: " + file.getOriginalFilename());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ApiResponse("Image upload failed: " + e.getMessage());
//        }
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
        headers.set("Content-Type", "image/png"); // Hoặc kiểu MIME tương ứng với ảnh

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
            Image imageEntity = new Image();
            imageEntity.setImageData(file.getBytes());
            imageEntity.setImageName(file.getOriginalFilename());
            imageRepository.save(imageEntity);
            return new ApiResponse("/identity/upload/"+imageEntity.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return new ApiResponse("Image upload failed: " + e.getMessage());
        }
    }
}
