package com.dyy.jinjingzheng.checkserverstatus;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.dyy")
@SpringBootApplication
public class CheckserverstatusApplication implements CommandLineRunner {
	private static final Logger log = Logger.getLogger(CheckserverstatusApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(CheckserverstatusApplication.class, args);

	}

	@Override
	public void run(String... strings) throws Exception {
		log.info("*****************启动成功****************");
		System.out.println("*****************启动成功****************");

	}




}
