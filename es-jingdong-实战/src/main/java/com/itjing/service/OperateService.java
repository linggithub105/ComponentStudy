package com.itjing.service;

import com.alibaba.fastjson.JSONObject;
import com.itjing.dto.AddDocDto;
import com.itjing.dto.UpdateDocDto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author lijing
 * @date 2022年06月02日 14:14
 * @description ES操作类
 */
@Service
@Slf4j
public class OperateService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 添加文档
     *
     * @param addDocDto
     * @throws IOException
     */
    public void addDoc(AddDocDto addDocDto) throws IOException {

        IndexRequest indexRequest = new IndexRequest(addDocDto.getIndexName());

        indexRequest.source(JSONObject.toJSONString(addDocDto.getData()), XContentType.JSON);
        indexRequest.id(addDocDto.getId());

        // 创建索引
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        log.info("执行结果:" + JSONObject.toJSONString(index));
    }

    /**
     * 更新文档(仅仅根据id更新，是增量字段)
     *
     * @param updateDocDto
     * @throws IOException
     */
    public void updateDoc3(UpdateDocDto updateDocDto) throws IOException {

        UpdateRequest indexRequest = new UpdateRequest(updateDocDto.getIndexName(), updateDocDto.getId());

        // 设置数据
        indexRequest.doc(updateDocDto.getData());

        // 更新文档
        UpdateResponse update = restHighLevelClient.update(indexRequest, RequestOptions.DEFAULT);

        log.info("执行结果:" + JSONObject.toJSONString(update));
    }
}
