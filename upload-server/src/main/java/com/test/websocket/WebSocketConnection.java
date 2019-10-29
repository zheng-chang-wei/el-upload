/*******************************************************************************
 * Copyright (c) 2019, 2019 Hirain Technologies Corporation.
 ******************************************************************************/
package com.test.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@Component
@ServerEndpoint(value = "/websocket")
public class WebSocketConnection {

	private Session session;

	private String key;

	@OnOpen
	public void onOpen(Session session) {
		this.session = session;
	}

	@OnClose
	public void onClose() {
		WebSocketServer.removeConnection(key, this);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("receive:" + message);
		this.key = message;
		WebSocketServer.addConnection(this.key, this);
	}

	@OnError
	public void onError(Session session, Throwable e) {
		System.out.println("error");
		e.printStackTrace();
	}

	public synchronized void sendMessage(String message) throws IOException {
		if (session.isOpen()) {
			session.getBasicRemote().sendText(message);
		}
	}
}
