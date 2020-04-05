package com.henry.as.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirPort extends BaseDomainObject {
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
