package tech.codefinity.order;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

  @Override
  public EntityModel<Order> toModel(Order order) {
    //    We customize the links to only show valid ones based on OrderStatus (canceling or
    // completing) 👇🏾

    //        Unconditional links to single-item resource and aggregate root
    EntityModel<Order> entity =
        EntityModel.of(
            order,
            linkTo(methodOn(OrderController.class).show(order.getId())).withSelfRel(),
            linkTo(methodOn(OrderController.class).index()).withRel("orders"));

    if (order.getStatus() == OrderStatus.IN_PROGRESS) {

      //  Conditional links based on order status
      entity.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));

      entity.add(
          linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
    }
    return entity;
  }
}
