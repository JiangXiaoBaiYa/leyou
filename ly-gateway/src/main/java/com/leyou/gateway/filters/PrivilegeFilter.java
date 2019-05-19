package com.leyou.gateway.filters;

import com.leyou.gateway.properties.JwtProperties;
import com.leyou.gateway.task.PrivilegeTokenHoder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @Author: 姜光明
 * @Date: 2019/5/19 15:15
 */
public class PrivilegeFilter extends ZuulFilter {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private PrivilegeTokenHoder tokenHoder;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * PRE_DECORATION_FILTER 是Zuul默认的处理请求头的过滤器，我们放到这个之后执行
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        //将token存入请求头中
        context.addZuulRequestHeader(prop.getApp().getHeaderName(),tokenHoder.getToken());
        return null;
    }
}
