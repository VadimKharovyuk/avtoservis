package com.example.avtoservis.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageSEODto {
    private String title;
    private String description;
    private String keywords;
    private String canonicalUrl;
    private String ogImage;
    private String ogType;


    // Open Graph теги

    private String ogTitle;
    private String ogDescription;
    private String ogUrl;

    private String ogImageAlt;
    private String ogSiteName;
    private String ogLocale;

    // Twitter Card теги
    private String twitterCard;
    private String twitterTitle;
    private String twitterDescription;
    private String twitterImage;
    private String twitterImageAlt;

    // Дополнительные теги
    private String author;
    private String robots;
    private String language;
    private String themeColor;
}
