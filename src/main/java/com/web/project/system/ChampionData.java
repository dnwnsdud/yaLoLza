package com.web.project.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.web.project.dto.Champion.Champion;
import com.web.project.dto.Champion.ChampionImage;
import com.web.project.dto.Champion.Passive;
import com.web.project.dto.Champion.PassiveImage;
import com.web.project.dto.Champion.Skin;
import com.web.project.dto.Champion.Spell;
import com.web.project.dto.Champion.Stat;

public class ChampionData {
	public static List<Champion> data() {
		String url1 = "https://ddragon.leagueoflegends.com/cdn/14.2.1/data/ko_KR/champion.json";
		JSONObject cham1 = Ajax.JsonTObj(Ajax.GETO(url1));
		List<Champion> data = new ArrayList<Champion>();
		JSONObject cham2 = cham1.getJSONObject("data");
		List<String> keylist = new ArrayList<String>(cham2.keySet());
		Collections.sort(keylist);
		for(int i = 0; i < cham2.length(); i+=1) {
			Champion champion = championinfo(keylist.get(i));
			data.add(champion);
		}
		return data;
	}
	
	public static Champion championinfo(String id) {
		String url2 = "https://ddragon.leagueoflegends.com/cdn/14.3.1/data/ko_KR/champion/";
		JSONObject cham3 = Ajax.JsonTObj(Ajax.GETO(url2+id+".json"))
				.getJSONObject("data")
				.getJSONObject(id);   
		Champion champion = new Champion();
		champion.setId(cham3.getString("id"));
		champion.setKey(cham3.getString("key"));
		champion.setName(cham3.getString("name"));  
		JSONObject cham9 = cham3.getJSONObject("image");
		ChampionImage img = new ChampionImage();
		img.setFull(cham9.getString("full"));
		img.setGroup(cham9.getString("group"));
		champion.setImage(img);
		
		JSONArray cham11 = cham3.getJSONArray("tags");
		List<String> tags = new ArrayList<String>();
		for(int l = 0; l < cham11.length(); l+=1) {
			tags.add(cham11.getString(l)
					.replace("Fighter", "전사")
					.replace("Tank", "탱커")
					.replace("Mage", "마법사")
					.replace("Assassin", "암살자")
					.replace("Marksman", "원거리")
					.replace("Support", "서포터"));
		}
		champion.setTags(tags);

		champion.setPartype(cham3.getString("partype"));
		JSONObject cham12 = cham3.getJSONObject("stats");
		Stat stats = new Stat();
		stats.setHp(cham12.getLong("hp"));
		stats.setHpperlevel(cham12.getLong("hpperlevel"));
		stats.setMp(cham12.getLong("mp"));
		stats.setMpperlevel(cham12.getLong("mpperlevel"));
		stats.setMovespeed(cham12.getLong("movespeed"));
		stats.setArmor(cham12.getLong("armor"));
		stats.setArmorperlevel(cham12.getDouble("armorperlevel"));
		stats.setSpellblock(cham12.getLong("spellblock"));
		stats.setSpellblockperlevel(cham12.getDouble("spellblockperlevel"));
		stats.setAttackrange(cham12.getLong("attackrange"));
		stats.setHpregen(cham12.getDouble("hpregen"));
		stats.setHpregenperlevel(cham12.getDouble("hpregenperlevel"));
		stats.setMpregen(cham12.getDouble("mpregen"));
		stats.setMpregenperlevel(cham12.getDouble("mpregenperlevel"));
		stats.setCrit(cham12.getLong("crit"));
		stats.setCritperlevel(cham12.getLong("critperlevel"));
		stats.setAttackdamage(cham12.getLong("attackdamage"));
		stats.setAttackdamageperlevel(cham12.getDouble("attackdamageperlevel"));
		stats.setAttackspeed(cham12.getDouble("attackspeed"));
		stats.setAttackspeedperlevel(cham12.getDouble("attackspeedperlevel"));
		champion.setStats(stats);
		JSONArray cham5 = cham3.getJSONArray("skins");
		List<Skin> skins = new ArrayList<Skin>();
		for(int j = 0; j < cham5.length(); j+=1) {
			JSONObject cham6 = cham5.getJSONObject(j);
			Skin skin = new Skin();
			skin.setId(cham6.getString("id"));
			skin.setNum(cham6.getInt("num"));
			skin.setName(cham6.getString("name"));
			skin.setChromas(cham6.getBoolean("chromas"));
			skins.add(skin);
		}
		champion.setSkins(skins);
		JSONArray cham6 = cham3.getJSONArray("spells");
		List<Spell> spells = new ArrayList<Spell>();
		for(int j = 0; j < cham6.length(); j+=1) {
			JSONObject cham7 = cham6.getJSONObject(j);
			Spell spell = new Spell();
			spell.setId(cham7.getString("id"));
			spell.setName(cham7.getString("name"));
			spell.setDescription(cham7.getString("description"));
			String tool = cham7.getString("tooltip");
			tool = tool.replaceAll("\\{\\{[^\\{\\}\\<\\>]*\\}\\}", "?");
			tool = tool.replaceAll("<br /><br />", "\n");
			tool = tool.replaceAll("<[^<>]*>", "");
			spell.setTooltip(tool);
			spell.setCooldownBurn(cham7.getString("cooldownBurn"));
			if(cham7.getString("costBurn").equalsIgnoreCase("0"))
				spell.setCostBurn("소모값 없음");
			else spell.setCostBurn(cham7.getString("costBurn"));
			spell.setRangeBurn(cham7.getString("rangeBurn"));
			spells.add(spell);
		}
		champion.setSpells(spells);
		JSONObject cham8 = cham3.getJSONObject("passive");
		Passive pass = new Passive();
		pass.setName(cham8.getString("name"));
		pass.setDescription(cham8.getString("description").replaceAll("<[^<>]*>", ""));
		JSONObject cham10 = cham8.getJSONObject("image");
		PassiveImage img2 = new PassiveImage();
		img2.setFull(cham10.getString("full"));
		img2.setGroup(cham10.getString("group"));
		pass.setImage(img2);
		champion.setPassive(pass);
		return champion;
	}
	public static List<Champion> imagedata() {
		String url1 = "https://ddragon.leagueoflegends.com/cdn/14.2.1/data/ko_KR/champion.json";
		List<Champion> data = new ArrayList<Champion>();
		JSONObject cham2 = Ajax.JsonTObj(Ajax.GETO(url1)).getJSONObject("data");
		List<String> keylist = new ArrayList<String>(cham2.keySet());
		Collections.sort(keylist);
		Map<String,String> keymap = new HashMap<String, String>();
		for(int i = 0; i < cham2.length(); i+=1) {
			JSONObject cham3 = cham2.getJSONObject(keylist.get(i));
			keymap.put(keylist.get(i), cham3.getString("name"));
		}
		keylist.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return keymap.get(o1).compareTo(keymap.get(o2));
            }
        });
		for(int i = 0; i < cham2.length(); i+=1) {
			JSONObject cham3 = cham2.getJSONObject(keylist.get(i));
			Champion champion = new Champion();
			champion.setId(keylist.get(i));
			champion.setName(cham3.getString("name"));
			keymap.put(keylist.get(i), cham3.getString("name"));
			JSONObject cham9 = cham3.getJSONObject("image");
			ChampionImage img = new ChampionImage();
			img.setFull(cham9.getString("full"));
			img.setGroup(cham9.getString("group"));
			champion.setImage(img);
			data.add(champion);
		}
		return data;
	}
	public static Map<String, String> keysChamName () {
		String url1 = "https://ddragon.leagueoflegends.com/cdn/14.2.1/data/ko_KR/champion.json";
		JSONObject cham2 = Ajax.JsonTObj(Ajax.GETO(url1)).getJSONObject("data");
		List<String> keylist = new ArrayList<String>(cham2.keySet());
		Collections.sort(keylist);
		Map<String,String> keysChamName = new HashMap<String, String>();
		for(int i = 0; i < cham2.length(); i+=1) {
			JSONObject cham3 = cham2.getJSONObject(keylist.get(i));
			keysChamName.put(keylist.get(i), cham3.getString("name"));
		}
		return keysChamName;
	}
}
