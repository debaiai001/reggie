package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @className: LoginCheckFilter
 * @description: 检查用户是否已经完成登录
 * @author: Figure
 * @date: 2023/02/07 11:37
 **/
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1. 获取本次请求的URI
        String requestURI = request.getRequestURI();

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login",//移动端登录
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };
        //2. 判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3. 如果不需要处理，直接放行
        if (check){
//            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4-1. 判断登陆状态，如果已登录，直接放行
        if (request.getSession().getAttribute("employee") != null){
            Long empId = (Long)request.getSession().getAttribute("employee");
            log.info("用户已登录，用户id为: {}", empId);

            //把id放进threadlocal里
            BaseContext.setCurrentId(empId);

            long id = Thread.currentThread().getId();
            log.info("线程id为 {}" + id);

            filterChain.doFilter(request, response);
            return;
        }

        //4-2. 判断登陆状态，如果已登录，直接放行
        if (request.getSession().getAttribute("user") != null){
            Long empId = (Long)request.getSession().getAttribute("user");
            log.info("用户已登录，用户id为: {}", empId);

            //把id放进threadlocal里
            BaseContext.setCurrentId(empId);

            long id = Thread.currentThread().getId();
            log.info("线程id为 {}" + id);

            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录，本次请求{}", requestURI);
        //5. 如果未登录返回未登录结果，通过输出流方式向客户端页面响应数据，前端拦截并跳转登录页面，在requet.js中有前端拦截器
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /*
     * @Author Figure
     * @Description 路径匹配，检查本次请求是否需要放行
     * @Date 12:00 2023/2/7
     * @Param [urls, requestURI]
     * @return boolean
     **/
    public boolean check(String[] urls, String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
