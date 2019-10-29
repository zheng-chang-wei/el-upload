/*******************************************************************************
 * Copyright (c) 2019, 2019 Hirain Technologies Corporation.
 ******************************************************************************/
package com.test.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Version 1.0
 * @Author jianwen.xin@hirain.com
 * @Created 2019年5月15日 上午10:01:18
 * @Description
 *              <p>
 * @Modification
 *               <p>
 *               Date Author Version Description
 *               <p>
 *               2019年5月15日 jianwen.xin@hirain.com 1.0 create file
 */
@Configuration
public class WebSocketConfig {

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
