package tech.codefinity.employee;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController // No stupid templates - just send response body with the data
class EmployeeController {
  private final EmployeeRepository repository;
  private final EmployeeModelAssembler assembler;

  //  Injection 💉
  public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  // The Aggregate Root is the parent Entity to all other Entities and Value Objects within the
  // Aggregate.
  // https://www.google.com/search?q=What+is+an+aggregate+root
  //    Aggregate root
  //    tag::get-aggregate-root[]
  @GetMapping("/employees")
  CollectionModel<EntityModel<Employee>> index() {

    List<EntityModel<Employee>> employees =

        //            Transform the employees into Entities (include links)
        repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());

    return CollectionModel.of(
        employees, linkTo(methodOn(EmployeeController.class).index()).withSelfRel());
  }
  //  end::get-aggregate-root[]

  @PostMapping("/employees")
  Employee add(@RequestBody Employee newEmployee) {
    return repository.save(newEmployee);
  }

  @GetMapping("/employees/{id}")
  //  EntityModel<T> is a generic container from Spring HATEOAS that includes not only the data but
  // a collection of links.
  //  https://spring.io/guides/tutorials/rest/
  EntityModel<Employee> show(@PathVariable Long id) {

    Employee foundEmployee =
        repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

    return assembler.toModel(foundEmployee);
  }

  @PutMapping("/employees/{id}")
  Employee replace(@RequestBody Employee employee2Update, @PathVariable Long id) {

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
