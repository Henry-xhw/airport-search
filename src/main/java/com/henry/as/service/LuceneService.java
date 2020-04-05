package com.henry.as.service;

import com.henry.as.dto.AirLineAndRouteDto;
import com.henry.as.dto.AirPortDto;
import com.henry.as.dto.Query;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

public interface LuceneService {
    void createIndex() throws IOException;

    List<AirPortDto> searchAirPort(Query<AirPortDto> query) throws IOException, ParseException;

    List<AirLineAndRouteDto> searchAirLineAndRoute(Query<AirLineAndRouteDto> query) throws IOException, ParseException;
}
