package com.example.avtoservis.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RobotsTxtController {

    @Value("${app.base-url}")
    private String baseUrl;



    @GetMapping(value = "/robots.txt", produces = "text/plain")
    @ResponseBody
    public String robotsTxt() {
        StringBuilder robots = new StringBuilder();

        robots.append("User-agent: *\n");

        // Блокируем служебные разделы
        robots.append("Disallow: /admin/\n");

        robots.append("Disallow: /actuator/\n");
        robots.append("Disallow: /error/\n");


        robots.append("Disallow: /child\n");


        // Сессионные параметры
        robots.append("Disallow: /*;jsessionid=*\n");
        robots.append("Disallow: /*?jsessionid=*\n");

        // Разрешаем важные страницы (не обязательно, но можно)
        robots.append("Allow: /\n"); // Разрешаем всё остальное

        // Sitemap
        robots.append("Sitemap: ").append(baseUrl).append("/sitemap.xml\n");



        return robots.toString();
    }
}

