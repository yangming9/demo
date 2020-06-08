package com.yangming.boot.demo.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JDContent {
    private String title;
    private String img;
    private String price;
}
