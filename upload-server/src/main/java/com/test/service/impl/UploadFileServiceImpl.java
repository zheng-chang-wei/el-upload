package com.test.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.test.model.ResponseBo;
import com.test.service.UploadFileService;
import com.test.websocket.WebSocketServer;

@Service
public class UploadFileServiceImpl implements UploadFileService {

	@Async("asyncServiceExecutor")
	@Override
	public void importFile(MultipartFile multipartFile, String realPath, String originalFilename, String userName) {
		try {
			// TODO 解析文件
			Thread.sleep(1000);
			WebSocketServer.sendFileUploadResult(userName, ResponseBo.ok(originalFilename));
		} catch (Exception e) {
			e.printStackTrace();
			WebSocketServer.sendFileUploadResult(userName, ResponseBo.error(originalFilename));
		}
	}

}
