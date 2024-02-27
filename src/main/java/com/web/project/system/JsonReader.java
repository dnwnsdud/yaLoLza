package com.web.project.system;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.project.dto.TierDataDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class JsonReader {

    private static final Logger logger = LoggerFactory.getLogger(JsonReader.class);
    private final ObjectMapper objectMapper;

    public JsonReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TierDataDTO readJsonFile(String tierName) throws IOException {
        String path = "C:\\Users\\Administrator\\Desktop\\yaLoLza\\src\\main\\resources\\static\\json\\" + tierName + ".json";
        logger.info("읽어온 파일 위치 : {}", path);
        
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(path)));
            TierDataDTO data = objectMapper.readValue(jsonContent, TierDataDTO.class);
            logger.info("읽어온 티어 : {}", tierName);
            return data;
        } catch (IOException e) {
            logger.error("Error", e);
            throw e;
        }
    }
}

