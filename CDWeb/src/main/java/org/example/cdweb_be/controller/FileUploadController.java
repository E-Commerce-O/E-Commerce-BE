package org.example.cdweb_be.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.cdweb_be.dto.response.ApiResponse;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("upload")
@Slf4j
public class FileUploadController {
    private final String CLIENT_ID = "dd8ce706a76465e";
    @PostMapping
    public ApiResponse uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw  new AppException(ErrorCode.IMAGE_REQUIRED);
        }
        String imageUrl = uploadImageAndGetLink(file);
        if (imageUrl != null) {
            return new ApiResponse<>(imageUrl);
        } else {
            throw new AppException(ErrorCode.SERVER_ERROR);
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
}
