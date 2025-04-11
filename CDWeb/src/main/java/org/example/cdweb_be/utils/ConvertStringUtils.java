package org.example.cdweb_be.utils;

public class ConvertStringUtils {
    public static String convertGivenName(String input) {
        // Chuyển đổi chuỗi thành chữ thường và sau đó viết hoa ký tự đầu mỗi từ
        String[] words = input.toLowerCase().split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1)).append(" ");
            }
        }

        return result.toString().trim(); // Xóa khoảng trắng ở đầu và cuối
    }
}
