package com.example.FOT_Event_Managment_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FotEventManagmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FotEventManagmentSystemApplication.class, args);
	}

}
