package tech.codefinity.employee;

class EmployeeNotFoundException extends RuntimeException {

  EmployeeNotFoundException(Long id) {
    super(String.format("Couldn't find employee with id: %d", id));
  }
}
