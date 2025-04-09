package org.example.cdweb_be.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class IPUtils {
    private static final String API_URL = "https://api.myip.com";
    public static String getIP(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        HttpGet get = new HttpGet(API_URL);

        try {
            if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse httpResponse = httpClient.execute(get);
                    String data = EntityUtils.toString(httpResponse.getEntity()).trim();
                    JSONObject jsonObject = new JSONObject(data);
                    ip = jsonObject.getString("ip");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi tại đây
        }
        return ip;
    }
}
