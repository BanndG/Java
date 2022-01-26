package code;

import java.time.LocalDate;

public class Employee {

  private final String name;
  private final LocalDate hireDay;
  private double salary;


  public Employee(String name, double salary, int year, int month, int day) {
    this.name = name;
    this.salary = salary;
    hireDay = LocalDate.of(year, month, day);
  }

  public Employee() {
    name = "";
    hireDay = LocalDate.MAX;
  }

  public double getSalary() {
    return salary;
  }

  public LocalDate getHireDay() {
    return hireDay;
  }

  String getName() {
    return name;
  }

  public void raiseSalary(double byPercent) {
    double raise = salary * byPercent / 100;
    salary += raise;
  }

  public Employee returnThis() {
    return this;
  }

  @Override
  public boolean equals(Object otherObject) {
// a quick test to see if the objects are identical
    if (this == otherObject) {
      return true;
    }
// must return false if the explicit parameter is null
    if (otherObject == null) {
      return false;
    }
// if the classes don' t match, they can' t be equal
    if (getClass() != otherObject.getClass()) {
      return false;
    }
// now we know otherObject is a non-null Employee
    Employee other = (Employee) otherObject;
// test whether the fields have identical values
    return name.equals(other.name)
        && salary == other.salary
        && hireDay.equals(other.hireDay);
  }

  public static void printname(String name){
    System.out.println(name);
  }
}