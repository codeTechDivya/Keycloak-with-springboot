package com.codetech.authserver.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.codetech.authserver.model.IntrospectResponse;
import com.codetech.authserver.model.LoginRequest;
import com.codetech.authserver.model.LoginResponse;
import com.codetech.authserver.model.Response;
import com.codetech.authserver.model.TokenRequest;

@Service
public class LoginService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
	private String issueUrl;
	
	@Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-id}")
	private String clientId;
	
	@Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.client-secret}")
	private String clientSecret;
	
	@Value("${spring.security.oauth2.client.registration.oauth2-client-credentials.authorization-grant-type}")
	private String grantType;
	

	public ResponseEntity<LoginResponse> login(LoginRequest loginrequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", clientId);
		map.add("client_secret", clientSecret);
		map.add("grant_type", grantType);
		map.add("username", loginrequest.getUsername());
		map.add("password", loginrequest.getPassword());
		
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map,headers);
		
		ResponseEntity<LoginResponse> response = restTemplate.postForEntity("http://localhost:8180/auth/realms/auth-realm/protocol/openid-connect/token", httpEntity, LoginResponse.class);
		return new ResponseEntity<>(response.getBody(),HttpStatus.OK);
	
		
	}


	public ResponseEntity<Response> logout(TokenRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", clientId);
		map.add("client_secret", clientSecret);
		map.add("refresh_token", request.getToken());
		
		
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map,headers);
		
		ResponseEntity<Response> response = restTemplate.postForEntity("http://localhost:8180/auth/realms/auth-realm/protocol/openid-connect/logout", httpEntity, Response.class);
		
		Response res = new Response();
		if(response.getStatusCode().is2xxSuccessful()) {
			res.setMessage("Logged out successfully");
		}
		return new ResponseEntity<>(res,HttpStatus.OK);
		
		
	}


	public ResponseEntity<IntrospectResponse> introspect(TokenRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("client_id", clientId);
		map.add("client_secret", clientSecret);
		map.add("token", request.getToken());
		
		
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map,headers);
		
		ResponseEntity<IntrospectResponse> response = restTemplate.postForEntity("http://localhost:8180/auth/realms/auth-realm/protocol/openid-connect/token/introspect", httpEntity, IntrospectResponse.class);
		return new ResponseEntity<>(response.getBody(),HttpStatus.OK);
	}
	
	

}
