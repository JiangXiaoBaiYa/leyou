package com.leyou.order.vo;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * @Author: 姜光明
 * @Date: 2019/5/26 14:30
 */
@Data
public class OrderVO {
    /**
     * 订单编号
     */
    @Id
    private Long orderId;
    /**
     * 商品金额
     */
    private Long totalFee;
    /**
     * 邮费
     */
    private Long postFee = 0L;
    /**
     * 实付金额
     */
    private Long actualFee;
    /**
     * 付款方式：1:在线支付, 2:货到付款
     */
    private Integer paymentType;
    /**
     * 优惠促销的活动id，
     */
    private String promotionIds;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 付款时间
     */
    private Date payTime;
    /**
     * 发货时间
     */
    private Date consignTime;
    /**
     * 确认收货时间
     */
    private Date endTime;
    /**
     * 交易关闭时间
     */
    private Date closeTime;
    /**
     * 评价时间
     */
    private Date commentTime;
    /**
     * 发票类型，0无发票，1普通发票，2电子发票，3增值税发票
     */
    private Integer invoiceType = 0;
    /**
     *  订单来源 1:app端，2：pc端，3：微信端
     */
    private Integer sourceType = 1;
    /**
     * 订单物流信息
     */
    private OrderLogisticsVO logistics;
    /**
     * 订单详情信息
     */
    private List<OrderDetailVO> detailList;
}
