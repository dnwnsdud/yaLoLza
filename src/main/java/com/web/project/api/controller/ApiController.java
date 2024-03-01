package com.web.project.api.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
	
	@GetMapping("/resources/{year}/{month}/{day}/{file}")
	public ResponseEntity<Resource> resourcesAPI(
			@PathVariable("year")
			String year,
			@PathVariable("month")
			String month,
			@PathVariable("day")
			String day,
			@PathVariable("file")
			String file,
			@RequestParam("type")
			String type,
			@RequestParam(name = "w", required = false)
			Integer w,
			@RequestParam(name = "t", required = false)
			String t,
			@RequestParam(name = "d", required = false)
			String d
			) throws IOException{
		Path path = Paths.get("src/main/resources/static").toAbsolutePath();
		path = path.getParent().getParent().getParent().getParent().resolve("resources");
		String folder = "";
		String ext = file.substring(
						file.lastIndexOf('.'));
		String purefile = file.replace(ext, "");
		switch(type) {
		case "i": 
			if(w == null) folder = "images";
			else {
				if(w == 300) folder = "thumbnail_300";
				else if(w == 600) folder = "thumbnail_600";
				else folder = "images";
			}
			break;
		case "v":
			if(t == null) folder = "videos";
			else {
				if(t.equalsIgnoreCase("mp4")) { 
					folder = "thumbmp4";
					ext = ".mp4";
				}
				else if(t.equalsIgnoreCase("gif")) { 
					folder = "thumbgif";
					ext = ".gif";
				}
				else if(t.equalsIgnoreCase("jpg")) { 
					folder = "thumbjpg";
					ext = ".jpg";
				}
				else folder = "videos";
			}
		}
		path = path
			.resolve(folder)
			.resolve(year)
			.resolve(month)
			.resolve(day)
			.resolve(purefile + ext);
		
		HttpHeaders headers = new HttpHeaders();
		if(d != null) {
			headers.setContentDisposition(
				ContentDisposition.builder("attatchment")
					.filename(purefile + ext).build()
			);
		}
		
		return ResponseEntity
				.ok()
				.contentType(
					MediaType.parseMediaType(
						URLConnection.guessContentTypeFromName(path.toString())
					)
				)
				.headers(headers)
				.body(new InputStreamResource(
						Files.newInputStream(path)
						));
	}
}

