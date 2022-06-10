package com.itjing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lijing
 * @date 2022年06月02日 14:21
 * @description 更新文档的dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDocDto {

    private String indexName;

    private String id;

    private Object data;

}
