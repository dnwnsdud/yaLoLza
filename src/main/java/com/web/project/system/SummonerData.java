package com.web.project.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.web.project.dto.info.Champion.Spell;

public class SummonerData {
	public final static Spell findspell(String id) {
		String url5 = "https://ddragon.leagueoflegends.com/cdn/14.2.1/data/ko_KR/summoner.json";
		try {
		      Integer.parseInt(id);
		      Map<String, String> keys1 = keysSumSpell();
		      id = keys1.get(id);
		    } catch (NumberFormatException ex) {}
		JSONObject sum3 = Ajax.JsonTObj(Ajax.GETO(url5)).getJSONObject("data").getJSONObject(id);
		Spell spell = new Spell();
		spell.setId(sum3.getString("id"));
		spell.setName(sum3.getString("name"));
		spell.setDescription(sum3.getString("description"));
		spell.setKey(sum3.getString("key"));
		return spell;
	}
	
	public static Map<String, String> keysSumSpell () {
		String url5 = "https://ddragon.leagueoflegends.com/cdn/14.2.1/data/ko_KR/summoner.json";
		JSONObject sum2 = Ajax.JsonTObj(Ajax.GETO(url5)).getJSONObject("data");
		Map<String, String> keysSumSpell= new HashMap<String, String>();
		List<String> keylist = new ArrayList<String>(sum2.keySet());
		Collections.sort(keylist);
		for(int i = 0; i < sum2.length(); i+=1) {
			JSONObject sum3 = sum2.getJSONObject(keylist.get(i));
			keysSumSpell.put(sum3.getString("key"), sum3.getString("id"));
		}
//		System.out.println(keysSumSpell);
		return keysSumSpell;
	}
	
	
	public static Map<Long, String> keysSumSpellLong () {
		String url5 = "https://ddragon.leagueoflegends.com/cdn/14.2.1/data/ko_KR/summoner.json";
		JSONObject sum2 = Ajax.JsonTObj(Ajax.GETO(url5)).getJSONObject("data");
		Map<Long, String> keysSumSpell= new HashMap<Long, String>();
		List<String> keylist = new ArrayList<String>(sum2.keySet());
		Collections.sort(keylist);
		for(int i = 0; i < sum2.length(); i+=1) {
			JSONObject sum3 = sum2.getJSONObject(keylist.get(i));
			keysSumSpell.put(sum3.getLong("key"), sum3.getString("id"));
		}
//		System.out.println(keysSumSpell);
		return keysSumSpell;
	}
}
