package com.line.business.work.request.dto;

import java.util.List;

import com.line.business.work.entity.UserEntity;

import lombok.Data;

@Data
public class LoginResponse {

	private String status;
	private String code;
	private UserEntity data;
	private String message;
	private String token;
}
