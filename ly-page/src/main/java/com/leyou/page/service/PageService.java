package com.leyou.page.service;

import com.leyou.item.ItemClient;
import com.leyou.item.dto.SpecGroupDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 姜光明
 * @Date: 2019/5/11 21:04
 */
@Service
public class PageService {

    @Autowired
    private ItemClient itemClient;

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
}
