package com.example.avtoservis.enums;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
@Getter
public enum Language {
    UK("uk", "Українська", "🇺🇦", true, "uk_UA"),
    EN("en", "English", "🇺🇸", true, "en_US"),
    CS("cs", "Čeština", "🇨🇿", true, "cs_CZ");// потом-потом



    private final String code;
    private final String displayName;
    private final String flag;
    private final boolean isEnabled;
    private final String locale; // Для Spring Locale

    Language(String code, String displayName, String flag, boolean isEnabled, String locale) {
        this.code = code;
        this.displayName = displayName;
        this.flag = flag;
        this.isEnabled = isEnabled;
        this.locale = locale;
    }

    public static Language fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return UK;
        }

        return Arrays.stream(values())
                .filter(lang -> lang.code.equalsIgnoreCase(code.trim()))
                .findFirst()
                .orElse(UK);
    }

    public static List<Language> getEnabledLanguages() {
        return Arrays.stream(values())
                .filter(Language::isEnabled)
                .collect(Collectors.toList());
    }

    // ←←← ЭТО ОБЯЗАТЕЛЬНО ←←←
    public static boolean isSupported(String code) {
        if (code == null) return false;
        String lower = code.toLowerCase();
        return Arrays.stream(values())
                .anyMatch(lang -> lang.code.equals(lower) || lower.startsWith(lang.code));
    }
    // Получить Locale для Spring
    public Locale toLocale() {
        String[] parts = locale.split("_");
        return new Locale(parts[0], parts[1]);
    }
}
