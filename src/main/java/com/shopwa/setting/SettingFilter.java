package com.shopwa.setting;

import com.shopwa.Constants;
import com.shopwa.entity.Menu;
import com.shopwa.entity.setting.Setting;
import com.shopwa.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Component
@Order(-123)
// hiện một số xử lý trước khi request đi vào các servlet hoặc controller khác trong ứng dụng web
public class SettingFilter implements Filter {
    @Autowired private SettingService settingService;
    @Autowired private MenuService menuService;
    @Override
    // Khi request được yêu cầu thì trang web sẽ tự động lấy các response
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String url = servletRequest.getRequestURL().toString();


        if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg")) {
            chain.doFilter(request, response);
            return;
        }

        loadGeneralSettings(request);
        loadMenuSettings(request);

        chain.doFilter(request, response);
    }

    private void loadMenuSettings(ServletRequest request) {
        List<Menu> headerMenuItems = menuService.getHeaderMenuItems();
        request.setAttribute("headerMenuItems", headerMenuItems);

        List<Menu> footerMenuItems = menuService.getFooterMenuItems();
        request.setAttribute("footerMenuItems", footerMenuItems);
    }

    private void loadGeneralSettings(ServletRequest request) {
        List<Setting> generalSettings = settingService.getGeneralSettings();

        generalSettings.forEach(setting -> {
            request.setAttribute(setting.getKey(), setting.getValue());
        });

        request.setAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
    }
}
