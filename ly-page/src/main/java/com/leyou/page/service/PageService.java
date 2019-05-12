package com.leyou.page.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.item.ItemClient;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 姜光明
 * @Date: 2019/5/11 21:04
 */
@Slf4j
@Service
public class PageService {

    @Autowired
    private ItemClient itemClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${ly.static.itemDir}")
    private String itemDir;
    @Value("${ly.static.item}")
    private String itemTemplate;

    /**
     * 服务端单个页面渲染
     * @param id
     * @return
     */
    public Map<String, Object> loadItemData(Long id) {
        //创建容器
        Map<String, Object> map = new HashMap<>();


        SpuDTO spu = itemClient.querySpuBySpuid(id);

        List<SpecGroupDTO> specs = itemClient.querySpecsByCid(spu.getCid3());

        map.put("categories", itemClient.queryByIds(spu.getCategoryIds()));
        map.put("brand", itemClient.queryBrandByBrandId(spu.getBrandId()));
        map.put("spuName", spu.getName());
        map.put("subTitle", spu.getSubTitle());
        map.put("detail", spu.getSpuDetail());
        map.put("skus", spu.getSkus());
        map.put("specs", specs);

        return map;
    }


    public void createItemHtml(Long id) {

        //上下文，准备数据模型
        Context context = new Context();
        //调用之前写好的方法加载数据
        context.setVariables(loadItemData(id));

        //准备文件路径
        File dir = new File(itemDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                //创建失败，抛出异常
                log.error("【静态页服务】创建静态页目录失败，目录地址：{}", dir.getAbsolutePath());
                throw new LyException(ExceptionEnum.DIRECTORY_WRITER_ERROR);
            }
        }
        File filePath = new File(dir, id + ".html");
        //创建写入流
        try (PrintWriter printWriter = new PrintWriter(filePath, "utf-8")) {
            templateEngine.process("item", context, printWriter);
        } catch (IOException e) {
            log.error("【静态页服务】静态页生成失败，商品id：{}", id, e);
            throw new LyException(ExceptionEnum.FILE_WRITER_ERROR);
        }
    }

}
