package com.itjing.mysql.mapper;

import com.itjing.mysql.entity.UploadFileTest;
import com.itjing.mysql.entity.UploadFileTestExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UploadFileTestMapper {
    long countByExample(UploadFileTestExample example);

    int deleteByExample(UploadFileTestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UploadFileTest record);

    int insertOrUpdate(UploadFileTest record);

    int insertOrUpdateSelective(UploadFileTest record);

    int insertSelective(UploadFileTest record);

    List<UploadFileTest> selectByExample(UploadFileTestExample example);

    UploadFileTest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UploadFileTest record, @Param("example") UploadFileTestExample example);

    int updateByExample(@Param("record") UploadFileTest record, @Param("example") UploadFileTestExample example);

    int updateByPrimaryKeySelective(UploadFileTest record);

    int updateByPrimaryKey(UploadFileTest record);

    int updateBatch(List<UploadFileTest> list);

    int updateBatchSelective(List<UploadFileTest> list);

    int batchInsert(@Param("list") List<UploadFileTest> list);
}