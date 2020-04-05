package com.henry.as.service;

import com.henry.as.dto.AirLineAndRouteDto;
import com.henry.as.dto.AirPortDto;
import com.henry.as.dto.Query;
import com.henry.as.repository.LuceneRepository;

import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class LuceneServiceImpl implements LuceneService {

    @Autowired
    private LuceneRepository luceneRepo;
    @Override
    public void createIndex() throws IOException {
        luceneRepo.createIndex();
    }

    @Override
    public List<AirPortDto> searchAirPort(Query<AirPortDto> query) throws IOException, ParseException {
        return luceneRepo.searchAirPort(query);
    }

    @Override
    public List<AirLineAndRouteDto> searchAirLineAndRoute(Query<AirLineAndRouteDto> query) throws IOException, ParseException {
        return luceneRepo.searchAirLineAndRoute(query);
    }
}
