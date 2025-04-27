package org.example.cdweb_be.utils;

import com.nimbusds.jose.shaded.gson.Gson;
import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.cdweb_be.exception.AppException;
import org.example.cdweb_be.exception.ErrorCode;
import org.example.cdweb_be.utils.responseUtilsAPI.DistrictUtil;
import org.example.cdweb_be.utils.responseUtilsAPI.DeliveryMethodUtil;
import org.example.cdweb_be.utils.responseUtilsAPI.ProvinceUtil;
import org.example.cdweb_be.utils.responseUtilsAPI.WardUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
@Data
@Builder
//@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AddressUltils {
    private static final String BASE_URL_ADDRESS = "https://partner.viettelpost.vn/v2/categories/";
    private static final String BASE_URL_FEE = "https://partner.viettelpost.vn/v2/order/getPriceAll";
    private static final String token = "F72D14C609C4C693ECDA34653EBCF032";
    public static final String HOA_TOC = "VHT";
    public static final String TIET_KIEM = "LCOD";
    public static final String NOI_TINH = "PHS";
    private static int quantity = 1;
    private static int productWeight = 100;
    private static int length = 10;
    private static int width =20;
    private static int height = 2;
    private static int productPrice =0;
    private static int moneyCollection = 0;
    public static List<String> getTypeVAT(String provinceSenderID, String provinceReceiverID) {
        if (provinceReceiverID.equals(provinceSenderID)) {
            return Arrays.asList(HOA_TOC, NOI_TINH);
        }
        return Arrays.asList(HOA_TOC, TIET_KIEM);
    }

    public static List<ProvinceUtil> getProvinces(@Nullable String provinceId) throws IOException {
        if (provinceId == null || provinceId.isEmpty()) {
            provinceId = "-1";
        }

        URL url = new URL(BASE_URL_ADDRESS + "listProvinceById?provinceId=" + provinceId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        log.info("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Đọc phản hồi và trích xuất liên kết từ JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Phân tích JSON và chuyển đổi thành danh sách Province
            JSONArray jsonArray = new JSONObject(response.toString()).getJSONArray("data");
            ProvinceUtil[] provinceUtils = new Gson().fromJson(jsonArray.toString(), ProvinceUtil[].class);
            connection.disconnect();
            return Arrays.asList(provinceUtils);
        }
        return null;
    }
    public static List<DistrictUtil> getDistricts(@Nullable String provinceId){
        try {

            if (provinceId == null || provinceId.isEmpty()) {
                provinceId = "-1";
            }

            URL url = new URL(BASE_URL_ADDRESS + "listDistrict?provinceId=" + provinceId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            log.info("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Đọc phản hồi và trích xuất liên kết từ JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Phân tích JSON và chuyển đổi thành danh sách Province
                JSONArray jsonArray = new JSONObject(response.toString()).getJSONArray("data");
                DistrictUtil[] districtUtils = new Gson().fromJson(jsonArray.toString(), DistrictUtil[].class);
                connection.disconnect();
                return Arrays.asList(districtUtils);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static List<WardUtil> getWards( String districtId){
        try {
            URL url = new URL(BASE_URL_ADDRESS + "listWards?districtId=" + districtId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
//            log.info("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Đọc phản hồi và trích xuất liên kết từ JSON
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Phân tích JSON và chuyển đổi thành danh sách Province
                JSONArray jsonArray = new JSONObject(response.toString()).getJSONArray("data");
                WardUtil[] districtUtils = new Gson().fromJson(jsonArray.toString(), WardUtil[].class);
                connection.disconnect();
                return Arrays.asList(districtUtils);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static List<DeliveryMethodUtil> getInfoShips(String senderProvince, String senderDistrict, String receiverProvince, String receiverDistrict) {
        try {
//            URL url = new URL(BASE_URL_FEE);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("token", token);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(BASE_URL_FEE);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("token", token);
            String json = "{\n" +
                    "  \"SENDER_PROVINCE\" : " + senderProvince + ",\n" +
                    "  \"SENDER_DISTRICT\" : " + senderDistrict + ",\n" +
                    "  \"RECEIVER_PROVINCE\" : " + receiverProvince + ",\n" +
                    "  \"RECEIVER_DISTRICT\" : " + receiverDistrict + ",\n" +
                    "  \"PRODUCT_TYPE\" : \"HH\",\n" +
                    "  \"PRODUCT_WEIGHT\" : " + productWeight + ",\n" +
                    "  \"PRODUCT_PRICE\" : " + productPrice + ",\n" +
                    "  \"MONEY_COLLECTION\" : \"" + moneyCollection + "\",\n" +
                    "  \"TYPE\" :1\n" +
                    "}";
            HttpEntity entity = new StringEntity(json, "UTF-8");
            post.setEntity(entity);
            HttpResponse httpResponse = client.execute(post);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == 200) { // Kiểm tra mã phản hồi
                String response = EntityUtils.toString(httpResponse.getEntity());
                JSONArray jsonArray = new JSONArray(response);
                return Arrays.asList(new Gson().fromJson(jsonArray.toString(), DeliveryMethodUtil[].class));
                // Xử lý jsonArray ở đây
            } else {
                System.out.println("Error: " + responseCode);
                throw new AppException(ErrorCode.SERVER_ERROR);
            }

        } catch (Exception e) {
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
    }

    }
