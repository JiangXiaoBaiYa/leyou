package com.leyou;

import com.leyou.item.ItemClient;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.dto.CategoryDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/8 21:56
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeigncLient {
    @Autowired
    private ItemClient itemClient;

    @Test
    public void queryByIdList() {

        List<CategoryDTO> categoryDTOS = itemClient.queryByIds(Arrays.asList(1l, 2l, 3l));
        for (CategoryDTO categoryDTO : categoryDTOS) {
            System.out.println("categoryDTO = " + categoryDTO);
        }
        Assert.assertEquals(3, categoryDTOS.size());
    }
}
