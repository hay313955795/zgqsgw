package com.backup.zgqsgw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 加入@EnableScheduling注解启动定时任务
 */
@EnableScheduling
@SpringBootApplication
public class ZgqsgwApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZgqsgwApplication.class, args);
	}



}
