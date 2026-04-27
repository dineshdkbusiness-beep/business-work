package com.line.business.work.entity;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "usersrecords")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIndex;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false, length = 20)
    private String userType;

    @Column(nullable = false)
    private boolean isAlive = true;

    @Column(nullable = false)
    private boolean userDeleted = false;

    private LocalDateTime expireDate;

    @Column(nullable = false)
    private int loginAttempts = 0;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "active_token")
    private String activeToken;
    
    public UserEntity(String username, LocalDateTime createdDate,String userType, boolean isAlive, boolean userDeleted,
                       LocalDateTime expireDate, int loginAttempts, String password,String email) {

			this.username = username;
			this.createdDate = createdDate;
			this.userType = userType;
			this.isAlive = isAlive;
			this.userDeleted = userDeleted;
			this.expireDate = expireDate;
			this.loginAttempts = loginAttempts;
			this.password = password;
			this.email = email;
	}
   
  
}

