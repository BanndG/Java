package code;

public class Manager extends Employee {

  private double bonus;

  /**
   * @param name   the employee's name
   * @param salary the salary
   * @param year   the hire year
   * @param month  the hire month
   * @param day    the hire day
   */
  public Manager(String name, double salary, int year, int month, int day) {
    super(name, salary, year, month, day);
    bonus = 0;
  }

  public Manager() {
    super();
  }

  @Override
  public double getSalary() {
    double baseSalary = super.getSalary();
    return baseSalary + bonus;
  }

  public void setBonus(double b) {
    bonus = b;
    Employee employee = new Employee();
  }

  @Override
  public boolean equals(Object otherObject) {
    if (!super.equals(otherObject)) {
      return false;
    }
// super.equals checked that this and otherObject belong to the same class
    Manager other = (Manager) otherObject;
    return bonus == other.bonus;
  }
}
