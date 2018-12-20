package com.nmys.story.webmagic;

import java.util.Map;

import org.jsoup.select.Elements;

import com.luway.pikachu.core.pipeline.BasePipeline;

public class TestPipeline extends BasePipeline<TestBean> {
    public TestPipeline(TestBean t) {
    	 super(t);
		// TODO Auto-generated constructor stub
	}
    @Override
    public void output(Map<String, Elements> result, String url) {
        System.out.println(result.get("content"));
    }
}