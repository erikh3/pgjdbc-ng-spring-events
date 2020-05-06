package cz.muni.fi.pa036.pgjdbcngspringdemo;

import com.github.javafaker.Faker;
import cz.muni.fi.pa036.pgjdbcngspringdemo.event.NotificationReceivedSpringEvent;
import cz.muni.fi.pa036.pgjdbcngspringdemo.event.NotificationSendSpringEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.Locale;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class PgjdbcNgSpringDemoApplication {
	private static final Faker faker = new Faker(Locale.getDefault());

	public static void main(String[] args) {
		SpringApplication.run(PgjdbcNgSpringDemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner app(PersonRepository personRepository, ApplicationContext ctx, ApplicationEventPublisher applicationEventPublisher) {
		return args -> {
			// create random person
			Person person = new Person();
			person.setNick(faker.name().username());
			personRepository.saveAndFlush(person);

			log.info("Welcome, your nick is " + person.getNick());
			log.info("Type any message, to exit press enter without any input.");

			Scanner keyboard = new Scanner(System.in);
			while (true) {
				String input = keyboard.nextLine();
				if (input == null || "".equals(input)) {
					break;
				}

				NotificationSendSpringEvent event = new NotificationSendSpringEvent(
						this,
						"my_channel_name",
						person.getNick() + " said " + input
				);
				applicationEventPublisher.publishEvent(event);
			}
			keyboard.close();

			log.info("Application ended!");

			SpringApplication.exit(ctx);
		};
	}

	@Async
	@EventListener
	public void handleNotificationReceived(NotificationReceivedSpringEvent event) {
		log.info("New notification received on channel " + event.getChannel() + ", " + event.getPayload());
	}
}
