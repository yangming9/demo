package com.yangming.boot.demo.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * elasticsearch 测试使用model
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElaUser {

    private String name;

    private Integer age;

}
