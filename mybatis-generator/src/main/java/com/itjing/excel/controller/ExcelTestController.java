package com.itjing.excel.controller;

import com.itjing.excel.util.ExcelUtil;
import com.itjing.excel.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author lijing
 * @date 2022年06月28日 17:38
 * @description
 */
@RestController
public class ExcelTestController {

    @GetMapping(value = "/excel/importTemplate", produces = {"application/json;charset=UTF-8"})
    public void importTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户数据.xlsx", "utf-8"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        ExcelUtil<UserVO> util = new ExcelUtil<>(UserVO.class);
        util.importTemplateExcel("用户数据", response);
    }
}
