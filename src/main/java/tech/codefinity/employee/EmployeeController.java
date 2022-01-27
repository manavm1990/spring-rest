package tech.codefinity.employee;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
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
  ResponseEntity<?> add(@RequestBody Employee newEmployee) {
    EntityModel<Employee> entity =

        //            Wrap the newly saved Employee
        assembler.toModel(repository.save(newEmployee));

    //    Use ResponseEntity to create a 201 status message, leveraging a URI from the 'self link'
    // to create Location header.
    return ResponseEntity.created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entity);
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
  ResponseEntity<?> replace(@RequestBody Employee employee2Update, @PathVariable Long id) {

    Employee updatedEmployee =
        repository
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

    EntityModel<Employee> entity = assembler.toModel(updatedEmployee);

    return ResponseEntity.

        //            Use 'created' to get a 201 with the Location response header
        created(entity.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entity);
  }

  @DeleteMapping("/employees/{id}")
  ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);

    return ResponseEntity.

        //            204 - No Content
        noContent()
        .build();
  }
}
