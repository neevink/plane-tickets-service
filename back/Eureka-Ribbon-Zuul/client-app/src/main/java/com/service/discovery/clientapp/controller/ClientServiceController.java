package com.service.discovery.clientapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.service.discovery.clientapp.model.GreetResponseClient;

@RestController
public class ClientServiceController {

	@Autowired
	private ClientAppServiceProxy proxy;
	
	@GetMapping("/greet")
	public GreetResponseClient getGreetingFeign() {
		GreetResponseClient response = proxy.getGreetingMessage();
		return response;
	}
}
