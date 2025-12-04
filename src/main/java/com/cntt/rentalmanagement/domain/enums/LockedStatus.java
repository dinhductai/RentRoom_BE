package com.cntt.rentalmanagement.domain.enums;

public enum LockedStatus {
    ENABLE("ENABLE"),
    DISABLE("DISABLE");


    private String value;

    LockedStatus(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
