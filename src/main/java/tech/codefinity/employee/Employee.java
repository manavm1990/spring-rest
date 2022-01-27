package tech.codefinity.employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity // A JPA annotation that prepares this...thing for storage.
public class Employee {

  //  These map to columns in database 🗃️.
  private @Id @GeneratedValue Long id; // Primary 🔑 - automatically populated.
  private String fname;
  private String lname;
  private String role;

  Employee() {}

  public Employee(String fname, String lname, String role) {

    this.fname = fname;
    this.lname = lname;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return String.format("%s %s", this.fname, this.lname);
  }

  public void setName(String name) {
    String[] splitName = name.split(" ");
    this.fname = splitName[0];
    this.lname = splitName[1];
  }

  public String getFname() {
    return fname;
  }

  public void setFname(String fname) {
    this.fname = fname;
  }

  public String getLname() {
    return lname;
  }

  public void setLname(String lname) {
    this.lname = lname;
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
        && Objects.equals(this.fname, emp.fname)
        && Objects.equals(this.lname, emp.lname)
        && Objects.equals(this.role, emp.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.fname, this.lname, this.role);
  }

  @Override
  public String toString() {
    return "Employee{id="
        + this.id
        + ", fname="
        + this.fname
        + ", lname="
        + this.lname
        + "role="
        + this.role
        + "}";
  }
}
