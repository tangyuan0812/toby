package com.nmys.story.webmagic;

import com.luway.pikachu.core.annotations.CssPath;
import com.luway.pikachu.core.annotations.MathUrl;
import com.luway.pikachu.core.annotations.MathUrl.Method;

@MathUrl(url = "https://www.dailyenglishquote.com/", method = Method.GET)
public class TestBean {

    @CssPath(selector = "#content")
    private String content;
}
