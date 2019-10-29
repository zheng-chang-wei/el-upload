package com.test.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.test.service.UploadFileService;

@RestController
@RequestMapping("/initialdata")
public class UploadFileController {

	@Autowired
	UploadFileService uploadFileService;

	@PostMapping("/uploadfile")
	public void saveInitialData(List<MultipartFile> files) {
		if (files != null) {
			String realPath = System.getProperty("user.dir") + "/temp";
			File file = new File(realPath);
			if (file.exists()) {
				file.delete();
			} else {
				file.mkdir();
			}
			for (MultipartFile multipartFile : files) {
				String originalFilename = multipartFile.getOriginalFilename();
				File targetFile = new File(realPath + "/" + originalFilename);
				try {
					multipartFile.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
				uploadFileService.importFile(multipartFile, realPath, originalFilename, "admin");
			}
		}
	}
}
