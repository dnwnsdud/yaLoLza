package com.web.project.system;

import com.web.project.dto.championstats.PatchDTO;
import com.web.project.dto.championstats.PatchesChampionDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.Map;

@Service
public class PatchesChampionService {

    private final patchesJsonReader patchesJsonReader;

    @Autowired
    public PatchesChampionService(patchesJsonReader patchesJsonReader) {
        this.patchesJsonReader = patchesJsonReader;
    }

    public PatchesChampionDTO getChampionData(String patchNumber, String championName) {
        try {
            Map<String, PatchDTO> patchData = patchesJsonReader.readPatchData();
            PatchDTO patch = patchData.get(patchNumber);
            return patch.getChampions().get(championName);
        } catch (IOException e) {
            // 오류 처리
            return null;
        }
    }
    public Map<String, PatchDTO> getAllPatchesData() throws IOException {
        return patchesJsonReader.readPatchData();
    }
}
