package ag04.project.moneyheist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneyHeistApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyHeistApplication.class, args);
    }

}