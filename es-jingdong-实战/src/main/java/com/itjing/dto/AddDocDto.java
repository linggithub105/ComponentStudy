package com.itjing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lijing
 * @date 2022年06月02日 14:15
 * @description 添加文档dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddDocDto {

    private String indexName;

    private String id;

    private Object data;
}
