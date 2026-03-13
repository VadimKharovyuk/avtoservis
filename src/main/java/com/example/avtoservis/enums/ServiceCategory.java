package com.example.avtoservis.enums;

import lombok.Getter;

@Getter
public enum ServiceCategory {
    AUTO_SERVICE("Autoservis"),
    TIRE_SERVICE("Pneuservis");

    private final String displayName;

    ServiceCategory(String displayName) {
        this.displayName = displayName;
    }

}
