package com.example.avtoservis.enums;

import lombok.Getter;

@Getter
public enum RequestStatus {
    NEW("Nový"),
    IN_PROGRESS("V řešení"),
    DONE("Vyřízeno");

    private final String displayName;

    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

}
