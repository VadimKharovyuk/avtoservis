package com.example.avtoservis.enums;

import lombok.Getter;

@Getter
public enum RequestType {
    CALLBACK("Zpětné volání"),
    SERVICE_ORDER("Objednávka servisu"),
    TIRE_SERVICE("Pneuservis");

    private final String displayName;

    RequestType(String displayName) {
        this.displayName = displayName;
    }
}
