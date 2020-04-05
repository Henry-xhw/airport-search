package com.henry.as.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sort {
    private String field;
    //ASC or DESC
    private String order;
}
