package com.leyou.page.controller;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @Author: 姜光明
 * @Date: 2019/5/11 20:24
 */
@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("item/{id}.html")
    public String toItemPage(Model model, @PathVariable("id") Long id) {
        //查询数据模型
        Map<String, Object> itemData = pageService.loadItemData(id);
        //存入模型数据
        model.addAllAttributes(itemData);
        return "item";
    }
}
