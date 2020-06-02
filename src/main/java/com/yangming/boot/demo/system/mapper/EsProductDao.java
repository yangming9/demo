package com.yangming.boot.demo.system.mapper;

import com.yangming.boot.demo.system.elasticsearch.EsProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface EsProductDao {
    List<EsProduct> getAllEsProductList(@Param("id") Long id);
}
