package com.leyou.order.web;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.service.OrderService;
import com.leyou.order.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: 姜光明
 * @Date: 2019/5/26 9:30
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 新增订单
     * @param orderDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderDTO orderDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDTO));
    }

    /**
     * 根据订单编号查询订单详情
     * @param orderId
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<OrderVO> queryOrderById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.queryOrderById(orderId));
    }
}

