package com.test.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

	void importFile(MultipartFile multipartFile, String realPath, String originalFilename, String string);

}
