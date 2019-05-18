package com.leyou.privilege.client;

import com.leyou.privilege.dto.ApplicationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: 姜光明
 * @Date: 2019/5/18 10:16
 */
@FeignClient("privilege-service")
public interface ApplicationClient {
    /**
     * 根据id和密码查询服务信息
     * @param id 服务id
     * @param secret 服务密钥
     * @return
     */
    @GetMapping("/app/query")
    ApplicationDTO queryByAppIdAndSecret(@RequestParam("id") Long id, @RequestParam("secret") String secret);

}
