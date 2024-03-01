package com.web.project.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.web.project.dto.info.Items;
import com.web.project.dto.info.item.Gold;
import com.web.project.dto.info.item.Item;
import com.web.project.dto.info.item.ItemImage;



public class ItemData {
	public static Item item(String id) {
		String url3 = "https://ddragon.leagueoflegends.com/cdn/14.3.1/data/ko_KR/item.json";
		JSONObject ite = Ajax.JsonTObj(Ajax.GETO(url3)).getJSONObject("data").getJSONObject(id);
		Item item = new Item( );
		item.setId(id);
		item.setName(ite.getString("name"));
		item.setDescription(ite.getString("description")
									.replaceAll("<br>", "\n")
									.replaceAll("<[^<>]*>", ""));
		item.setPlaintext(ite.getString("plaintext"));
		JSONObject ite4 = ite.getJSONObject("image");
		ItemImage image = new ItemImage();
		image.setFull(ite4.getString("full"));
		image.setGroup(ite4.getString("group"));
		item.setImage(image);
		JSONObject ite5 = ite.getJSONObject("gold");
		Gold gold = new Gold();
		gold.setBase(ite5.getLong("base"));
		gold.setTotal(ite5.getLong("total"));
		gold.setSell(ite5.getLong("sell"));
		item.setGold(gold);
		return item;
	}
	public static Items items () {
		String url3 = "https://ddragon.leagueoflegends.com/cdn/14.3.1/data/ko_KR/item.json";
		JSONObject ite1 = Ajax.JsonTObj(Ajax.GETO(url3));
		System.out.println("itemjson");
		Items items = new Items();
		items.setType(ite1.getString("type"));
		items.setVersion(ite1.getString("version"));
		JSONObject ite2 = ite1.getJSONObject("data");
		List<String> keylist = new ArrayList<String>(ite2.keySet());
		Collections.sort(keylist);
		List<Item> data = new ArrayList<Item>();
		for(int i = 0; i < ite2.length(); i+=1) {
			Item item = ItemData.item(keylist.get(i));
			data.add(item);
		} 
		data =data.stream().collect(Collectors.toList());
		items.setData(data);
		return items;
	}
	
	public List<String> legendlist() {
		String url3 = "https://ddragon.leagueoflegends.com/cdn/14.3.1/data/ko_KR/item.json";
		JSONObject ite2 = Ajax.JsonTObj(Ajax.GETO(url3)).getJSONObject("data");
		List<String> keylist = new ArrayList<String>(ite2.keySet());
		Collections.sort(keylist);
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < ite2.length(); i+=1) {
			JSONObject ite3 = ite2.getJSONObject(keylist.get(i));
			if(!ite3.has("into")) list.add(keylist.get(i));
		}
		System.out.println(list);
		return list;
		
	}
}
