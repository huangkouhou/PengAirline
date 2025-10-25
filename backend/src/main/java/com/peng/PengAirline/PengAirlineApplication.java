package com.peng.PengAirline;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.internet.MimeMessage;

@SpringBootApplication
public class PengAirlineApplication {

	@Autowired//依赖注入。您可以把它想象成在向 Spring 索要一个工具：这个注解告诉 Spring：“请自动把配置好的那个 JavaMailSender 实例（邮递员）交给我，我要在这里使用它。”
	//Spring 会自动去读取您的 application.properties 或 application.yml 文件，根据您提供的配置（如服务器地址、用户名、密码）来创建这个“邮递员”实例。
	private JavaMailSender javaMailSender;

	public static void main(String[] args) {
		SpringApplication.run(PengAirlineApplication.class, args);
	}

	@Bean//告诉 Spring，这个方法会返回一个对象（Bean），请 Spring 来管理它。
	CommandLineRunner runner(){//CommandLineRunner：这是一个特殊的接口。任何被注册为 Bean 的 CommandLineRunner，都会在 Spring Boot 应用完全启动并准备就绪之后，自动执行其 run 方法
		return args -> {
			try {

				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(
						mimeMessage,
						MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,//这是一个模式设置，告诉助手这封邮件支持多种内容（比如正文、附件和内嵌图片混合）。
						StandardCharsets.UTF_8.name()
				);
				helper.setTo("huangkouhou@gmail.com");
				helper.setSubject("Hello Testing");
				helper.setText("Hello World! You are doing well, Peng");

				System.out.println("About to send Email...");
				javaMailSender.send(mimeMessage);

			}catch (Exception ex){
				System.out.println(ex.getMessage());
			}
		};
	}

}

