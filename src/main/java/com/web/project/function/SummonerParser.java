package com.web.project.function;

import java.util.HashMap;
import java.util.Map;

public class SummonerParser {

    public static Map<String, String> parseSummonerNameAndTag(String name) {
        Map<String, String> result = new HashMap<>();

        String[] nameParts = name.split("#");

        if (nameParts.length == 2) {
            String summonername = nameParts[0];
            String tag = nameParts[1];

            result.put("summonername", summonername);
            result.put("tag", tag);
        } else {
        	result.put("summonername", name);
        	result.put("tag", "#KR1");
        }

        return result;
    }
}