/*******************************************************************************
 * Copyright (c) 2019, 2019 Hirain Technologies Corporation.
 ******************************************************************************/
package com.test.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.test.model.ResponseBo;

@Component
public class WebSocketServer {

	private static Map<String, WebSocketConnection> connectionMap = new ConcurrentHashMap<>();

	private static AtomicInteger onlineCount = new AtomicInteger(0);

	public static void addConnection(String key, WebSocketConnection connection) {
		connectionMap.put(key, connection);
		onlineCount.incrementAndGet();
	}

	public static void removeConnection(String key, WebSocketConnection connection) {
		connectionMap.remove(key);
		onlineCount.decrementAndGet();
	}

	public static void sendFileUploadResult(String userName, ResponseBo message) {
		for (Entry<String, WebSocketConnection> entry : connectionMap.entrySet()) {
			String userName_uuid = entry.getKey();
			String[] split = userName_uuid.split("_");
			if (split[0].equals(userName)) {
				WebSocketConnection connection = entry.getValue();
				if (connection != null) {
					try {
						connection.sendMessage(JSONObject.toJSONString(message));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}
	}

}
