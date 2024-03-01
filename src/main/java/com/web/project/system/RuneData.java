package com.web.project.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.web.project.dto.info.Perk;
import com.web.project.dto.info.Perks;
import com.web.project.dto.info.Runes;
import com.web.project.dto.info.rune.*;

public class RuneData {
	public static Rune runedetail(Integer id) {
		  Map<Integer, Integer> keys1= new HashMap<Integer, Integer>();
	      keys1.put(8000, 2);
	      keys1.put(8100, 0);
	      keys1.put(8200, 4);
	      keys1.put(8300, 1);
	      keys1.put(8400, 3);
	      Map<Integer, String> keys2= new HashMap<Integer, String>();
	      keys2.put(8000, "공격 강화 및 지속적 피해");
	      keys2.put(8100, "강력한 피해와 빠른 접근");
	      keys2.put(8200, "스킬 및 광역 효과 강화");
	      keys2.put(8300, "다양한 방식의 전투 보조");
	      keys2.put(8400, "내구력 및 군중 제어");
	      
	      String url4 = "https://ddragon.leagueoflegends.com/cdn/14.3.1/data/ko_KR/runesReforged.json";
	      JSONArray ru1 = Ajax.JsonTArr(Ajax.GETO(url4));
	      Map<Integer,List<List<Integer>>> runesIdList = new HashMap<Integer,List<List<Integer>>>();
	      for (int i = 0; i < ru1.length(); i+=1) {
	         JSONObject ru2 = ru1.getJSONObject(i);
	         JSONArray ru3 = ru2.getJSONArray("slots");
	         List<List<Integer>> slotsIdList = new LinkedList<List<Integer>>();
	         for (int j = 0; j < ru3.length(); j+=1) {
	            JSONObject ru4 = ru3.getJSONObject(j);
	            List<Integer> runeIdList = new LinkedList<Integer>();
	            for (int k = 0; k < ru4.getJSONArray("runes").length(); k+=1) {
	               JSONObject ru5 = ru4.getJSONArray("runes").getJSONObject(k);
	               runeIdList.add(ru5.getInt("id"));
	            }
	            slotsIdList.add(runeIdList);
	         }
	         runesIdList.put(ru2.getInt("id"), slotsIdList);
	      }
	      Rune rune = new Rune();
	      if(runesIdList.containsKey(id)) {
	         rune.setId(id);
	         rune.setName(ru1.getJSONObject(keys1.get(id)).getString("name"));
	         rune.setLongDesc(keys2.get(id));
	      }
	      else {
	         int a = 0;int b = 0; int c = 0;
	         loop:
	         for(Integer in : new int [] {8000,8100,8200,8300,8400}) {
	            List<List<Integer>> slotsIdList = runesIdList.get(in);
	            for(int i = 0; i < slotsIdList.size(); i+=1) {
	               List<Integer> runeIdList = slotsIdList.get(i);
	               for (int j = 0; j < runeIdList.size(); j+=1) {
	                  Integer runeId = runeIdList.get(j);
	                  if(runeId.intValue() == id.intValue()) {
	                	 a = in; b = i;c = j;   
	                     break loop;
	                  }
	               }
	            }
	         }
	         JSONObject ru = ru1.getJSONObject(keys1.get(a)).getJSONArray("slots").getJSONObject(b).getJSONArray("runes").getJSONObject(c);
	         rune.setId(ru.getInt("id"));
	         rune.setName(ru.getString("name"));
	         rune.setLongDesc(ru.getString("longDesc"));
	    }
	    return rune;
	}
	public static Runes runes (Integer id) {
		Runes runes = new Runes();
		String url4 = "https://ddragon.leagueoflegends.com/cdn/14.3.1/data/ko_KR/runesReforged.json";
		JSONArray ru1 = Ajax.JsonTArr(Ajax.GETO(url4));
		JSONObject ru2 = new JSONObject();
		switch (id) {
			case 8000 :   //정밀
				ru2 = ru1.getJSONObject(2);
				runes.setLongDesc("공격 강화 및 지속적 피해");
				break;  
			case 8100 :    //지배
				ru2 = ru1.getJSONObject(0);
				runes.setLongDesc("강력한 피해와 빠른 접근");
			break;
			case 8200 :    //마법
				ru2 = ru1.getJSONObject(4);
				runes.setLongDesc("스킬 및 광역 효과 강화");
			break;
			case 8300 :    //영감
				ru2 = ru1.getJSONObject(1);
				runes.setLongDesc("다양한 방식의 전투 보조");
			break;
			case 8400 :    //결의
				ru2 = ru1.getJSONObject(3);
				runes.setLongDesc("내구력 및 군중 제어");
			break;
			default :
				break;
		}
		runes.setId(ru2.getInt("id"));
		runes.setKey(ru2.getString("key"));
		runes.setIcon(ru2.getString("icon"));
		runes.setName(ru2.getString("name"));
		List<Slots> slotslist = new ArrayList<Slots>();
		JSONArray ru3 = ru2.getJSONArray("slots");
		for (int j = 0; j < ru3.length(); j+=1) {
			JSONObject ru4 = ru3.getJSONObject(j);
			Slots slots = new Slots();
			List<Rune> runelist = new ArrayList<Rune>();
			for (int k = 0; k < ru4.getJSONArray("runes").length(); k+=1) {
				JSONObject ru5 = ru4.getJSONArray("runes").getJSONObject(k);
				Rune rune = new Rune();
				rune.setId(ru5.getInt("id"));
				rune.setKey(ru5.getString("key"));
				rune.setIcon(ru5.getString("icon"));
				rune.setName(ru5.getString("name"));
				rune.setLongDesc(ru5.getString("longDesc"));
				runelist.add(rune);
			}
			slots.setRunes(runelist);
			slotslist.add(slots);
		}
		runes.setSlots(slotslist);
		return runes;
	}
	public static List<Runes> runeslist () {
		List<Runes> runeslist = new ArrayList<Runes>();
		String url4 = "https://ddragon.leagueoflegends.com/cdn/14.3.1/data/ko_KR/runesReforged.json";
		JSONArray ru1 = Ajax.JsonTArr(Ajax.GETO(url4));
		for (int i = 0; i < ru1.length(); i+=1) {
			JSONObject ru2 = ru1.getJSONObject(i);
			Runes runes = new Runes();
			int runeid = ru2.getInt("id");
			runes.setId(runeid);
			runes.setKey(ru2.getString("key"));
			runes.setIcon(ru2.getString("icon"));
			runes.setName(ru2.getString("name"));
			switch (runeid) {
			case 8000 :   //정밀
				runes.setLongDesc("공격 강화 및 지속적 피해");
				break;  
			case 8100 :    //지배
				runes.setLongDesc("강력한 피해와 빠른 접근");
			break;
			case 8200 :    //마법
				runes.setLongDesc("스킬 및 광역 효과 강화");
			break;
			case 8300 :    //영감
				runes.setLongDesc("다양한 방식의 전투 보조");
			break;
			case 8400 :    //결의
				runes.setLongDesc("내구력 및 군중 제어");
			break;
			default :
				break;
		}
			
			List<Slots> slotslist = new ArrayList<Slots>();
			JSONArray ru3 = ru2.getJSONArray("slots");
			for (int j = 0; j < ru3.length(); j+=1) {
				JSONObject ru4 = ru3.getJSONObject(j);
				Slots slots = new Slots();
				List<Rune> runelist = new ArrayList<Rune>();
				for (int k = 0; k < ru4.getJSONArray("runes").length(); k+=1) {
					JSONObject ru5 = ru4.getJSONArray("runes").getJSONObject(k);
					Rune rune = new Rune();
					rune.setId(ru5.getInt("id"));
					rune.setKey(ru5.getString("key"));
					rune.setIcon(ru5.getString("icon"));
					rune.setName(ru5.getString("name"));
					rune.setLongDesc(ru5.getString("longDesc"));
					runelist.add(rune);
				}
				slots.setRunes(runelist);
				slotslist.add(slots);
			}
			runes.setSlots(slotslist);
			runeslist.add(runes);
		}
		return runeslist;
	}
	public static List<Perk> perklist () {
		List<Perk> perklist = new LinkedList<Perk> ();
//		Perk perk = new Perk();
//		perk.builder()
//					.id(5003).name("마법저항력")
//					.tooltip("마법 저항력 +8").build();
//		perk.builder()
//					.id(5002).name("방어력")
//					.tooltip("방어력 +6").build();
		perklist.add(
				Perk.builder()
				.id(5008).name("적응형 능력치").group("OFFENSE")
				.icon("perk-images/StatMods/StatModsAdaptiveForceIcon.png")
				.tooltip("적응형 능력치 +9").build()
				);
		perklist.add(
				Perk.builder()
				.id(5005).name("공격 속도").group("OFFENSE")
				.icon("perk-images/StatMods/StatModsAttackSpeedIcon.png")
				.tooltip("공격 속도 +10%").build()
				);
		perklist.add(
				Perk.builder()
				.id(5007).name("스킬 가속").group("OFFENSE")
				.icon("perk-images/StatMods/StatModsCDRScalingIcon.png")
				.tooltip("스킬 가속 +8").build()
				);
		perklist.add(
				Perk.builder()
				.id(5008).name("적응형 능력치").group("OFFENSE")
				.icon("perk-images/StatMods/StatModsAdaptiveForceIcon.png")
				.tooltip("적응형 능력치 +9").build()
				);
		perklist.add(
				Perk.builder()
				.id(5010).name("이동 속도").group("FLEX")
				.icon("perk-images/StatMods/StatModsMovementSpeedIcon.png")
				.tooltip("이동 속도 +2%").build()
				);
		perklist.add(
				Perk.builder()
				.id(5001).name("체력 증가").group("DEFENSE")
				.icon("perk-images/StatMods/StatModsHealthPlusIcon.png")
				.tooltip("체력 +15~140 (레벨에 비례)").build()
				);
		perklist.add(
				Perk.builder()
				.id(5011).name("체력").group("DEFENSE")
				.icon("perk-images/StatMods/StatModsHealthScalingIcon.png")
				.tooltip("체력 +65").build()
				);
		perklist.add(
				Perk.builder()
					.id(5013).name("강인함 및 둔화 저항").group("DEFENSE")
					.icon("perk-images/StatMods/StatModsTenacityIcon.png")
					.tooltip("강인함 및 둔화 저항 +10%").build()
		);
		perklist.add(
				Perk.builder()
					.id(5001).name("체력 증가").group("DEFENSE")
					.icon("perk-images/StatMods/StatModsHealthPlusIcon.png")
					.tooltip("체력 +15~140 (레벨에 비례)").build()
				);
		return perklist;
	}
	
