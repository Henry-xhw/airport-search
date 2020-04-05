package com.henry.as.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Query<T> {
    @NotNull
    private T params;
    private Sort sort;
    @NotNull
    private Map<String, String> queryParam;

}
