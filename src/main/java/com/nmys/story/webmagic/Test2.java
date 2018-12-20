package com.nmys.story.webmagic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingDeque;


public class Test2 {
	public static void main(String[] args) {
		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		LinkedBlockingDeque<Map<String, Long>> queue = new LinkedBlockingDeque<>();
		Long tid=null;
		String string=null;
		Long value =null;
		for(;;) {
			String result= httpget();
			
			List<Map> strToArray = DataUtils.strToArray(result);
			for(Map map:strToArray) {
				string = map.get("date_ms").toString();
				tid = Long.valueOf(map.get("tid").toString());
				if(!queue.isEmpty()) {
					Map<String, Long> last = queue.getLast();
					for(Entry<String, Long> l:last.entrySet()) {
						 value = l.getValue();
					}
					if(tid<value) {
						continue;
					}
					
				}
				String format = simpleDateFormat.format(new Date(Long.valueOf(string)));
				Map<String, Long> maps=new HashMap<>();
				maps.put(format, tid);
				queue.add(maps);
				System.out.println(queue.toString());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
			
		}
	}

	 public static String httpget() {
	    	String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = "https://www.okex.com/api/v1/trades.do?symbol=btc_usdt&size=60";
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    }
}
