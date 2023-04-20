package com.example.linux.pdfPublisheru;

import com.example.linux.pdfPublisheru.publisher.FileStorageProperties;
import com.example.linux.pdfPublisheru.service.StorageProperties;
import com.example.linux.pdfPublisheru.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class, StorageProperties.class
})
public class PdfPublisheruApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfPublisheruApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
