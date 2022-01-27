package tech.codefinity.order;

class OrderNotFoundException extends RuntimeException {

  OrderNotFoundException(Long id) {
    super(String.format("Couldn't find order with id: %d", id));
  }
}
