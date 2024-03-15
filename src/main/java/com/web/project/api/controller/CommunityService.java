package com.web.project.api.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.project.Exception.DataNotFoundException;
import com.web.project.dao.CommunityRepository;
import com.web.project.dto.Community;
import com.web.project.dto.SiteUser;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;



    public Page<Community> getList(int page, String category, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 15, Sort.by(sorts));

        if ("전체".equals(category)) {
            return this.communityRepository.findAllNotQnA(keyword, pageable);
        } else if ("인기".equals(category)) {
            return communityRepository.findallbest(pageable);

        } else {
            return this.communityRepository.findAllByCategory(pageable, category, keyword);
        }
    }
    public Community getCommu(Integer id) {
        Optional<Community> community = this.communityRepository.findById(id);

        if(community.isPresent()) {
            Community commu = community.get();
            commu.setWriteview(commu.getWriteview()+1);
            this.communityRepository.save(commu);
            return commu;
        }
        else {
            throw new DataNotFoundException("데이터가 없습니다");
        }

    }


    @Transactional
    public void create(String title, String content, String category, List<MultipartFile> files, SiteUser user)
            throws IOException {
        Community c = new Community();
        List<String> filePaths = new ArrayList<>();
        for(MultipartFile file: files){
            if(!file.isEmpty()) {
                //저장 경로 지정
                String projectPath;

                //랜덤 이름 만들어줌
                UUID uuid = UUID.randomUUID();

                /*
                 * //저장될 파일 이름 지정 String fileName = uuid + "_" + file.getOriginalFilename();
                 */
                String fileName;
                String fileadd;
                if(file.getOriginalFilename().indexOf(".mp4") != -1) {
                    fileName = uuid + "_" + ".mp4";
                    fileadd = "mp4/";
                    projectPath = System.getProperty("user.dir") + "/src/main/resources/files/mp4";
                } else if(file.getOriginalFilename().contains(".jpg")) {
                    fileName = uuid + "_" + ".jpg";
                    fileadd = "img/";
                    projectPath = System.getProperty("user.dir") + "/src/main/resources/files/img";
                }else if(file.getOriginalFilename().contains(".PNG")) {
                    fileName = uuid + "_" + ".PNG";
                    fileadd = "img/";
                    projectPath = System.getProperty("user.dir") + "/src/main/resources/files/img";
                }else if(file.getOriginalFilename().contains(".jpeg")) {
                    fileName = uuid + "_" + ".jpeg";
                    fileadd = "img/";
                    projectPath = System.getProperty("user.dir") + "/src/main/resources/files/img";
                }else if(file.getOriginalFilename().contains(".gif")) {
                    fileName = uuid + "_" + ".gif";
                    fileadd = "img/";
                    projectPath = System.getProperty("user.dir") + "/src/main/resources/files/img";
                } else{
                    return;
                }


                File saveFile = new File(projectPath, fileName);

                file.transferTo(saveFile);

                c.setFilename(fileName);
                filePaths.add(fileadd + fileName);
            }
        }

        c.setTitle(title);
        c.setContent(content);

        c.setCreateDate(LocalDateTime.now());
        c.setCategory(category);
        c.setSiteUser(user);



        c.setFilePath(filePaths);
        this.communityRepository.save(c);

    }


    @Transactional
    public void modify(Community community, String title, String content, List<MultipartFile> files) throws IOException {
        community.setTitle(title);
        community.setContent(content);
        List<String> filePaths = new ArrayList<>();
        for(MultipartFile file: files){
            if(!file.isEmpty()) {
                //저장 경로 지정
                String projectPath;

                //랜덤 이름 만들어줌
                UUID uuid = UUID.randomUUID();
                String fileName;
                String fileadd;
                if(file.getOriginalFilename().indexOf(".mp4") != -1) {
                    fileName = uuid + "_" + ".mp4";
                    fileadd = "mp4/";
                    projectPath = System.getProperty("user.dir") + "/src/main/resources/files/mp4";
                } else {
                    fileName = uuid + "_" + file.getOriginalFilename();
                    fileadd = "img/";
                    projectPath = System.getProperty("user.dir") + "/src/main/resources/files/img";
                }


                File saveFile = new File(projectPath, fileName);

                file.transferTo(saveFile);

                community.setFilename(fileName);
                filePaths.add(fileadd + fileName);
            }
        }

        community.setFilePath(filePaths);
        this.communityRepository.save(community);

    }
    public void delete(Community community) {
        this.communityRepository.delete(community);
    }
	public List<Community> getAllCommunities() {
		return communityRepository.findAllByOrderByIdDesc();
	}
	public void deleteCommunity(Integer communityId) {
        Optional<Community> communityOptional = communityRepository.findById(communityId);
        communityOptional.ifPresent(communityRepository::delete);
    }
	public Community getCommunity(Integer id) {
        Optional<Community> communityOptional = communityRepository.findById(id);
        return communityOptional.orElse(null);
	}

}
