package com.codefarme.imchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//SpringBoot项目的Bean装配默认规则是根据Application类所在的包位置从上往下扫描！
@SpringBootApplication()
@MapperScan("com.codefarme.imchat.mapper")
@ComponentScan("com.codefarme.imchat.service")
@ComponentScan("com.codefarme.imchat.limit")
@ComponentScan("org.n3r.idworker")
public class ZxfApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZxfApplication.class, args);
	}
}
