package com.henry.as.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirLineAndRouteDto extends BaseDto {
    private AirLineDto airLineDto;
    private RouteDto routeDto;
}
