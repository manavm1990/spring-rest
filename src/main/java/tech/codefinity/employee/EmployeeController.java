package tech.codefinity.employee;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class EmployeeController {
  private final EmployeeRepository repository;

  public EmployeeController(EmployeeRepository repository) {
    this.repository = repository;
  }

  //    Aggregate root
  //    tag::get-aggregate-root[]
  @GetMapping("/employees")
  List<Employee> index() {
    return repository.findAll();
  }
  //  end::get-aggregate-root[]

  @PostMapping("/employees")
  Employee add(@RequestBody Employee newEmployee) {
    return repository.save(newEmployee);
  }

  @GetMapping("/employees/{id}")
  Employee show(@PathVariable Long id) {

    return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
  }

  @PutMapping("/employees/{id}")
  Employee update(@RequestBody Employee employee2Update, @PathVariable Long id) {

    return repository
        .findById(id)
        .map(
            emp -> {
              emp.setName(employee2Update.getName());
              emp.setRole(employee2Update.getRole());

              return repository.save(emp);
            })
        .orElseGet(
            () -> {
              employee2Update.setId(id);
              return repository.save(employee2Update);
            });
  }

  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);
  }
}
