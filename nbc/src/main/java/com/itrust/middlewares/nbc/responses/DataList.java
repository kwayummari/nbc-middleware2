package com.itrust.middlewares.nbc.responses;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class DataList {

    private final List<String> body;

    @JsonCreator
    public DataList(List<String> attrs) {
        this.body = attrs;
    }

    @Override
    public String toString() {
        return "{" +
                "attr=" + body +
                '}';
    }
}
