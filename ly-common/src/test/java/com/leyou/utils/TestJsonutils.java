package com.leyou.utils;

/**
 * @Author: 姜光明
 * @Date: 2019/4/27 20:47
 */
import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import static com.leyou.common.utils.JsonUtils.nativeRead;


/**
 * @Author: 姜光明
 * @Date: 2019/4/27 20:24
 */

class TestJsonutils{
    public static void main(String[] args) {
        User user = new User("jack", 21);
        String s = JsonUtils.toString(user);
        System.out.println("s = " + s); //{"name":"jack","age":21}
        User user1 = JsonUtils.toBean(s, User.class);
        System.out.println(user1);

//         toList
        String json = "[20, -10, 5, 15]";
        List<Integer> list = JsonUtils.toList(json, Integer.class);
        System.out.println("list = " + list);


        // toMap
        String json2 = "{\"name\":\"Jack\", \"age\": \"21\"}";

        Map<String, String> map =JsonUtils.toMap(json2, String.class, String.class);
        System.out.println("map = " + map);

        String json3 = "[{\"name\":\"Jack\", \"age\": \"21\"}, {\"name\":\"Rose\", \"age\": \"18\"}]";

        List<Map<String, String>> maps = nativeRead(json3, new TypeReference<List<Map<String, String>>>() {
        });

        for (Map<String, String> map1 : maps) {
            System.out.println("map = " + map1);
        }
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User{
        String name;
        Integer age;
    }
}

