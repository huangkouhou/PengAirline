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

	@Autowired
	private JavaMailSender javaMailSender;

	public static void main(String[] args) {
		SpringApplication.run(PengAirlineApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(){
		return args -> {
			try {

				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(
						mimeMessage,
						MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
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

