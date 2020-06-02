package com.yangming.boot.demo.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.yangming.boot.demo.system.mapper")
public class MyBatisConfig {
}
