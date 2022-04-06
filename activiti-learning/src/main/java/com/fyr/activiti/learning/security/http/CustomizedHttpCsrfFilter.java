package com.fyr.activiti.learning.security.http;

import com.fyr.activiti.learning.constant.SecurityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// @Component
public class CustomizedHttpCsrfFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(CustomizedHttpCsrfFilter.class);
    private List<String> excludeList = new ArrayList<String>();
    private boolean isActived = false;
    private final List<String> values = new ArrayList<>() {
        {
            add("1");
            add("on");
            add("true");
        }
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (logger.isDebugEnabled()) {
            logger.debug("customized csrf filter initializing ...");
        }

        String exclude = filterConfig.getInitParameter(SecurityConstant.EXCLUDE.getName());
        if (StringUtils.hasLength(exclude)) {
            excludeList = Arrays.stream(exclude.split(",")).collect(Collectors.toList());
        }

        String is_actived = filterConfig.getInitParameter(SecurityConstant.IS_ACTIVED.getName());
        if (StringUtils.hasLength(is_actived) && values.contains(is_actived)) {
            isActived = true;
        }

        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(!isActived){
            chain.doFilter(request, response);
            return ;
        }
        if(logger.isDebugEnabled()){
            logger.debug("customized csrf filter is running");
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Object token = session.getAttribute("token");
        if(!"post".equalsIgnoreCase(req.getMethod()) || handleExcludeURL(req, resp) || token == null){
            chain.doFilter(request, response);
            return;
        }

        String requestToken = req.getParameter("token");
        if(StringUtils.hasLength(requestToken) || !requestToken.equals(token)){
            // AjaxResponseWriter.write(req, resp, ServiceStatusEnum.ILLEGAL_TOKEN, "非法的token");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        if (!CollectionUtils.isEmpty(excludeList)) {
            String url = request.getServletPath();
            for (String pattern : excludeList) {
                Pattern p = Pattern.compile("^" + pattern);
                Matcher m = p.matcher(url);
                if (m.find()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
