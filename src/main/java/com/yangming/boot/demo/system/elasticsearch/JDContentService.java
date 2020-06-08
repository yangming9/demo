package com.yangming.boot.demo.system.elasticsearch;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JDContentService {

    Boolean parseContent(String keywords) throws IOException;

    List<Map<String,Object>> searchPage(String keyword, int pageNo, int pageSize)throws IOException;

    List<Map<String,Object>> searchPageWithHighlight(String keyword, int pageNo, int pageSize)throws IOException;
}
