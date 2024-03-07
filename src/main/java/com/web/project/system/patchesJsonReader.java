package com.web.project.system;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.project.dto.championstats.PatchDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class patchesJsonReader {
	@Value("${mainroot.json}")
	private String jsonroot;
	
    private static final Logger logger = LoggerFactory.getLogger(patchesJsonReader.class);
    private final ObjectMapper objectMapper;

    public patchesJsonReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, PatchDTO> readPatchData() throws IOException {
        String path = "static/json/patcheschampion.json";
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            String jsonContent = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            Map<String, Map<String, PatchDTO>> allData = 
                objectMapper.readValue(jsonContent, new TypeReference<Map<String, Map<String, PatchDTO>>>() {});

            return allData.get("patch");
        } catch (IOException e) {
            throw e;
        }
    }
}
