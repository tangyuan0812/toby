package com.nmys.story.webmagic;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class DouyingTest {
	
	private static ConcurrentLinkedDeque<String> queue = new ConcurrentLinkedDeque<>(); 
	
	private static Executor executor = Executors.newFixedThreadPool(10);
	
	private static final String URL = "http://v.ranks.xin/video-parse.php?url=";
	
	public static void main(String[] args) {
		
		try {
			//tt-input__icon bui-icon icon-search is-clickable
			
			System.setProperty("webdriver.chrome.driver", "E:/log/chromedriver.exe");
			// 实例化一个浏览器对象
			WebDriver driver = new ChromeDriver();
			//driver.manage().window().setSize(new Dimension(500, 600));
			driver.get("https://www.365yg.com/search/?keyword=%E6%8A%96%E9%9F%B3");
			Thread.sleep(4000);
			
			/*driver.findElement(By.cssSelector("input.tt-input__inner")).clear();
			driver.findElement(By.cssSelector("input.tt-input__inner")).sendKeys("抖音");
			driver.findElement(By.cssSelector("i.tt-input__icon.bui-icon.icon-search.is-clickable")).click();*/
			
			/*driver.findElement(By.name("keyword")).clear();
			driver.findElement(By.name("keyword")).sendKeys("抖音");// 将需要转换的链接放入该输入框中
			//driver.findElement(By.cssSelector("button.search-btn")).click();// 点击解析
			((JavascriptExecutor)driver).executeScript("document.getElementsByClassName(\"search-btn\")[0].click()");*/
			
			Thread.sleep(3000);
			
			/*int number=500;
			for(int i=0;i<5;i++) {
				String stringnumber=String.valueOf(number);
				((JavascriptExecutor)driver).executeScript("window.scrollTo(500,"+stringnumber+");");
				Thread.sleep(1000);
				number*=2;
			}*/
			
			
			for(int i=0;i<10;i++) {
				//document.documentElement.scrollTo(0,(document.documentElement.scrollTop)*2)
				if(i==0) {
					
					((JavascriptExecutor)driver).executeScript("window.scrollTo(0,2000)");
				}else {
					((JavascriptExecutor)driver).executeScript("document.documentElement.scrollTo(0,(document.documentElement.scrollTop)*2)");
				}
				Thread.sleep(1500);
			}
			List<WebElement> elements = driver.findElements(By.cssSelector("a.img-wrap"));// 获取到每个视频的模块
			System.out.println("大小为"+elements.size());
			
			
			System.out.println("start" + DataUtils.getDate(new Date().getTime()));
			CountDownLatch latch = new CountDownLatch(elements.size());
			for (WebElement we : elements) {
				RunnableT runnableT = new RunnableT(we, latch);
				executor.execute(runnableT);
				
			}
			latch.await();
			System.out.println("end" + DataUtils.getDate(new Date().getTime()));
			driver.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 休眠等待页面加载
		
	}
	
	static class  RunnableT extends Thread{
		private WebElement we;
		private CountDownLatch latch;
		
		public RunnableT(WebElement we,CountDownLatch latch) {
			this.we=we;
			this.latch=latch;
		}
		
		@Override
		public void run() {
			try {
				String address = we.getAttribute("href").toString();// 获取模块的data-id的属性值
				String httpGet2 = Testa.httpGet(URL + address);
				Map<String, Object> strToMap2 = DataUtils.strToMap(httpGet2);
				List<Map> object = (List<Map>) strToMap2.get("data");
				int size = object.size();
				if (size >= 1) {
					String url = object.get(size - 1).get("url").toString();
					Testa.download(url);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}finally {
				if(this.latch!=null) {
					latch.countDown();
				}
			}
			
		}
		
	}

}
