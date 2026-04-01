package com.example.avtoservis.seo;

import com.example.avtoservis.util.PageSEODto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.avtoservis.enums.Language;


import java.util.Map;

@Component
public class HomePageSeoBuilder {

    @Value("${app.base-url}")
    private String baseUrl;

    // Тексти для кожної мови
    private static final Map<Language, SeoTexts> TEXTS = Map.of(
            Language.CS, new SeoTexts(
                    "Autoservis a opravy vozů v Brně | EN-motors",
                    "Servis a opravy osobních a užitkových vozů v centru Brna. Opravy ihned bez objednání. Pneuservis, diagnostika, příprava na STK.",
                    "autoservis brno, oprava auta brno, pneuservis brno, servis vozů, STK příprava, diagnostika auta",
                    "cs_CZ"
            ),
            Language.EN, new SeoTexts(
                    "Car repair and service in Brno | EN-motors",
                    "Car repair and service in the center of Brno. Immediate repairs without appointment. Tire service, diagnostics, MOT preparation.",
                    "car repair brno, auto service brno, tire service brno, car diagnostics, MOT preparation",
                    "en_GB"
            ),
            Language.UK, new SeoTexts(
                    "Автосервіс та ремонт авто у Брно | EN-motors",
                    "Сервіс та ремонт легкових і комерційних авто в центрі Брно. Ремонт одразу без запису. Шиномонтаж, діагностика, підготовка до ТО.",
                    "автосервіс брно, ремонт авто брно, шиномонтаж брно, діагностика авто, підготовка до ТО",
                    "uk_UA"
            )
    );

    public PageSEODto buildHomePageSeo(Language language) {
        SeoTexts texts = TEXTS.getOrDefault(language, TEXTS.get(Language.CS));
        String langUrl = baseUrl + "/" + language.getCode();

        return PageSEODto.builder()
                .title(texts.title)
                .description(texts.description)
                .keywords(texts.keywords)
                .canonicalUrl(langUrl)
                .robots("index, follow, max-image-preview:large, max-snippet:-1, max-video-preview:-1")
                .language(language.getCode())
                .author("EN-motors")
                .themeColor("#1e293b")

                .ogTitle(texts.title)
                .ogDescription(texts.description)
                .ogUrl(langUrl)
                .ogType("website")
                .ogImage(baseUrl + "/images/Logo-image.jpg")
                .ogImageAlt("EN-motors — autoservis Brno")
                .ogSiteName("EN-motors")
                .ogLocale(texts.locale)

                .twitterCard("summary_large_image")
                .twitterTitle(texts.title)
                .twitterDescription(texts.description)
                .twitterImage(baseUrl + "/images/Logo-image.jpg")
                .twitterImageAlt("EN-motors — autoservis Brno")

                .build();
    }

    // Внутрішній record для зберігання текстів
    private record SeoTexts(String title, String description, String keywords, String locale) {}
}

