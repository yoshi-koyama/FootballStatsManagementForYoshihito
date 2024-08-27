package football.StatsManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class StatsManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatsManagementApplication.class, args);
	}

}
