package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.ItemClient;
import com.leyou.item.dto.*;
import com.leyou.search.pojo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 姜光明
 * @Date: 2019/5/8 22:26
 */
@Service
public class SearchService {

    @Autowired
    private ItemClient itemClient;

    /**
     * 把一个Spu转成一个Goods对象
     * @param spu
     * @return
     */
    public Goods BuildGoods(SpuDTO spu) {
        //构建goods对象,准备填数据
        Goods goods = new Goods();

        //1.查询的搜索字段all
        //1.1品牌名的查询
        String brandName = itemClient.queryBrandByBrandId(spu.getBrandId()).getName();
        //1.2spu名称的查询
        String name = spu.getName();
        //1.3分类名的查询
        String collectNames = itemClient.queryByIds(spu.getCategoryIds())
                .stream().map(CategoryDTO::getName).collect(Collectors.joining(","));
        //1.4拼接所有的搜索字段
        String all = brandName + name + collectNames;

        //2.查询所有的sku集合的json格式(只需四个字段,把它摘取出来)
        List<Map<String, Object>> skulist = new ArrayList<>();
        List<SkuDTO> skus = itemClient.querySkuById(spu.getId());
//        List<SkuDTO> skus = spu.getSkus();
        for (SkuDTO skuDTO : skus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", skuDTO.getId());
            map.put("title", skuDTO.getTitle());
            //因images为数据,截取出第一个展示即可,工具类可防止空指针异常
            map.put("images", StringUtils.substringBefore(skuDTO.getImages(), ","));
            map.put("price", skuDTO.getPrice());
            //把每个sku对象添加到集合中
            skulist.add(map);
        }
        //序列化sku集合为json串
        String skuJson = JsonUtils.toString(skulist);

        //3.查询所有sku的价格集合
        Set<Long> skuPrice = skus.stream().map(SkuDTO::getPrice).collect(Collectors.toSet());


        //4.获取所有的规格参数
        HashMap<String, Object> specs = new HashMap<>();
        //4.1获取规格参数key,来自于specParam中当前类下的所有需要搜索的规格
        List<SpecParamDTO> specParams = itemClient.querySpecParamsList(null, spu.getCid3(), true);
        //4.2 获取规格参数的值,来自于spuDetail
        SpuDetailDTO spuDetailDTO = itemClient.querySpuDetailById(spu.getId());
        //4.2.1通用规格参数值
        Map<String, Object> genericSpec = JsonUtils.toMap(spuDetailDTO.getGenericSpec(), String.class, Object.class);
        //4.2.2特有规格参数值
        Map<Long, List<String>> specSpec = JsonUtils.nativeRead(spuDetailDTO.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });
        for (SpecParamDTO specParam : specParams) {
            //获取规格参数的名称
            String key = specParam.getName();
            //获取规格参数值
            Object value = null;
            //判断是否是通用规格参数
            if (specParam.getGeneric()) {
                //通用规格
                value = genericSpec.get(specParam.getId());
            } else {
                //特有规格
                value = specSpec.get(specParam.getId());
            }

            //判断是否是数字类型
            if (specParam.getNumeric()) {
                //是数字类型,就分段
                value = chooseSegment(value, specParam);
            }
            //添加到specs
            specs.put(key, value);
        }


        goods.setBrandId(spu.getBrandId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setCategoryId(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime().getTime());
        goods.setId(spu.getId());
        goods.setAll(all); // 查询的搜索字段all  分类名+spu的名称+品牌名
        goods.setSkus(skuJson); // 查询所有的sku集合的josn格式
        goods.setPrice(skuPrice); // 查询所有sku的价格集合
        goods.setSpecs(null); //TODO 获取所有的规格参数
        return goods;
    }

    private String chooseSegment(Object value, SpecParamDTO p) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其它";
        }
        double val = parseDouble(value.toString());
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0;
        }
    }
}
