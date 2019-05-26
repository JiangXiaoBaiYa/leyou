package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.utils.BeanHelper;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.ItemClient;
import com.leyou.search.dto.GoodsDTO;
import com.leyou.search.dto.SearchRequest;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.user.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate esTemplate;

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
        Map<Long, Object> genericSpec = JsonUtils.toMap(spuDetailDTO.getGenericSpec(), Long.class, Object.class);
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
        goods.setSpecs(specs); // 获取所有的规格参数
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

    /**
     * 搜索
     *
     * @param searchRequest
     * @return
     */
    public PageResult<GoodsDTO> search(SearchRequest searchRequest) {
        //0.健壮性判断
        if (StringUtils.isBlank(searchRequest.getKey())) {
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        //1.构建原生搜索查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //2.装载搜索策略
        queryBuilder.withQuery(basicQuery(searchRequest));
        //3.过滤下要显示的字段,控制字段数量
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        //4.分页条件的构建
        int page = searchRequest.getPage() - 1;
        int size = searchRequest.getSize();
        queryBuilder.withPageable(PageRequest.of(page, size));

        //5.搜索结果
        AggregatedPage<Goods> result = esTemplate.queryForPage(queryBuilder.build(), Goods.class);
        //6.解析结果

        long total = result.getTotalElements();  //查询到的总结果数
        int totalPage = result.getTotalPages();  //查询到的总页数
        List<Goods> list = result.getContent();  //查询到的结果集
        //转换成页面需要的DTO
        List<GoodsDTO> goodsDTOS = BeanHelper.copyWithCollection(list, GoodsDTO.class);

        //7封装到页面结果集

        return new PageResult<>(total, totalPage, goodsDTOS);
    }

    private QueryBuilder basicQuery(SearchRequest searchRequest) {
        //构建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //构建基本的match查询
        queryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));
        //构建过滤条件
        Map<String, String> filter = searchRequest.getFilter();
        if (!CollectionUtils.isEmpty(filter)) {
            for (Map.Entry<String, String> entry : filter.entrySet()) {
                //获取过滤条件的key
                String key = entry.getKey();
                //规格参数的key要做前缀specs
                if ("分类".equals(key)) {
                    key = "categoryId";
                } else if ("品牌".equals(key)) {
                    key = "brandId";
                } else {
                    key = "specs." + key;
                }
                //value
                String value = entry.getValue();
                //添加过滤条件
                queryBuilder.filter(QueryBuilders.termQuery(key, value));
            }
        }
        return queryBuilder;
    }

    /**
     * 查询过滤项
     *
     * @param searchRequest
     * @return
     */
    public Map<String, List<?>> queryFilters(SearchRequest searchRequest) {
        //1.创建过滤项集合
        Map<String, List<?>> filterList = new LinkedHashMap<>();

        //2.创建原生搜索构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //3.添加构造条件
        queryBuilder.withQuery(basicQuery(searchRequest));
        //3.1因为只要聚合结果,减少source,显示空的source，提高查询效率
        queryBuilder.withSourceFilter(new FetchSourceFilterBuilder().build());
        //3.2减少hits的数据，每页显示 1个
        queryBuilder.withPageable(PageRequest.of(0, 1));

        //4.添加聚合条件
        //4.1分类的聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("categoryAgg").field("categoryId"));
        //4.2品牌的聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brandId"));

        //5.查询聚合结果
        AggregatedPage<Goods> result = esTemplate.queryForPage(queryBuilder.build(), Goods.class);

        //6.解析聚合结果(获取所有的聚合结果)
        Aggregations aggregations = result.getAggregations();
        //6.1解析分类的聚合获取List<Long>所有的ids
        LongTerms cTerms = aggregations.get("categoryAgg");
        //6.1.1解析buckets
        List<Long> ids = cTerms.getBuckets().stream()
                .map(LongTerms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());
        //6.1.2根据ids查询分类
        List<CategoryDTO> categoryDTOS = itemClient.queryByIds(ids);

        //6.2解析品牌的聚合获取List<Long>所有的ids
        LongTerms bTerms = aggregations.get("brandAgg");
        //6.2.1解析buckets
        List<Long> longs = bTerms.getBuckets().stream()
                .map(LongTerms.Bucket::getKeyAsNumber)
                .map(Number::longValue)
                .collect(Collectors.toList());
        //6.2.2根据ids查询品牌
        List<BrandDTO> brandDTOS = itemClient.queryBrandByIds(longs);

        //6.3放入过滤项集合中并返回
        filterList.put("分类", categoryDTOS);
        filterList.put("品牌", brandDTOS);

        //       规格参数处理
        if (ids != null && ids.size() == 1) {
            handleSpecAgg(ids.get(0), filterList,searchRequest);
        }

        return filterList;
    }

    /**
     * 过滤规格参数
     * @param cid
     * @param filterList
     * @param searchRequest
     */
    private void handleSpecAgg(Long cid, Map<String, List<?>> filterList, SearchRequest searchRequest) {
        //1.根据分类id查询这个分类下需要搜索的规格参数
        List<SpecParamDTO> specParams = itemClient.querySpecParamsList(null, cid, true);
        //2.创建原生搜索构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //3.添加构造条件
        queryBuilder.withQuery(basicQuery(searchRequest));
        //3.2减少hits的数据，每页显示 1个
        queryBuilder.withPageable(PageRequest.of(0, 1));
        //3.1因为只要聚合结果,减少source,显示空的source，提高查询效率
        queryBuilder.withSourceFilter(new FetchSourceFilterBuilder().build());


        //4.添加聚合条件,对查询出的规格参数聚合
        for (SpecParamDTO specParam : specParams) {
            //获取param的name作为聚合名称
            String name = specParam.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name));
        }

        //5.查询数据
        AggregatedPage<Goods> result = esTemplate.queryForPage(queryBuilder.build(), Goods.class);
        Aggregations aggregations = result.getAggregations();

        //6.解析聚合结果
        for (SpecParamDTO specParam : specParams) {
            String name = specParam.getName();
            StringTerms terms = aggregations.get(name);
            // 获取聚合结果，注意，规格聚合的结果 直接是字符串，不用做特殊处理
            List<String> spec = terms.getBuckets()
                    .stream()
                    .map(StringTerms.Bucket::getKeyAsString)
                    .collect(Collectors.toList());
            //解析聚合结果，放到filterList中
            filterList.put(name, spec);
        }
    }

    /**
     * 新增索引
     *
     * @param id
     */
    public void createIndex(Long id) {
        //查询spu
        SpuDTO spu = itemClient.querySpuBySpuid(id);
        //构建goods对象
        Goods goods = this.BuildGoods(spu);
        //保存数据到索引库
        goodsRepository.save(goods);
    }

    /**
     * 删除索引
     * @param id
     */
    public void deleteById(Long id) {
        goodsRepository.deleteById(id);
    }
}
