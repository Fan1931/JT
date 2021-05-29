package com.jt.test;

import com.jt.util.HttpClientService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class TestHttpClient {

    /**
     * 1.实例化httpClient工具API对象
     * 2.定义请求url工具路径
     * 3.定义请求方式POST/GET/PUT/DELETE
     * 4.利用工具API发起http请求
     * 5.获取响应结果,判断用户的请求是否正确  检查状态码是否为200
     * 6.状态码正确，则动态的获取相应的结果
     */

    @Test
    public void testHttpClient01() throws IOException {
        //什么时候使用，什么时候创建，创建链接池的方式是最好的
        HttpClient httpClient = HttpClients.createDefault();
        String url = "https://www.baidu.com/";
        HttpGet httpGet = new HttpGet(url);
        HttpPost httpPost = new HttpPost(url);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String protocol = httpResponse.getStatusLine().getProtocolVersion().getProtocol();
        System.out.println(protocol);
        String reason = httpResponse.getStatusLine().getReasonPhrase();
        System.out.println(reason);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        //判断请求是否正确
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            System.out.println("表示服务器执行成功");
            //获取返回值信息
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity, "utf-8");
            System.out.println(result);
        }
    }

    /**
     * 从spring中动态获取httpClient对象获取数据
     */
    @Autowired
    private HttpClient httpClient;

    @Test
    public void test01() throws IOException {
        String url = "https://www.baidu.com/";
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
    }

    @Autowired
    private HttpClientService httpClientService;
    @Test
    public void testGet() throws ClientProtocolException,IOException{
        String url = "https://www.baidu.com/";
        Map<String, String> params = new HashMap<>();
        params.put("id", "100");
        params.put("name", "tomcat");
        String url2 = httpClientService.doGet(url, params, null);
        System.out.println(url);
    }
}
