package com.web.project.system;

import com.web.project.dto.championstats.AramChampionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.util.StreamUtils;

@Service
public class AramStatsJsonReader {
	@Value("${mainroot.json}")
	private String jsonroot;
	
    private static final Logger logger = LoggerFactory.getLogger(CounterJsonReader.class);
	
    private final ObjectMapper objectMapper;

    public AramStatsJsonReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AramChampionDTO[] readAramStatsJsonFile() throws IOException {
        String path = "static/json/aram_stats.json"; // JSON 파일 경로
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream()) {
            String jsonContent = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            return objectMapper.readValue(jsonContent, AramChampionDTO[].class);
        }
    }
} 