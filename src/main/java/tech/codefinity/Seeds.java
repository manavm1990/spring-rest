package tech.codefinity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.codefinity.employee.Employee;
import tech.codefinity.employee.EmployeeRepository;
import tech.codefinity.order.Order;
import tech.codefinity.order.OrderRepository;
import tech.codefinity.order.OrderStatus;

@Configuration
class Seeds {
  private static final Logger logger = LoggerFactory.getLogger(Employee.class);

  @Bean
  // Spring Boot will run ALL CommandLineRunner beans once the application context is loaded
  // https://spring.io/guides/tutorials/rest/
  CommandLineRunner init(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
    return args -> {
      logger.info(
          "Seeding Database... "
              + employeeRepository.save(new Employee("Bilbo", "Baggins", "burglar")));
      logger.info(
          "Seeding Database..."
              + employeeRepository.save(new Employee("Frodo", "Baggins", "thief")));

      orderRepository.save(new Order("MacBook Pro", OrderStatus.COMPLETED));
      orderRepository.save(new Order("iPhone", OrderStatus.IN_PROGRESS));

      orderRepository.findAll().forEach(order -> logger.info("Seeding Database... " + order));
    };
  }
}
