package com.itjing.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.itjing.excel.entity.User;
import com.itjing.excel.exception.CustomException;
import com.itjing.excel.handler.ExcelImportValidHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 用户导入监听类
 * @Author: lijing
 * @CreateTime: 2022-09-07 15:11
 */
@Slf4j
public class UserListener extends AnalysisEventListener<User> {

    /**
     * 分批导入，批量值
     */
    private static final int BATCH_COUNT = 100;

    List<User> cachedDataList = new ArrayList<>();

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(User data, AnalysisContext context) {
        try {
            // 通用方法数据校验
            ExcelImportValidHandler.valid(data);
        } catch (CustomException e) {
            log.error(e.getMessage());
            // 在easyExcel监听器中抛出业务异常
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
//        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList.clear();
            // 或者
//            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        log.info(JSON.toJSONString(cachedDataList));
        log.info("存储数据库成功！");
    }
}
