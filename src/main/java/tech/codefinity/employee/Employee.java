package tech.codefinity.employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity // A JPA annotation that prepares this...thing for storage.
public class Employee {

//  These map to columns in database 🗃️.
  private @Id @GeneratedValue Long id; // Primary 🔑 - automatically populated.
  private String name;
  private String role;

  Employee() {}

  Employee(String name, String role) {

    this.name = name;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Employee)) {
      return false;
    }

    Employee emp = (Employee) obj;
    return Objects.equals(this.id, emp.id)
        && Objects.equals(this.name, emp.name)
        && Objects.equals(this.role, emp.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.role);
  }

  @Override
  public String toString() {
    return "Employee{id=" + this.id + ", name=" + this.name + ", role=" + this.role + "}";
  }
}
