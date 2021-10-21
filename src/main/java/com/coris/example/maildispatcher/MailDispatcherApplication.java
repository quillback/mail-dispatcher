package com.coris.example.maildispatcher;

import com.coris.example.maildispatcher.model.ItemModel;
import com.coris.example.maildispatcher.network.inbound.Communication;
import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
public class MailDispatcherApplication {

	static Logger logger = LoggerFactory.getLogger(MailDispatcherApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MailDispatcherApplication.class, args);
	}

	@Bean
	public Communication<ItemModel> inboundCommunication(){
		logger.error("You must implement a communication channel");

		return null;
	}


	@Bean
	public JavaMailSender mailSender(){
//		final var mail = new JavaMailSenderImpl();
//		mail.setHost("smtp.gmail.com");
//		mail.setPort(587);
//		mail.setUsername("***@gmail.com");
//		mail.setPassword("*****");
//
//		Properties props = mail.getJavaMailProperties();
//		props.put("mail.transport.protocol", "smtp");
//		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.debug", "true");

		logger.error("You must implement the mail properties to be able to use email");

		return new JavaMailSenderImpl();
	}


	@Component
	public static class ParseEngine{
		private final Logger logger = LoggerFactory.getLogger(getClass());
		private final ExecutorService executorService;
		private final ScheduledExecutorService scheduledExecutorService;


		ParseEngine(){
			final var count = Runtime.getRuntime().availableProcessors();
			this.executorService = Executors.newFixedThreadPool(count+1);
			this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				this.executorService.shutdownNow();
				this.scheduledExecutorService.shutdownNow();
				this.logger.info("Shutdown Hook -> executor shut down");
			}));

		}

		@Bean(destroyMethod = "shutdown")
		public Engine engine(){
			return new EngineBuilder().setTaskExecutor(this.executorService)
					.setTimerScheduler(this.scheduledExecutorService).build();

		}
	}

}
