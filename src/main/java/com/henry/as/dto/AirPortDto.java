package com.henry.as.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NotNull
public class AirPortDto extends BaseDto {
    private int id;
    private String name;
    private String city;
    private String country;
    private String iata;
    private String icao;
    private double latitude;
    private double Longitude;
    private int altitude;
    private float timeZone;
    private String dst;
    private String tzFormat;
    private String type;
    private String source;

}
