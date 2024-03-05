package com.web.project.api.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class YoutubeService {

	@Value("${youtube.api.key}")
	private String apiKey;

	public String youtubeGenerator(String champion) {

		String query = "장인 " + champion;

		String apiUrl = "https://www.googleapis.com/youtube/v3/search";
		String part = "snippet";
		String type = "video";
		int maxResults = 12;
		String order = "viewCount";
		LocalDateTime MonthsAgo = LocalDateTime.now().minusMonths(12);
		DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String publishedAfter = MonthsAgo.format(datetime);
		String relevanceLanguage = "ko";
		String regionCode = "KR";
		String safeSearch = "none";
		String videoCategoryId = "20";
		String videoDuration = "medium";

		String url = apiUrl + "?key=" + apiKey + "&q=" + query + "&part=" + part + "&type=" + type + "&maxResults="
				+ maxResults + "&order=" + order + "&publishedAfter=" + publishedAfter + "&relevanceLanguage="
				+ relevanceLanguage + "&regionCode=" + regionCode + "&safeSearch=" + safeSearch + "&videoCategoryId="
				+ videoCategoryId + "&videoDuration=" + videoDuration;

		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(url, String.class);

		JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
		JsonArray items = jsonResponse.getAsJsonArray("items");

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			JsonObject video = items.get(i).getAsJsonObject();
			JsonObject id = video.getAsJsonObject("id");
			String videoId = id.get("videoId").getAsString();

			JsonObject snippet = video.getAsJsonObject("snippet");
			String title = snippet.get("title").getAsString();

			String iframeCode = "<iframe width=\"445\" height=\"250\" " + "src=\"https://www.youtube.com/embed/"
					+ videoId + "\" " + "title=\"" + title + "\" frameborder=\"0\" "
					+ "allow=\"accelerometer; picture-in-picture;\" "
					+ "allowfullscreen></iframe>";

			result.append(iframeCode).append("\n");
		}

		return result.toString();
	}
}