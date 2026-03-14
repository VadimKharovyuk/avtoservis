package com.example.avtoservis.config;

import com.example.avtoservis.service.SitemapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SitemapController {

    @Value("${app.base-url}")
    private String baseUrl;

    private final SitemapService sitemapService;
    private static final DateTimeFormatter W3C_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    @ResponseBody
    public String sitemap() {
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Головна
        addUrl(xml, "/", null, "1.0", "daily");

        // Про нас
        addUrl(xml, "/about", null, "0.8", "monthly");

        // Блог — список
        addUrl(xml, "/blog", null, "0.9", "daily");

        addUrl(xml, "/kategoriyi", null, "0.8", "weekly");

//        // Блог — пости
//        List<BlogPostResponse> blogPosts = sitemapService.getAllBlogPosts();
//        for (BlogPostResponse post : blogPosts) {
//            addUrl(xml, "/blog/" + post.getSlug(), post.getUpdatedAt(), "0.8", "weekly");
//        }





        // Reviews page (одна сторінка)
        addUrl(xml, "/review", LocalDateTime.now(), "0.5", "weekly");

        xml.append("</urlset>");

        return xml.toString();
    }

    private void addUrl(StringBuilder xml, String path, LocalDateTime lastmod,
                        String priority, String changefreq) {
        xml.append("  <url>\n");
        xml.append("    <loc>").append(baseUrl).append(path).append("</loc>\n");

        if (lastmod != null) {
            xml.append("    <lastmod>").append(lastmod.format(W3C_FORMAT)).append("</lastmod>\n");
        }

        xml.append("    <changefreq>").append(changefreq).append("</changefreq>\n");
        xml.append("    <priority>").append(priority).append("</priority>\n");
        xml.append("  </url>\n");
    }

    @GetMapping(value = "/llms.txt", produces = "text/plain; charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> llmsTxt() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/llms.txt");
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "text/plain; charset=UTF-8")
                .body(content);
    }
}
