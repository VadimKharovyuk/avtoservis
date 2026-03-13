package com.example.avtoservis.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    public static final String ADMIN_SESSION_KEY = "adminLoggedIn";

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession();

        // Якщо вже авторизований
        if (Boolean.TRUE.equals(session.getAttribute(ADMIN_SESSION_KEY))) {
            return true;
        }

        // Пропускаємо сторінку логіну
        String uri = request.getRequestURI();
        if (uri.equals("/admin/login") || uri.equals("/admin/logout")) {
            return true;
        }

        // Перенаправляємо на логін
        response.sendRedirect("/admin/login");
        return false;
    }

    public boolean checkCredentials(String username, String password) {
        return adminUsername.equals(username) && adminPassword.equals(password);
    }
}