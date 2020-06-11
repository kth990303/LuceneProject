package apiNaverNews;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.*;
import org.json.simple.parser.*;


public class ApiNews {

    public String NewsStr(String ss) throws ParseException {
        String clientId = "N1C6pkOYKtueY_QQengR"; //���ø����̼� Ŭ���̾�Ʈ ���̵�"
        String clientSecret = "KCfSyde7ik"; //���ø����̼� Ŭ���̾�Ʈ ��ũ����"
        
        String ret=null;
        String text = null;
        try {
            text = URLEncoder.encode(ss, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("�˻��� ���ڵ� ����",e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text;    // json ���
        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml ���

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);

        // System.out.println(responseBody);
        JSONParser jsonParser=new JSONParser();
        JSONObject jsonObject=(JSONObject) jsonParser.parse(responseBody);
        JSONArray infoArray=(JSONArray) jsonObject.get("items");

        for(int i=0;i<infoArray.size();i++) {
        	System.out.println("=item "+i+"===========================");
        	JSONObject itemObject=(JSONObject) infoArray.get(i);
        	System.out.println("title:\t"+itemObject.get("title"));
        	System.out.println("description:\t"+itemObject.get("description"));
        	System.out.println("link:\t"+itemObject.get("link")+"\n");
        	ret+=itemObject.get("title");
        	ret+="\n";
        	ret+=itemObject.get("description");
        	ret+="\n";
        }
        return ret;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // ���� ȣ��
                return readBody(con.getInputStream());
            } else { // ���� �߻�
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API ��û�� ���� ����", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL�� �߸��Ǿ����ϴ�. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("������ �����߽��ϴ�. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API ������ �дµ� �����߽��ϴ�.", e);
        }
    }
}