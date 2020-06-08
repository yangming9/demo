package com.yangming.boot.demo.system.util;

import com.yangming.boot.demo.system.model.JDContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTMLParseUtil {

    public static void main(String[] args) throws IOException {
        parseJD("电脑").forEach(System.out::println);
    }

    public static List<JDContent> parseJD(String keywords) throws IOException {
        //获取请求  https://search.jd.com/Search?keyword=java
        String url = "https://search.jd.com/Search?keyword=" + keywords;
        //解析网页
        Document document = Jsoup.parse(new URL(url), 30000);
        //所有js可以使用的都可以使用
        Element element = document.getElementById("J_goodsList");

        ArrayList<JDContent> goodList = new ArrayList<>();

        //获取所有的li元素
        Elements elements = element.getElementsByTag("li");
        //关于图片很多的网站 都是延时加载的
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("src");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();

            JDContent jdContent = new JDContent();

            jdContent.setImg(img);
            jdContent.setPrice(price);
            jdContent.setTitle(title);

            goodList.add(jdContent);
        }

        return goodList;
    }
}
