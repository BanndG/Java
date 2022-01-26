package code;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.accessibility.Accessible;

public class StaticTest {

  public static void main(String[] args)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

    Class clazz = Employee.class;
    Constructor<Employee> constructor = clazz.getConstructor(String.class, double.class, int.class,
        int.class, int.class);
    Employee a = constructor.newInstance("1", 1.0, 1, 1, 1);

    Employee[] employees = new Manager[10];
    Class cl2 = employees.getClass();
    cl2.getComponentType();


    Object[] objects = employees;
    Employee[] employees1 = employees;

    ArrayList<Employee> aaa = (ArrayList<Employee>) new ArrayList();

    int[] ints = new int[100];

    Method method = clazz.getMethod("printname",new Class[] {String.class});
    method.invoke(null,"1");



    Calendar gregorianCalendar = new GregorianCalendar();
    int i = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
    System.out.println(i);

    Double.compare()
        Arrays.sort();
    Comparable
  }
}