	public static Perk perk(Integer id) {
		Perk perk = new Perk();
		List<Perk> perklist = RuneData.perklist();
		for(int i = 0; i < perklist.size(); i+=1) {
			perk = perklist.get(i);
			if(perk.getId().intValue() == id.intValue()) break;
		}
		return perk;
		
	}
	
	public static Map<Integer, String> keysRune () {
		String url4 = "https://ddragon.leagueoflegends.com/cdn/14.3.1/data/ko_KR/runesReforged.json";
		JSONArray ru1 = Ajax.JsonTArr(Ajax.GETO(url4));
		Map<Integer, String> keysRune= new HashMap<Integer, String>();
		for (int i = 0; i < ru1.length(); i+=1) {
			JSONObject ru2 = ru1.getJSONObject(i);
			keysRune.put(ru2.getInt("id"),ru2.getString("key"));
			for (int j = 0; j < ru2.getJSONArray("slots").length(); j+=1) {
				JSONObject ru4 = ru2.getJSONArray("slots").getJSONObject(j);
				for (int k = 0; k < ru4.getJSONArray("runes").length(); k+=1) {
					JSONObject ru5 = ru4.getJSONArray("runes").getJSONObject(k);
					keysRune.put(ru5.getInt("id"),ru5.getString("key"));
				}
			}
		}
		return keysRune;
	}
	public static Map<Long, String> keysRuneImage () {
		String url4 = "https://ddragon.leagueoflegends.com/cdn/14.3.1/data/ko_KR/runesReforged.json";
		JSONArray ru1 = Ajax.JsonTArr(Ajax.GETO(url4));
		Map<Long, String> keysRuneImage= new HashMap<Long, String>();
		for (int i = 0; i < ru1.length(); i+=1) {
			JSONObject ru2 = ru1.getJSONObject(i);
			keysRuneImage.put(ru2.getLong("id"),ru2.getString("icon"));
			for (int j = 0; j < ru2.getJSONArray("slots").length(); j+=1) {
				JSONObject ru4 = ru2.getJSONArray("slots").getJSONObject(j);
				for (int k = 0; k < ru4.getJSONArray("runes").length(); k+=1) {
					JSONObject ru5 = ru4.getJSONArray("runes").getJSONObject(k);
					keysRuneImage.put(ru5.getLong("id"),ru5.getString("icon"));
				}
			}
		}
		return keysRuneImage;
	}

