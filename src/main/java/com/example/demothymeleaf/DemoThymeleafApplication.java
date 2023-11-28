package com.example.demothymeleaf;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@EnableEncryptableProperties
@SpringBootApplication
@EnableEncryptableProperties
public class DemoThymeleafApplication {


	public static void main(String[] args) {
		SpringApplication.run(DemoThymeleafApplication.class, args);
	}

}
