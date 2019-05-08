package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: 姜光明
 * @Date: 2019/5/8 22:19
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
