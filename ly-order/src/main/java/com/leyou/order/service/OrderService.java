package com.leyou.order.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.enums.OrderStatusEnum;
import com.leyou.common.exceptions.LyException;
import com.leyou.common.threadlocals.UserHolder;
import com.leyou.common.utils.BeanHelper;
import com.leyou.common.utils.IdWorker;
import com.leyou.item.ItemClient;
import com.leyou.order.dto.CartDTO;
import com.leyou.order.dto.OrderDTO;
import com.leyou.order.entity.Order;
import com.leyou.order.entity.OrderDetail;
import com.leyou.order.entity.OrderLogistics;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderLogisticsMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.vo.OrderDetailVO;
import com.leyou.order.vo.OrderLogisticsVO;
import com.leyou.order.vo.OrderVO;
import com.leyou.user.client.UserClient;
import com.leyou.user.dto.AddressDTO;
import com.leyou.user.dto.SkuDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: 姜光明
 * @Date: 2019/5/26 9:31
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper detailMapper;

    @Autowired
    private OrderLogisticsMapper logisticsMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ItemClient itemClient;

    @Transactional
    public Long createOrder(OrderDTO orderDTO) {

        //1 写order
        Order order = new Order();
        //1.1订单编号
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);

        //1.2登录用户
        Long userId = UserHolder.getUser();
        order.setUserId(userId);

        //1.3金额相关信息
        List<CartDTO> carts = orderDTO.getCarts();
        //获取所有sku的id
        List<Long> idList = carts.stream().map(CartDTO::getSkuId).collect(Collectors.toList());
        //处理CartDTO为一个map，其key是skuId；值为num
        Map<Long, Integer> numMap = carts.stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
        //1.3.1 查询sku
        List<SkuDTO> skuList = itemClient.querySkuByIds(idList);
        //定义一个Order Detail的集合
        List<OrderDetail> details = new ArrayList<>();

        //1.3.2 计算金额的和
        long total = 0;
        for (SkuDTO skuDTO : skuList) {
            int num = numMap.get(skuDTO.getId());
            //计算总金额
            total += skuDTO.getPrice() * num;
            //组装OrderDetail
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orderId);
            detail.setImage(StringUtils.substringBefore(skuDTO.getImages(), ","));
            detail.setNum(num);
            detail.setSkuId(skuDTO.getId());
            detail.setOwnSpec(skuDTO.getOwnSpec());
            detail.setPrice(skuDTO.getPrice());
            detail.setTitle(skuDTO.getTitle());
            details.add(detail);
        }

        //1.3.3 填写金额数据
        order.setTotalFee(total);

        order.setPaymentType(orderDTO.getPaymentType());
//        order.setActualFee(total + order.getPostFee()/* - 优惠金额*/);
        order.setPostFee(0l);
        order.setActualFee(total+ order.getPostFee());

        //1.4订单状态初始化
        order.setStatus(OrderStatusEnum.INIT.value());

        //1.5写order到数据库
        int count = orderMapper.insertSelective(order);
        if (count != 1) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //2写OrderDetail
        count = detailMapper.insertDetailList(details);
        if (count != details.size()) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        // 3 写orderLogistics
        // 3.1查询收获地址
        AddressDTO addr = userClient.queryAddressById(userId, orderDTO.getAddressId());
        // 3.2 填写物流信息
        OrderLogistics logistics = BeanHelper.copyProperties(addr, OrderLogistics.class);
        logistics.setOrderId(orderId);

        count = logisticsMapper.insertSelective(logistics);
        if (count != 1) {
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

        //4 减库存
        itemClient.minusStock(numMap);

        return orderId;
    }

    public OrderVO queryOrderById(Long orderId) {
        //1.查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            // 不存在
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        //判断用户id是否正确
        Long userId = UserHolder.getUser();
        if (!userId.equals(order.getUserId())) {
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }

        //2.查询订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(orderId);
        List<OrderDetail> details = detailMapper.select(detail);
        if (CollectionUtils.isEmpty(details)) {
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }

        //3.查询订单详情
        OrderLogistics logistics = logisticsMapper.selectByPrimaryKey(orderId);
        if (logistics == null) {
            throw new LyException(ExceptionEnum.ORDER_NOT_FOUND);
        }

        //4.封装数据
        OrderVO orderVO = BeanHelper.copyProperties(order, OrderVO.class);
        orderVO.setDetailList(BeanHelper.copyWithCollection(details, OrderDetailVO.class));
        orderVO.setLogistics(BeanHelper.copyProperties(logistics, OrderLogisticsVO.class));
        return orderVO;
    }
}
