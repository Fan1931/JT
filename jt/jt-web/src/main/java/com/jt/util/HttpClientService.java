package com.jt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {
	@Autowired
	private CloseableHttpClient httpClient;
	@Autowired
	private RequestConfig requestConfig;

	/**
	 * 需求:给定url地址，则自动发起请求，并将返回值结果给用户
	 * 参数分析：
	 *  1.用户的url参数地址
	 *  2.判断用户是否需要携带参数 使用Map集合封装
	 *  3.指定字符集编码 需要用户自己指定
	 * 返回值:String类型
	 */
	public String doGet(String url,Map<String,String> params,String charset){
		//1.判断字符集编码是否为空 如果为空则给定默认值utf-8
		if(StringUtils.isEmpty(charset)){

			charset = "UTF-8";
		}

		/**
		 * 2.判断用户是否携带参数
		 *   没有带参数:http:///manage.jt.com/item
		 *   带参数:http:///manage.jt.com/item?id=1&name=xxx
		 *
		 * 补充知识：entry={key=value} Map-->entry--->key:value
		 */
		if(params != null){//如果不为null，则需要进行参数拼接
			url += "?";
			//map集合遍历，从中获取key=value
			for (Map.Entry<String,String> entry : params.entrySet()) {
				String key = entry.getKey().trim();
				String value = entry.getValue().trim();
				url += key + "=" + value + "&";
			}
			//去除多余&
			url = url.substring(0, url.length() - 1);
		}

		//3.定义参数提交对象
		HttpGet httpGet = new HttpGet(url);

		//4.为请求设定超时时间
		httpGet.setConfig(requestConfig);

		String result = null;
		//5.通过httpClient发送请求
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			//判断用户的请求是否正确
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				//表示程序调用成功
				return EntityUtils.toString(httpResponse.getEntity(),charset);
			}else{
				System.out.println("调用异常:状态信息:"+httpResponse.getStatusLine().getStatusCode());
			}
			int status = httpResponse.getStatusLine().getStatusCode();
			throw new RuntimeException("获取后台错误的状态码:" + status + "|请求的路径为:url" + url);
		} catch (Exception e) {
			e.printStackTrace();
			//将检查异常，转化为运行时异常
			throw new RuntimeException(e);
		}
	}

	/**
	 * 重载多个doGet方法，满足不同需求
	 * @param url
	 * @return
	 */
	public String doGet(String url){

		return doGet(url, null, null);
	}

	public String doGet(String url,Map<String,String> params){

		return doGet(url, params, null);
	}

	public String doGet(String url,String charset){

		return doGet(url, null, charset);
	}

	//实现httpClient POST提交
	public String doPost(String url,Map<String,String> params,String charset){
		String result = null;

		//1.定义请求类型
		HttpPost post = new HttpPost(url);
		post.setConfig(requestConfig);  	//定义超时时间

		//2.判断字符集是否为null
		if(StringUtils.isEmpty(charset)){

			charset = "UTF-8";
		}

		//3.判断用户是否传递参数
		if(params !=null){
			//3.2准备List集合信息
			List<NameValuePair> parameters =
					new ArrayList<>();

			//3.3将数据封装到List集合中
			for (Map.Entry<String,String> entry : params.entrySet()) {

				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			//3.1模拟表单提交
			try {
				UrlEncodedFormEntity formEntity =
						new UrlEncodedFormEntity(parameters,charset); //采用u8编码

				//3.4将实体对象封装到请求对象中
				post.setEntity(formEntity);
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
		}

		//4.发送请求
		try {
			CloseableHttpResponse response =
					httpClient.execute(post);

			//4.1判断返回值状态
			if(response.getStatusLine().getStatusCode() == 200) {

				//4.2表示请求成功
				result = EntityUtils.toString(response.getEntity(),charset);
			}else{
				System.out.println("获取状态码信息:"+response.getStatusLine().getStatusCode());
				throw new RuntimeException();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}



	public String doPost(String url){

		return doPost(url, null, null);
	}

	public String doPost(String url,Map<String,String> params){

		return doPost(url, params, null);
	}

	public String doPost(String url,String charset){

		return doPost(url, null, charset);
	}
}
