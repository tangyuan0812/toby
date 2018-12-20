package com.nmys.story.webmagic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class Test {
	private static String filePath = "E:\\log";
	private static String url = "https://aweme.snssdk.com/aweme/v1/play/?video_id=v0200f660000bctojepcgf31ghmrsmdg&line=0&ratio=720p&media_type=1&vr_type=0&test_cdn=None&improve_bitrate=0";

	public static void main(String[] args) throws Exception {
		CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
				.setRedirectsEnabled(false).build();
		HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig);
		get.setHeader("Accept", "*/*");
		get.setHeader("User-Agent", "Aweme/1.7.9 (iPhone; iOS 10.2.1; Scale/3.00)");
		CloseableHttpResponse response = httpClient.execute(get);
		System.out.println(response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() != 302) {
			System.exit(0);
		}
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity, "utf-8");
		String detail = getVideo(content);
		download(detail);
	}

	private static String getVideo(String str) {
		String regEx = "href=\"(.*?)\">";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static void download(String videoUrl) throws Exception {
		// 构造URL
		URL url = new URL(videoUrl);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = con.getInputStream();

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(filePath);
		long time = System.currentTimeMillis() / 1000;
		OutputStream os = new FileOutputStream(sf.getPath() + "/" + time + ".mp4");
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();

	}

}
