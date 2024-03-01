package com.web.project.page.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.web.project.dto.SiteUser;
import com.web.project.system.Rand;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegBuilder.Strict;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.Dithering;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;

@Controller
public class PageController {
//	@GetMapping("/home")
//	public String home(@AuthenticationPrincipal SiteUser user, Model model) {
//		model.addAttribute("user", user);
//		return "home";
//	}
	
	@GetMapping("/home")
	public String home(@AuthenticationPrincipal SiteUser user, Model model) {
//		if(user !=null)
//			System.out.println(user.toString());
		model.addAttribute("user", user);
		return "home";
	}
	
	@GetMapping("/upload")
	public String Upload() {
		return "upload";
	}
	

	@PostMapping("/upload/videos")
	public String UploadVideos(
			@RequestParam(name = "title")
			String title,
			@RequestPart(name = "file")
			List<MultipartFile> files
			) throws IOException {
		Date date = new Date();
		Path path = Paths.get("src/main/resources/static").toAbsolutePath();
		Path thumbgif, thumbjpg, thumbmp4;
		path = path.getParent().getParent().getParent().getParent().resolve("resources");
		thumbgif = path
				.resolve("thumbgif")
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		thumbjpg = path
				.resolve("thumbjpg")
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		thumbmp4 = path
				.resolve("thumbmp4")
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		path = path
				.resolve("videos")
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		if(!Files.exists(path)) Files.createDirectories(path);
		if(!Files.exists(thumbgif)) Files.createDirectories(thumbgif);
		if(!Files.exists(thumbjpg)) Files.createDirectories(thumbjpg);
		if(!Files.exists(thumbmp4)) Files.createDirectories(thumbmp4);
		
		List<Path> filepaths = new LinkedList<Path>();
		for(MultipartFile file : files) {
			String ext = file
				.getOriginalFilename()
				.substring(file.getOriginalFilename().lastIndexOf('.'));
			Path real = Paths.get(path.toString(), 
					Rand.String(50,c->
					(c >= 65 && c <= 90) ||
					(c >= 97 && c <= 122) || 
					(c >= 48 && c <= 57)) + ext);
			Files.copy(file.getInputStream(), real);
			filepaths.add(real);
		}
		
		
		for(Path filepath : filepaths) {
			String ext = filepath
					.getFileName().toString()
					.substring(
							filepath
							.getFileName()
							.toString()
							.lastIndexOf('.'));
			FFmpegBuilder builder;
			builder =
					new FFmpegBuilder()
					// 파일 지정
					.setInput(filepath.toString())
					// 뽑아낼 파일 위치
					.addOutput(thumbjpg.resolve(
									filepath.getFileName().toString()
									).toString().replace(ext, ".jpg"))
					.disableAudio()
					.disableSubtitle()
					// 뽑아낼 프레임 개수
					.setFrames(1)
					// 프레임 시작 위치
					.setStartOffset(30, TimeUnit.SECONDS)
//						.setVideoResolution(300, 500)
//						.setVideoResolution("300x600")
					.setVideoWidth(300)
					.setStrict(Strict.EXPERIMENTAL)
					// 다 끝내고 생성
					.done();
			
			new FFmpegExecutor(
					new FFmpeg("exec/ffmpeg/ffmpeg.exe"),
					new FFprobe("exec/ffmpeg/ffprobe.exe"))
					.createJob(builder).run();

			builder =
					new FFmpegBuilder()
					.setInput(filepath.toString())
					.addOutput(thumbgif.resolve(
									filepath.getFileName().toString()
									).toString().replace(ext, ".gif"))
					.disableAudio()
					.disableSubtitle()
					.setFrames(60)
					.setVideoFrameRate(15)
					.setStartOffset(30, TimeUnit.SECONDS)
					.setVideoWidth(300)
					.setStrict(Strict.EXPERIMENTAL)
					.done();
			
			new FFmpegExecutor(
					new FFmpeg("exec/ffmpeg/ffmpeg.exe"),
					new FFprobe("exec/ffmpeg/ffprobe.exe"))
					.createJob(builder).run();

			builder =
					new FFmpegBuilder()
					.setInput(filepath.toString())
					.addOutput(thumbmp4.resolve(
									filepath.getFileName().toString()
									).toString().replace(ext, ".mp4"))
					.disableSubtitle()
					.setVideoFrameRate(60)
					// 전송 속도
					// .setAudioBitRate(0).setVideoBitRate(0)
					// .setVideoCodec(codec).setAudioCodec(codec)
					.setDuration(30, TimeUnit.SECONDS)
					.setStartOffset(30, TimeUnit.SECONDS)
					.setVideoWidth(300)
					.setStrict(Strict.EXPERIMENTAL)
					.done();
			
			new FFmpegExecutor(
					new FFmpeg("exec/ffmpeg/ffmpeg.exe"),
					new FFprobe("exec/ffmpeg/ffprobe.exe"))
					.createJob(builder).run();
		}
		return "redirect:/home";
	}
	
