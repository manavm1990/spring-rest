package tech.codefinity.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
// ⚠️ Need to change table name b/c can't use "ORDER"
@Table(name = "CUSTOMER_ORDER")
public class Order {

  private @Id @GeneratedValue Long id;

  private String desc;
  private OrderStatus status;

  public Order() {}

  public Order(String desc, OrderStatus status) {
    this.desc = desc;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Order)) {
      return false;
    }

    Order order = (Order) obj;

    return Objects.equals(this.id, order.id)
        && Objects.equals(this.desc, order.desc)
        && this.status == order.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.desc, this.status);
  }

  @Override
  public String toString() {
    return "Order{id=" + this.id + ", desc=" + this.desc + ", status=" + this.status;
  }
}
