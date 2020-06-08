package com.yangming.boot.demo.system.controller;

import com.yangming.boot.demo.system.elasticsearch.JDContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class JDContentController {

    @Autowired
    private JDContentService jdContentService;

    @GetMapping("/parse/{keywords}")
    public Boolean parse(@PathVariable("keywords") String keywords) throws IOException {
        return jdContentService.parseContent(keywords);
    }

    @GetMapping("/parse/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> search(@PathVariable("keyword") String keyword,
                                           @PathVariable("pageNo") Integer pageNo,
                                           @PathVariable("pageSize") Integer pageSize
                                           )throws IOException{
        return jdContentService.searchPageWithHighlight(keyword,pageNo,pageSize);
    }

}
