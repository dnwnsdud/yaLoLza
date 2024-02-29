package com.web.project.page.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.project.dto.championstats.ChampionStatsDTO;
import com.web.project.dto.championstats.TierDataDTO;
import com.web.project.system.JsonReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class StaticController {

    private final JsonReader jsonReader;

    @Autowired
    public StaticController(JsonReader jsonReader) {
        this.jsonReader = jsonReader;
    }

    @GetMapping("/static")
    public String getChampionsData(@RequestParam(required = false, defaultValue = "ALL") String tier,
                                   @RequestParam(required = false, defaultValue = "ALL") String position, Model model) {
        try {
            List<ChampionStatsDTO> allChampionsData = new ArrayList<>();

            if ("ALL".equals(tier)) {
                for (String eachTier : List.of("IRON", "BRONZE", "SILVER", "GOLD", "PLATINUM", "DIAMOND", "MASTER", "GRANDMASTER", "CHALLENGER")) {
                    TierDataDTO
                    tierData = jsonReader.readJsonFile(eachTier);
                    if ("ALL".equals(position)) {
                        tierData.getPositions().values().forEach(allChampionsData::addAll);
                    } else {
                        allChampionsData.addAll(tierData.getPositions().getOrDefault(position, new ArrayList<>()));
                    }
                }
            } else {
                TierDataDTO tierData = jsonReader.readJsonFile(tier);
                if ("ALL".equals(position)) {
                    tierData.getPositions().values().forEach(allChampionsData::addAll);
                } else {
                    allChampionsData.addAll(tierData.getPositions().getOrDefault(position, new ArrayList<>()));
                }
            }

            allChampionsData.sort((champ1, champ2) -> Double.compare(champ2.getStats().getTierScore(),
                    champ1.getStats().getTierScore()));

            Set<String> addedChampions = new HashSet<>();
            List<ChampionStatsDTO> uniqueChampions = new ArrayList<>();
            for (ChampionStatsDTO champion : allChampionsData) {
                if (addedChampions.add(champion.getChampionName())) {
                    uniqueChampions.add(champion);
                }
            }

            model.addAttribute("positionData", uniqueChampions);
        } catch (Exception e) {
            model.addAttribute("error", "ERROR: " + e.getMessage());
        }
        return "static";
    }
}