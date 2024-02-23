package com.web.project.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegBuilder.Strict;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.Dithering;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;

@Service
public final class MediaFiles {
	
	@Value("${media.download.root}")
	static String rootFolder;
	@Value("${media.download.video.original}")
	static String videoOriginalFolder;
	@Value("${media.download.image.original}")
	static String imageOriginalFolder;
	@Value("${media.download.randomNameLength}")
	static Integer randomNameLength;
	@Value("${media.download.video.gif.frame.rates}")
	static Integer videoGifFrameRates;
	@Value("${media.download.video.thumb.duration}")
	static Long videoThumbDuration;
	@Value("${media.download.video.thumb.offset}")
	static Long videoThumbOffset;
	@Value("${media.download.video.gif.width}")
	static Integer videoGifWidth;
	@Value("${media.download.video.mp4.width}")
	static Integer videoMp4Width;
	@Value("${media.download.video.jpg.width}")
	static Integer videoJpgWidth;
	@Value("${media.download.video.ffmpeg.path}")
	static String videoFFmpegPath;
	@Value("${media.download.video.ffprobe.path}")
	static String videoFFprobePath;
	@Value("${media.download.image.big.width}")
	static Integer imageBigWidth;
	@Value("${media.download.image.medium.width}")
	static Integer imageMediumWidth;
	@Value("${media.download.image.small.width}")
	static Integer imageSmallWidth;
	
	public enum VideoThumbnail { mp4, jpg, gif }
	public enum ImageThumbnail { big, medium, small }
	
	private MediaFiles() {}
	public static Path getRootPath() {
		Path path = Paths.get("src/main/resources/static").toAbsolutePath();
		return path
				.getParent().getParent().getParent().getParent();
	}
	
	public static double getDuration(String path) throws IOException, InterruptedException {
		ProcessBuilder builder = 
			new ProcessBuilder(
				"ffprobe",
				"-v",
				"error",
				"-show_entries",
				"format=duration",
				"-of",
				"default=noprint_wrappers=1:nokey=1",
				path
			);
		Process process = builder.start();
		BufferedReader reader = 
			new BufferedReader(
				new InputStreamReader(
					process.getInputStream()
				)
			);
		String line;
		double duration = 0;
		while((line = reader.readLine()) != null) {
			duration = Double.parseDouble(line);
		}
		process.waitFor();
		process.destroy();
		return duration;
	}
	
	public static List<Path> MediaDownloads(
			List<MultipartFile> files, 
			Date date, boolean isVideo) throws IOException {
		Path path = getRootPath()
				.resolve(rootFolder)
				.resolve(isVideo ? videoOriginalFolder : imageOriginalFolder)
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		if(!Files.exists(path)) Files.createDirectories(path);
		
		List<Path> filepaths = new LinkedList<Path>();
		for(MultipartFile file : files) {
			String ext = file
				.getOriginalFilename()
				.substring(file.getOriginalFilename().lastIndexOf('.'));
			Path real = Paths.get(path.toString(), 
					Rand.String(randomNameLength,c->
					(c >= 65 && c <= 90) ||
					(c >= 97 && c <= 122) || 
					(c >= 48 && c <= 57)) + ext);
			Files.copy(file.getInputStream(), real);
			filepaths.add(real);
		}
		
		return filepaths;
	}
	
	public static List<Path> ThumbnailFromVideo(
			List<Path> originalPath,
			Date date,
			VideoThumbnail type) throws IOException, InterruptedException {
		List<Path> paths = new LinkedList<Path>();
		Path path = getRootPath()
				.resolve(rootFolder)
				.resolve(type.name())
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		if(!Files.exists(path)) Files.createDirectories(path);
		for(Path filepath : originalPath) {
			String ext = filepath
					.getFileName().toString()
					.substring(
							filepath
							.getFileName()
							.toString()
							.lastIndexOf('.'));
			double duration = getDuration(filepath.toString());
			FFmpegOutputBuilder builder;
			builder =
					new FFmpegBuilder().setInput(filepath.toString())
					.addOutput(
							path.resolve(
									filepath
									.toString()
									.replace(ext, "." + type.name())
									).toString());
			paths.add(path.resolve(
					filepath
					.toString()
					.replace(ext, "." + type.name())
					));
			if(type == VideoThumbnail.jpg || type == VideoThumbnail.gif)
					builder = builder
								.disableAudio()
								.disableSubtitle();
			else
					builder = builder
								.disableSubtitle();
			if(type == VideoThumbnail.jpg)
					builder = builder
								.setFrames(1);
			else if(type == VideoThumbnail.gif)
					builder = builder
								.setFrames(
									duration * (videoThumbDuration / 100.0) * videoGifFrameRates > 0 ?
									(int)(duration * (videoThumbDuration / 100.0) * videoGifFrameRates) :
									1
								)
								.setVideoFrameRate(videoGifFrameRates);
			else
					builder = builder
								.setDuration(
									(long)(duration * (videoThumbDuration * 10)), 
									TimeUnit.MILLISECONDS);

					builder = builder
								.setStartOffset(
									(long)(duration * (videoThumbOffset * 10)),
									TimeUnit.MILLISECONDS)
								.setVideoWidth(
									type == VideoThumbnail.jpg ?
										videoJpgWidth :
									type == VideoThumbnail.gif ?
										videoGifWidth :
										videoMp4Width
								)
								.setStrict(Strict.EXPERIMENTAL);
			
			new Thread(
			new FFmpegExecutor(
					new FFmpeg(videoFFmpegPath),
					new FFprobe(videoFFprobePath))
					.createJob(builder.done())).start();
		}
		return paths;
	}
	
	
	public static List<Path> ThumbnailFromImage(
			List<Path> originalPath,
			Date date,
			ImageThumbnail type,
			boolean isJpg) throws IOException{
		List<Path> paths = new LinkedList<Path>();
		Path path = getRootPath()
				.resolve(rootFolder)
				.resolve(type.name())
				.resolve(Integer.toString(date.getYear() + 1900))
				.resolve(Integer.toString(date.getMonth() + 1))
				.resolve(Integer.toString(date.getDate()));
		if(!Files.exists(path)) Files.createDirectories(path);
		for(Path filepath : originalPath) {
			String ext = filepath
					.getFileName().toString()
					.substring(
							filepath
							.getFileName()
							.toString()
							.lastIndexOf('.'));
			paths.add(path.resolve(
					isJpg ?
						filepath.getFileName().toString().replace(ext, ".jpg") :
						filepath.getFileName().toString()));
			
			new Thread(()->{
			try {
				Thumbnails
					.of(filepath.toFile())
					.alphaInterpolation(AlphaInterpolation.QUALITY)
					.antialiasing(Antialiasing.ON)
					.dithering(Dithering.DISABLE)
					.width(
						type == ImageThumbnail.small ?
							imageSmallWidth :
						type == ImageThumbnail.medium ?
							imageMediumWidth :
							imageBigWidth
					)
					.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
					.outputQuality(0.85)
					.toFile(path.resolve(
						isJpg ?
							filepath.getFileName().toString().replace(ext, ".jpg") :
							filepath.getFileName().toString()
					).toFile());
			} catch (IOException e) {
				e.printStackTrace();
			}}).start();
		}
		return paths;
	}
}

















