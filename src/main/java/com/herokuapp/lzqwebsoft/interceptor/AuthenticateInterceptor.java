package com.herokuapp.lzqwebsoft.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import com.herokuapp.lzqwebsoft.pojo.Menu;
import com.herokuapp.lzqwebsoft.service.MenuService;
import com.herokuapp.lzqwebsoft.util.CommonConstant;

/**
 * 用户管理菜单权限，如果用户没有登录，则能查看的菜单是有限的。
 *
 * @author zqluo
 */
public class AuthenticateInterceptor implements HandlerInterceptor {

    @Autowired
    private MenuService menuService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model) {
        HttpSession session = request.getSession();
        boolean isLogined = session.getAttribute(CommonConstant.LOGIN_USER) != null;
        List<Menu> menus = menuService.getAllMenus(isLogined);
        request.setAttribute(CommonConstant.MENUS, menus);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 用于过滤掉普通用户不能访问的页面，如果用户没权限访问则重定向到index.html画面
        HttpSession session = request.getSession();
        boolean isLogined = session.getAttribute(CommonConstant.LOGIN_USER) != null;
        if (!isLogined) {
            String requestURL = new UrlPathHelper().getPathWithinApplication(request);
            ResourceBundle rb = ResourceBundle.getBundle("checkpath");
            // 得到需要检查的URL的个数
            int count = Integer.parseInt(rb.getString("authentication.checkpath.count"));
            String url = null;
            while (count > 0) {
                url = rb.getString("authentication.checkpath." + count);
                url = url.replaceAll("\\.html", "(.html)?");  // 意思就是不是.html后缀的也要拦截
                Pattern pattern = Pattern.compile(".*" + url + "$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(requestURL);
                if (url != null && matcher.matches()) {
                    if (!"XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
                        // AJAX请求，则不将URL放入session,登录成功后不跳AJAX链接
                        session.setAttribute(CommonConstant.LAST_REQUEST_URL, requestURL);
                        response.sendRedirect(request.getContextPath() + "/signIn.html");
                    } else {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json;charset=UTF-8");

                        Map<String, String> json = new HashMap();
                        json.put("status", "-1");
                        json.put("messages", "用户未登录，请先登录！");
                        json.put("redirect_url", request.getContextPath() + "/signIn.html");
                        response.getWriter().write(new Gson().toJson(json));
                    }
                    return false;
                }
                count--;
            }
            return true;
        } else {
            return true;
        }
    }
}
