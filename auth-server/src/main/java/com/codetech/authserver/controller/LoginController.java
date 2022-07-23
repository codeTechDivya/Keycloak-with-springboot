package com.codetech.authserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codetech.authserver.model.IntrospectResponse;
import com.codetech.authserver.model.LoginRequest;
import com.codetech.authserver.model.LoginResponse;
import com.codetech.authserver.model.Response;
import com.codetech.authserver.model.TokenRequest;
import com.codetech.authserver.service.LoginService;

@RestController
@RequestMapping("/auth")
public class LoginController {
	
	
	@Autowired
	LoginService loginservice;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest loginrequest) {
		return loginservice.login(loginrequest);
		
		
		
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Response> logout (@RequestBody TokenRequest token) {
		return loginservice.logout(token);
	}
	
	@PostMapping("/introspect")
	public ResponseEntity<IntrospectResponse> introspect(@RequestBody TokenRequest token) {
		return loginservice.introspect(token);
	}

}