	public static Map<Integer, String> keysPerk () {
		Map<Integer, String> keysPerk= new HashMap<Integer, String>();
		List<Perk> perklist = RuneData.perklist();
		for(int i = 0; i < perklist.size(); i+=1) {
			Perk perk = perklist.get(i);
			keysPerk.put(perk.getId(), perk.getIcon());
		}
		return keysPerk;
		
	}

	public static Perks perks () {
		Perks perks = new Perks();
		List<ArrayList<Perk>> slots = new ArrayList<ArrayList<Perk>>();
		ArrayList<Perk> slot1 = new ArrayList<Perk>();
		slot1.add(
				Perk.builder()
				.id(5008).name("적응형 능력치").group("OFFENSE")
				.icon("perk-images/StatMods/StatModsAdaptiveForceIcon.png")
				.tooltip("적응형 능력치 +9").build()
				);
		slot1.add(
				Perk.builder()
				.id(5005).name("공격 속도").group("OFFENSE")
				.icon("perk-images/StatMods/StatModsAttackSpeedIcon.png")
				.tooltip("공격 속도 +10%").build()
				);
		slot1.add(
				Perk.builder()
				.id(5007).name("스킬 가속").group("OFFENSE")
				.icon("perk-images/StatMods/StatModsCDRScalingIcon.png")
				.tooltip("스킬 가속 +8").build()
				);
		
		slots.add(slot1);
		ArrayList<Perk> slot2 = new ArrayList<Perk>();
		slot2.add(
				Perk.builder()
				.id(5008).name("적응형 능력치").group("OFFENSE")
				.icon("perk-images/StatMods/StatModsAdaptiveForceIcon.png")
				.tooltip("적응형 능력치 +9").build()
				);
		slot2.add(
				Perk.builder()
				.id(5010).name("이동 속도").group("FLEX")
				.icon("perk-images/StatMods/StatModsMovementSpeedIcon.png")
				.tooltip("이동 속도 +2%").build()
				);
		slot2.add(
				Perk.builder()
				.id(5001).name("체력 증가").group("DEFENSE")
				.icon("perk-images/StatMods/StatModsHealthPlusIcon.png")
				.tooltip("체력 +15~140 (레벨에 비례)").build()
				);
		slots.add(slot2);
		ArrayList<Perk> slot3 = new ArrayList<Perk>();
		slot3.add(
				Perk.builder()
				.id(5011).name("체력").group("DEFENSE")
				.icon("perk-images/StatMods/StatModsHealthScalingIcon.png")
				.tooltip("체력 +65").build()
				);
		slot3.add(
				Perk.builder()
					.id(5013).name("강인함 및 둔화 저항").group("DEFENSE")
					.icon("perk-images/StatMods/StatModsTenacityIcon.png")
					.tooltip("강인함 및 둔화 저항 +10%").build()
		);
		slot3.add(
				Perk.builder()
					.id(5001).name("체력 증가").group("DEFENSE")
					.icon("perk-images/StatMods/StatModsHealthPlusIcon.png")
					.tooltip("체력 +15~140 (레벨에 비례)").build()
				);
		
		slots.add(slot3);
		perks.setSlots(slots);
		return perks;
	} 

	public static Perk perkfind (Integer id) {
		Perk perk = new Perk();
		Perks perks = RuneData.perks();
		for (int i = 0; i < 3; i+=1) {
			ArrayList<Perk> perklots = perks.getSlots().get(i);
			
		}
		return perk;
	}
}
