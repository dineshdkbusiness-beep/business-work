package com.line.business.work.response.dto;

import lombok.Data;

@Data
public class LoginRequest {

	private String userName;
	private String password;
}
