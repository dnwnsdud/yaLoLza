package com.web.project.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.project.dto.championstats.CounterPositionDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class CounterJsonReader {
	@Value("${mainroot.json}")
	private String jsonroot;
	
    private static final Logger logger = LoggerFactory.getLogger(CounterJsonReader.class);
    private final ObjectMapper objectMapper;

    public CounterJsonReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CounterPositionDTO readCounterJsonFile() throws IOException {
        String path = jsonroot+"counter.json";
        logger.info("Reading Counter JSON file from: {}", path);
        
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(path)));
            CounterPositionDTO counterData = objectMapper.readValue(jsonContent, CounterPositionDTO.class);
            logger.info("Counter JSON file read successfully.");
            return counterData;
        } catch (IOException e) {
            logger.error("Error reading Counter JSON file", e);
            throw e;
        }
    }

}
