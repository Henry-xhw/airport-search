package com.henry.as.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.henry.as.dto.AirPortDto;
import com.henry.as.dto.Query;
import com.henry.as.service.LuceneService;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AirPortSearchController.class)
public class AirPortSearchControllerTestCase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LuceneService luceneService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void searchAirPortSuccess() throws Exception {
        List<AirPortDto> apDtos = new ArrayList<>();
        AirPortDto apDto = new AirPortDto();
        apDto.setIata("test");
        apDto.setId(425);
        Map<String, String> map = new HashMap<>();
        map.put("searchKeyStr", "test");
        Query<AirPortDto> query = new Query<>();
        query.setParams(apDto);
        query.setQueryParam(map);
        when(luceneService.searchAirPort(query)).thenReturn(apDtos);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/airports").contentType(
                "application/vnd.henry" +
                ".airport-search.v1+json").accept("application/vnd.henry.airport-search.v1+json")
                .content(objectMapper.writeValueAsString(query))).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assert.assertTrue(StringUtils.isNotEmpty(result));
    }

    @Test
    public void searchAirPortThrowExceptionWithoutMap() throws Exception {
        List<AirPortDto> apDtos = new ArrayList<>();
        AirPortDto apDto = new AirPortDto();
        apDto.setIata("test");
        apDto.setId(425);
        Query<AirPortDto> query = new Query<>();
        query.setParams(apDto);
        when(luceneService.searchAirPort(query)).thenReturn(apDtos);
        mockMvc.perform(MockMvcRequestBuilders.post("/airports").contentType(
                    "application/vnd.henry" +
                            ".airport-search.v1+json").accept("application/vnd.henry.airport-search.v1+json")
                    .content(objectMapper.writeValueAsString(query))).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
