package com.stephen.blowball;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 *
 * @author stephen qiu
 */
@SpringBootApplication
@MapperScan("com.stephen.blowball.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class BlowballApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BlowballApplication.class, args);
	}
	
}
