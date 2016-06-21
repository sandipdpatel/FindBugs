package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ClientUtil {

    public String getRequest(String url) throws ClientProtocolException, IOException {
        HttpGet request = new HttpGet(url);
        return exec(request);
    }

    public String postRequest(String url, String key, String value) throws IOException {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair(key, value));
        post.setEntity(new UrlEncodedFormEntity(nvps));
        return exec(post);
    }

    private String exec(HttpUriRequest request) {
        HttpHost proxy = new HttpHost("148.87.19.20", 80);
        try {
            CloseableHttpClient client = HttpClientBuilder.create().setProxy(proxy).build();
            // CloseableHttpClient client = HttpClientBuilder.create().build();
            CloseableHttpResponse httpResponse = client.execute(request);
            HttpEntity entity = httpResponse.getEntity();
            byte[] bytes = {};

            BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                buffer.append(line);
            }
            EntityUtils.consume(entity);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                throw new Exception(new String(bytes, StandardCharsets.UTF_8));
            } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                throw new Exception("No Content Found at location: " + request.getURI() + "  Confirm server is running with content at that location.");
            } else if (statusCode == HttpStatus.SC_BAD_REQUEST) {
                String message = new String(bytes, StandardCharsets.UTF_8);
                throw new Exception(message);
            } else if (statusCode == HttpStatus.SC_METHOD_NOT_ALLOWED) {
                String message = new String(bytes, StandardCharsets.UTF_8);
                throw new Exception(message);
            }

            return buffer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}