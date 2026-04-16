package com.example.FOT_Event_Managment_System;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.sql.Connection;

@SpringBootApplication
@EnableAsync
public class FotEventManagmentSystemApplication {

	@Autowired
	private DBConnection dbConnection;

	public static void main(String[] args) {
		SpringApplication.run(FotEventManagmentSystemApplication.class, args);
	}

	@PostConstruct
	public void init() {
		Connection conn = dbConnection.getConnection();

		if (conn != null) {
			System.out.println("Database connection verified at startup!");
		}
	}
}