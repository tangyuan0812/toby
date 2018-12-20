package com.nmys.story.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/*public class Test implements PageProcessor{
	
	int myid=0;
	int size=0;

	private Site site=Site.me().setCharset("utf-8").setRetryTimes(1000).setSleepTime(1000);
	
	@Override
	public void process(Page page) {
		Html html = page.getHtml();
		String string = html.xpath("//*[@id=\"J_section_0\"]/div/div/div[*]/a/href").get();
		System.out.println(string);
	}

	@Override
	public Site getSite() {
		
		return site;
	}
	
	public static void main(String[] args) {

		
		Test test = new Test();
		
		Spider.create(test).addUrl("http://365yg.com/search/?keyword=%E6%8A%96%E9%9F%B3").thread(1).run();
		
	}

}
*/

public class GithubRepoPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(1).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
    	String string = page.getHtml().links().xpath("//*[@id=\"pane-news\"]/div/ul/li[2]/strong/a/text()").toString();
    	System.out.println("++++++++++++"+string);
    	
       /* page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-]+/[\\w\\-]+)").all());
        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/[\\w\\-])").all());
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));*/
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
      Spider.create(new GithubRepoPageProcessor()).addUrl("http://news.baidu.com/").thread(1).run();
    	
    }
}