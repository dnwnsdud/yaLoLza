package com.web.project.dto.summoner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class SummonerIdListResponse {
    private String[] summonerIds;
}