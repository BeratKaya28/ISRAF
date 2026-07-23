package com.israf.api.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmail;
	
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public String generateVerificationCode() {
		Random random = new Random();
		int code = 100000 + random.nextInt(900000);
		return String.valueOf(code);
	}
	
	
	@Async
	public void sendVerificationEmail(String toEmail, String code) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom(fromEmail);
		
		message.setTo(toEmail);
		
		message.setSubject("ISRAF - E-posta Dogrulama Kodu");
		message.setText("Merhaba, \n\n"
						+"Israf ekosistemimize satici olarak kayit olamk icin ilk adimi attiniz!"
						+"Hesabinizi dogrulamak ve giris yapmak icin lutfen size verilen kodu kullaniniz:\n\n"
						+"Dogrulama Kodunuz: "+code+"\n\n"
						+"Bu islemi siz yapmadiysaniz bu mail i dikkate almayiniz.\n\n"
						+"Israf Ekibi");
		mailSender.send(message);
		
	}
	
	@Async
	public void sendPasswordResetEmail(String toEmail, String code) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(fromEmail);
		message.setTo(toEmail);
		message.setSubject("ISRAF - Sifre Sifirlama Kodunuz!");
		message.setText("Merhaba, \n\n"
						+"Assagidaki kodu kullanarak yeni sifrenizi belirleyiniz:\n\n"
						+"Sifre sifirlama kodunuz: "+code+"\n\n"
						+"Verilen kod 15 dakika icerisinde kullanilmalidir."
						+"Israf Ekibi");	
		mailSender.send(message);
		
	}
	
	
	
	
}
