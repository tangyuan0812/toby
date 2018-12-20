package com.nmys.story.webmagic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

public class Testa {

	private static final String filePath = "E:\\log";
	private static final String URL = "http://v.ranks.xin/video-parse.php?url=";
	private static final String ID = "https://www.365yg.com/a";
	private static CyclicBarrier cyclicBarrier;

	static class RunnableTask extends Thread {

		public void run() {

			try {

				String httpGet = httpGet(
						"https://lf.snssdk.com/api/news/feed/v47/?category=hotsoon_video&refer=1&refresh_reason=0&count=20&list_entrance=main_tab&last_refresh_sub_entrance_interval=1545206961&loc_mode=7&loc_time=1545206919&latitude=22.576333712969163&longitude=114.06669502949687&city=%E6%B7%B1%E5%9C%B3%E5%B8%82&tt_from=pull&lac=1&cid=15185&plugin_enable=4&iid=54151373073&device_id=54243683418&ac=wifi&channel=lite_xiaomi&aid=35&app_name=news_article_lite&version_code=667&version_name=6.6.7&device_platform=android&ab_version=486956%2C651387%2C475366%2C633724%2C581621%2C610654%2C374098%2C641501%2C612182%2C377587%2C633811%2C621315%2C644000%2C641034%2C657090%2C637880%2C642435%2C656016%2C572478%2C467498%2C649239%2C657089%2C646418%2C631232%2C636081&ab_client=a1%2Cc4%2Ce1%2Cf2%2Cg2%2Cf7&ab_group=z2&ab_feature=z1&abflag=3&ssmix=a&device_type=MI+6X&device_brand=xiaomi&language=zh&os_api=27&os_version=8.1.0&uuid=99001123304216&openudid=1704e0dcf3f79cf1&manifest_version_code=667&resolution=1080*2030&dpi=440&update_version_code=6674&_rticket=1545206961712&plugin=0&fp=w2T_FSU7P2ZtFlTrFlU1FYFSczwW&rom_version=miui_v10_v10.0.3.0.odccnfh&ts=1545206962&as=a2b5ff81f20bfcbc794344&mas=0018dbe93955034054496d6d7648040886aa885aed06e8e69f&cp=56cf1a92fccb1q1");
				Map<String, Object> strToMap = DataUtils.strToMap(httpGet);
				List<Map> listMap = (List<Map>) strToMap.get("data");
				for (Map map : listMap) {
					try {
						String address = JSONPath.eval(JSONObject.parse(map.get("content").toString()), "$.id")
								.toString();
						
						try {

							System.out.println(URL + ID + address);
							String httpGet2 = httpGet(URL + ID + address);
							Map<String, Object> strToMap2 = DataUtils.strToMap(httpGet2);
							List<Map> object = (List<Map>) strToMap2.get("data");
							int size = object.size();
							if (size >= 1) {
								String url = object.get(size - 1).get("url").toString();
								download(url);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				cyclicBarrier.await();
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public static void main(String[] args) {
		Long start = System.currentTimeMillis();
		cyclicBarrier = new CyclicBarrier(10);
		for (int i = 0; i < 10; i++) {
			new RunnableTask().start();
		}
		System.out.println("end: "+ (System.currentTimeMillis()-start));

	}

	public static String httpGet(String url) {
		try {
			CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
					.setRedirectsEnabled(false).build();
			HttpGet get = new HttpGet(url);
//			get.setConfig(requestConfig);
//			get.setHeader("Accept", "*/*");
//			get.setHeader("User-Agent", "Aweme/1.7.9 (iPhone; iOS 10.2.1; Scale/3.00)");
			CloseableHttpResponse response = httpClient.execute(get);
			// System.out.println(response.getStatusLine().getStatusCode());
//			if (response.getStatusLine().getStatusCode() != 302) {
//				System.exit(0);
//			}
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;

	}

	public static void download(String videoUrl) throws Exception {
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
