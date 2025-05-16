package com.daaloul.BackEnd;

// import com.daaloul.BackEnd.models.User;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
// import org.springframework.boot.CommandLineRunner;
@SpringBootApplication
@EntityScan(basePackages = "com.daaloul.BackEnd.models")

public class BackEndApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}





}
