package tech.codefinity.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EmployeeSeeds {
  private static final Logger logger = LoggerFactory.getLogger(Employee.class);

  @Bean
  // Spring Boot will run ALL CommandLineRunner beans once the application context is loaded
  // https://spring.io/guides/tutorials/rest/
  CommandLineRunner init(EmployeeRepository repository) {
    return args -> {
      logger.info(
          "Seeding Database... " + repository.save(new Employee("Bilbo", "Baggins", "burglar")));
      logger.info(
          "Seeding Database..." + repository.save(new Employee("Frodo", "Baggins", "thief")));
    };
  }
}
