package com.ckb.explorer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@EnableFeignClients
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class, org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class})
@MapperScan("com.ckb.explorer.mapper")
public class CkbExplorerWebApplication {

	public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(CkbExplorerWebApplication.class, args);
	}

}
