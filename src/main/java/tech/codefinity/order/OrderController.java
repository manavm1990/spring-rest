package tech.codefinity.order;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class OrderController {

  private final OrderRepository repository;
  private final OrderModelAssembler assembler;

  OrderController(OrderRepository repository, OrderModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/orders")
  CollectionModel<EntityModel<Order>> index() {
    List<EntityModel<Order>> orders =
        repository.findAll().stream().map(assembler::toModel).collect(Collectors.toList());

    return CollectionModel.of(
        orders, linkTo(methodOn(OrderController.class).index()).withSelfRel());
  }

  @GetMapping("/orders/{id}")
  EntityModel<Order> show(@PathVariable Long id) {

    Order foundOrder = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    return assembler.toModel(foundOrder);
  }

  @PostMapping("/orders")
  ResponseEntity<EntityModel<Order>> add(@RequestBody Order order) {

    order.setStatus(OrderStatus.IN_PROGRESS);

    Order newOrder = repository.save(order);

    return ResponseEntity.created(
            linkTo(methodOn(OrderController.class).show(newOrder.getId())).toUri())
        .body(assembler.toModel(newOrder));
  }

  @PutMapping("/orders/{id}/complete")
  ResponseEntity<?> complete(@PathVariable Long id) {

    Order order2Complete =
        repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (order2Complete.getStatus() == OrderStatus.IN_PROGRESS) {
      order2Complete.setStatus(OrderStatus.COMPLETED);
      return ResponseEntity.ok(assembler.toModel(repository.save(order2Complete)));
    }

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
        .body(
            Problem.create()
                .withTitle("Method Not Allowed❗")
                .withDetail(
                    String.format(
                        "Cannot COMPLETE order that has status: %s", order2Complete.getStatus())));
  }

  @DeleteMapping("/orders/{id}/cancel")
  ResponseEntity<?> cancel(@PathVariable Long id) {

    Order order2Cancel = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (order2Cancel.getStatus() == OrderStatus.IN_PROGRESS) {
      order2Cancel.setStatus(OrderStatus.CANCELLED);
      return ResponseEntity.ok(assembler.toModel(repository.save(order2Cancel)));
    }

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
        .body(
            Problem.create()
                .withTitle("Method Not Allowed❗")
                .withDetail(
                    String.format(
                        "Cannot cancel order that has status: %s", order2Cancel.getStatus())));
  }
}
