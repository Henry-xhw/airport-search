package com.henry.as.controller;

import com.henry.as.dto.AirPortDto;
import com.henry.as.dto.Query;
import com.henry.as.service.LuceneService;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/airports", consumes = "application/vnd.henry.airport-search.v1+json", produces =
        "application/vnd.henry.airport-search.v1+json")
public class AirPortSearchController {

    @Autowired
    private LuceneService luceneService;

    @PostMapping
    public List<AirPortDto> searchAirPort(@RequestBody @Validated Query<AirPortDto> query) throws IOException, ParseException {
        return luceneService.searchAirPort(query);
    }
}
