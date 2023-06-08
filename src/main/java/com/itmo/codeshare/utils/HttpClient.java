package com.itmo.codeshare.utils;

import net.minidev.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;

public class HttpClient {
    final private String url;
    public HttpClient(String url) {
        this.url = url;
    }
    public HttpResponse requestPOST(@Nullable JSONObject body) throws IOException {
        org.apache.http.client.HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);

        if (body != null) {
            request.setHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(body.toString(), ContentType.APPLICATION_JSON);
            request.setEntity(entity);
        }

        return client.execute(request);
    }

    static public JSONObject buildJson(Map<String, String> payload) {
        JSONObject json = new JSONObject();
        json.putAll(payload);
        return json;
    }

    static public String getResponseData(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }
}