	@PostMapping("/upload/images")
	public String UploadImages(
			@RequestParam(name = "title")
			String title,
			@RequestPart(name = "file")
			List<MultipartFile> files
			) throws IOException {
		Date date = new Date();
		Path path = Paths.get("src/main/resources/static").toAbsolutePath();
		Path thumbpath300, thumbpath600;
		path = path.getParent().getParent().getParent().getParent().resolve("resources");
		thumbpath300 = path
				.resolve("thumbnail_300")
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		thumbpath600 = path
				.resolve("thumbnail_600")
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		path = path
				.resolve("images")
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		if(!Files.exists(path)) Files.createDirectories(path);
		if(!Files.exists(thumbpath300)) Files.createDirectories(thumbpath300);
		if(!Files.exists(thumbpath600)) Files.createDirectories(thumbpath600);
		
		List<Path> filepaths = new LinkedList<Path>();
		for(MultipartFile file : files) {
			String ext = file
				.getOriginalFilename()
				.substring(file.getOriginalFilename().lastIndexOf('.'));
			Path real = Paths.get(path.toString(), 
					Rand.String(50,c->
					(c >= 65 && c <= 90) ||
					(c >= 97 && c <= 122) || 
					(c >= 48 && c <= 57)) + ext);
			Files.copy(file.getInputStream(), real);
			filepaths.add(real);
		}
		
		for(Path filepath : filepaths) {
			Thumbnails
				.of(filepath.toFile())
				// 우선시 하는 것
				.alphaInterpolation(AlphaInterpolation.QUALITY)
				// 각지는 것 여부
				.antialiasing(Antialiasing.ON)
				// 색상 제한
				.dithering(Dithering.DISABLE)
				// 비율 유지 여부
				// .keepAspectRatio(true)
				// 배율
				// .scale(1.5)
				// 정확한 치수 -> 비율 우선
				// .size(300, 500)
				// 정확한 치수 -> 치수 우선
				// .forceSize(300, 500)
				// 치수 -> 한쪽 치수 -> 나머지는 자동
				.width(600)
				// .height(0)
				// 잘라내기할 이미지 위치
				// .sourceRegion(Positions.CENTER)
				// 이미지 변형 모드
				.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
				// 뽑아낼 확장자
				// .outputFormat("jpg")
				.outputQuality(0.85)
				//.toFiles(Rename.)
				.toFiles(thumbpath600.toFile(), Rename.NO_CHANGE);
			Thumbnails
				.of(filepath.toFile())
				.alphaInterpolation(AlphaInterpolation.QUALITY)
				.antialiasing(Antialiasing.ON)
				.dithering(Dithering.DISABLE)
				.width(300)
				.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
				.outputQuality(0.85)
				.toFiles(thumbpath300.toFile(), Rename.NO_CHANGE);
		}
		
		return "redirect:/home";
	}
}
