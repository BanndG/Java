# ChapterThree 继承

这一章中，我们使用员工Employee和经理Manager的传统示例。从理论上讲，在Manager与Employee之间存在着明显的"is-a"(
是)关系，每个经理都是一名雇员:"is-a"关系是继承的一个明显特征。

## 样例代码

### Employee.java

```java
package inheritance;
import java.time.LocalDate;

public class Employee {

  private final String name;
  private double salary;
  private final LocalDate hireDay;


  public Employee(String name, double salary, int year, int month, int day) {
    this.name = name;
    this.salary = salary;
    hireDay = LocalDate.of(year, month, day);
  }

  public double getSalary() {
    return salary;
  }

  public LocalDate getHireDay() {
    return hireDay;
  }

  public String getName() {
    return name;
  }

  public void raiseSalary(double byPercent) {
    double raise = salary * byPercent / 100;
    salary += raise;
  }
}
```

### Manager.java

```java
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

  public double getSalary() {
    double baseSalary = super.getSalary();
    return baseSalary + bonus;
  }

  public void setBonus(double b) {
    bonus = b;
  }
}
```

## 类、超类和子类

### 定义子类

通过关键字`extends`表明类之间的继承关系

```java
public class Manager extends Employee
{
	// 添加方法和域
}
```

子类会自动继承父类的实例域和方法(除private 外都能继承)

### 覆盖方法

Manager的工资计算方式与一般雇员不同 所以需要提供一个新的getSalary方法来覆盖父类的同名方法

1. ```java
   public double getSalary()
   {
   	return salary + bonus; // won't work
   }
   ```

​	方法不生效 因为salary为父类的私有域子类无法直接访问

2. ```java
   public double getSalary()
   {
   	double baseSalary = getSalary()；// still won't work
   	return baseSalary + bonus;
   }
   ```

​	此时getSalary因为被覆盖 调用的是自身的方法 也就是自我调用无限循环

3. ```java
   public double getSalary() {
       double baseSalary = super.getSalary();
       return baseSalary + bonus;
   }
   ```

> `super`与`this`不同 super只是一个指示调用父类对象的关键字 无法作为对象引用进行传递，而this可以作为对象引用进行返回
>
> ```java
> public Employee returnThis(){
>     return this;
> }
> ```

### 子类构造器

```java
public Manager(St ring name, double salary, int year, int month, int day)
{
	super(name, salary, year, month, day) ;
	bonus = 0;
}
```

`super`表示调用父类对应参数列表的构造器进行初始化

> 子类调用父类的构造器得显式地生命在子类构造器的第一行 如无显式声明 则默认调用父类的无参构造器 若父类无无参构造器则报错

### 继承层次

继承可以有多层 也可以多次派生 Employee也可以派生为Programmer,Manager可以继续派生为Executive

![image-20220109170050512](C:\Users\luoyu\AppData\Roaming\Typora\typora-user-images\image-20220109170050512.png)

继承可且仅可以继承一个父类，若想要实现多继承 通过实现多个接口的方式来达到

### 多态

```java
public static void main(String[] args) {
	Manager boss = new Manager("Carl Cracker", 80000，1987, 12, 15) ;
    boss.setBonus(5000) ;
	Employee[] staff = new Employee[3];

	staff[0] = boss;
	staff[1] = new Employee("Harry Hacker", 50000, 1989, 10, 1) ;
	staff[2] = new Employee("Tony Tester", 40000, 1990, 3, 15);
	for (Employee e : staff){
        System.out.println(e.getName() + " " + e.getSalary()) ;
    }
}
```

```bash
Carl Cracker 85000.0
Harry Hacker 50000.0
Tommy Tester 40000.0
```

尽管staff[0]被声明为Employee 但是实际指向的对象为Manager 所以实际调用的方法还是Manager的getSalary()

[具体调用过程见动态绑定](#dynamicbinding)

但是若想要调用Manager的setBonus()方法则会报错 因为staff[0]被声明的还是一个Employee对象 而Employee是没有setBonus()方法的

```java
boss.setBonus(5000); // OK
staff[0].setBonus(5000); // Error
```

> 在Java中，子类数组的引用可以转换成超类数组的引用，而不需要采用强制类型转换。
>
> ```java
> // fill the staff array with three Employee objects
>  Manager[] managers = new Manager[10];
>  Employee[] staff = managers; // OK
>  staff[0] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
>  managers[0].setBonus(5000);
> ```
>
> 但在这里，staff[O]与manager[0]引用的是同一个对象，似乎我们把一个普通雇员擅自归入经理行列中了。这是一种很忌讳发生的情形，当调用managers[0].setBonus(5000) 的时候，将会导致调用一个不存在的实例域，进而搅乱相邻存储空间的内容。
> 为了确保不发生这类错误，所有数组都要牢记创建它们的元素类型，并负责监督仅将类型兼容的引用存储到数组中。例如，使用new managers[10] 创建的数组是一个经理数组。如果试图存储一个Employee 类型的引用就会引发ArrayStoreException异常。以下代码即是报错代码
>
> ```java
> public static void main(String[] args) {
> // fill the staff array with three Employee objects
> Manager[] managers = new Manager[10];
> Employee[] staff = managers; // OK
> staff[0] = new Employee("Harry Hacker", 50000, 1989, 10, 1);
> }
> ```
> 以下代码不会报错
> ```java
> public static void main(String[] args) {
>     // fill the staff array with three Employee objects
>     Manager[] managers = new Manager[10];
>     Employee[] staff = managers; // OK
>     staff[0] = new Manager("Harry Hacker", 50000, 1989, 10, 1);
>   }
> ```

### 理解方法调用

下面假设要调用x.f(args)，隐式参数x声明为类C的一个对象。下面是调用过程的详细描述：

1. 编译器査看对象的声明类型和方法名。编译器将会一一列举所有C类中名为f的方法和其超类中访问属性为public(可访问到的)且名为f的方法。
2. 编译器査看调用方法时提供的参数类型。如果在所有名为f的方法中存在一个与提供的参数类型完全匹配，就选择这个方法。这个过程被称为重载解析(overloading resolution)。
   由于允许类型转换(int可以转换成double,Manager可以转换成Employee等等)，所以这个过程可能很复杂。如果编译器没有找到与参数类型匹配的方法，或者发现经过类型转换后有多个方法与之匹配，就会报告一个错误。

> - 当父子类具有相同签名的方法时，子类的返回值需可以向上转型为父类的返回值类型否则会报错，这两个父子类的方法叫做具有可协变的返回类型
> - 在覆盖一个方法的时候，子类方法不能低于超类方法的可见性

3. 如果是private方法、static方法、final方法或者构造器，那么编译器将可以准确地知道应该调用哪个方法，我们将这种调用方式称
   为静态绑定(static binding)。与此对应的是，调用的方法依赖于隐式参数的实际类型，并且在运行时实现动态绑定。在我们列举的示例中，编译器采用动态绑定的方式生成一条调用f(String)的指令。

4. 采用动态绑定调用方法时，虚拟机调用与x所`引用对象的实际类型`最合适的那个类的方法。假设x的实际类型是D，它是C类的子类。如果D类定义了方法f(String)，就直接调用它；否则，将在D类的超类中寻找f(String)，以此类推。

   > 虚拟机预先为每个类创建了一个方法表(method table),其中列出了所有方法的签名和实际调用的方法。这样一来，在真正调用方法的时候，虚拟机仅查找这个表就行了。在前面的例子中，虚拟机搜索D类的方法表，以便寻找与调用f(Sting)相匹配的方法。这个方法既有可能是D.f(String),也有可能是C.f(String)。这里需要提醒一点，如果调用super.f(param), 编译器将对隐式参数超类的方法表进行搜索。
<div name="dynamicbinding"></div>
> e声明为Employee类型。Employee类只有一个名叫getSalary的方法，这个方法没有参数。因此，在这里不必担心重载解析的问题。由于getSalary不是private 方法、static方法或final方法，所以将采用动态绑定。虚拟机为Employee和Manager 两个类生成方法表。在Employee 的方法表中，列出了这个类定义的所有方法：
> Employee:
> getName() -> Employee.getName()
> getSalary() -> Employee.getSalary()
> getHireDay() -> Employee.getHireDay()
> raiseSalary(double) -> Employee.raiseSalary(double)
> !！ 略去从Object超类继承的方法
> Manager方法表稍微有些不同。其中有三个方法是继承而来的，一个方法是重新定义的，
> 还有一个方法是新增加的。
> Manager:
> getName() -> Employee.getName()
> getSalary() -> Manager.getSalary()
> getHireDay() -> Employee.getHireDay()
> raiseSalary(double) -> Employee.raiseSalary(double)
> setBonus(double) -> Manager.setBonus(double)
> 
> 在运行时，调用e.getSalary()的解析过程为：
> 
> 1. 首先，虚拟机提取e的实际类型的方法表。即Manager的方法表。
> 
> 2. 接下来，虚拟机搜索定义getSalary签名的类。此时，虚拟机已经知道应该调用哪个方法。
> 3. 最后，虚拟机调用方法。

### 阻止继承:final类和方法

修饰

- 类:无法被继承
- 方法:无法覆盖
- 域:无法修改值(但对象可以修改对象内的值)

> 如果将一个类声明为final，其中的方法自动地成为final,域不是。

### 内联调用

如果一个方法没有被覆盖并且很短，编译器就能够对它进行优化处理，这个过程为称为内联(inlining)。例如，内联调用e.getName()将被替换为访问e.name域。这是一项很有意义的改进，这是由于CPU在处理调用方法的指令时，使用的分支转移会扰乱预取指令的策略，所以，这被视为不受欢迎的。然而，如果getName在另外一个类中被覆盖，那么编译器就无法知道覆盖的代码将会做什么操作，因此也就不能对它进行内联处理了。

虚拟机中的即时编译器比传统编译器的处理能力强得多。这种编译器可以准确地知道类之间的继承关系，并能够检测出类中是否真正地存在覆盖给定的方法。如果方法很简短、被频繁调用且没有真正地被覆盖，那么即时编译器就会将这个方法进行内联处理。如果虚拟机加载了另外一个子类，而在这个子类中包含了对内联方法的覆盖，那么将会发生什么情况呢？优化器将取消对覆盖方法的内联。这个过程很慢，但却很少发生。

### 强制类型转换

类似于基本类型的强制转换，对象之间也存在由父类向子类进行的强制转换。但当转换失败时Java会产生一个ClassCastException

```java
Manager boss = (Manager) staff[1] ; // Error null无法强制转换
```

因此需在在进行类型转换之前，先查看一下是否能够成功地转换。】

```java
if (staff[1] instanceof Manager)
{
   boss = (Manager) staff[1]；// 父类不是子类的实例 但子类是父类的实例
}
```

### 抽象类

在继承层次中，位于上层的类更具有通用性，可能更加抽象。从某种角度看，祖先类更加通用，人们只将它作为派生其他类的基类，而不作为想使用的特定的实例类。如学生Student和雇员Employee都是人，且共有一些诸如姓名这样的属性，所以可以将人这个类抽象为最顶层的祖先类。另外再增加一个getDescription()方法，它可以返回对一个人的简短描述，分别在Employee类和Student类中实现。但是Person 类除了姓名之外，一无所知。当然，所以可以让Person使用abstract关键字，而实现这个方法。

```java
public abstract String getDescription();
// no implementation required
```

> - 抽象类可以没有抽象方法
> - 有抽象方法的类一定是抽象类
> - 抽象类可以有实例方法与域
> - 抽象类不能被实例化。
>   new Person("Vinee Vu")是错误的
> - 可以定义一个抽象类的对象变量， 但是它只能引用非抽象子类的对象。
>   Person p = new Student("Vinee Vu", "Economics") ;
>
> 当调用p.getDescription()时 虽然Person没有定义该方法 但是引用的对象Student类实现了该方法且由于动态绑定的关系 调用的是Student类的方法 所以不会有问题

## Object:所有类的超类

Object类是Java 中所有类的始祖，在Java 中每个类都是由它扩展而来的，但不需要显式继承

> 所有的数组类型，不管是对象数组还是基本类型数组都扩展了Object类。
>
> ```java
> Employee[] staff = new Employee[10];
> obj = staff; // OK
> obj = new int[10]; // OK
> ```
>
> 

```java
public class Object {

    private static native void registerNatives();
    static {
        registerNatives();
    }
    public final native Class<?> getClass();
    public native int hashCode();
    public boolean equals(Object obj) {
        return (this == obj);
    }
    protected native Object clone() throws CloneNotSupportedException;
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
    public final native void notify();
    public final native void notifyAll();
    public final native void wait(long timeout) throws InterruptedException;
    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }
    public final void wait() throws InterruptedException {
        wait(0);
    }
    protected void finalize() throws Throwable { }
}
```



### equals方法

Object 类中的equals方法用于检测一个对象是否等于另外一个对象。在Object类中，这个方法将判断两个对象是否具有相同的引用。如果两个对象具有相同的引用，它们一定是相等的。多数情况下这样没有意义.

```java
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
```

> 为了防备name或hireDay可能为null的情况，需要使用Objects.equals方法。
>
> 在子类中定义equals方法时，首先调用超类的equals。如果检测失败，对象就不可能相等。如果超类中的域都相等，再比较子类中的实例域。
>
> ```java
> public boolean equals(Object otherObject) {
>     if (!super.equals(otherObject)) {
>       return false;
>     }
> // super.equals checked that this and otherObject belong to the same class
>     Manager other = (Manager) otherObject;
>     return bonus == other.bonus;
> ```

### 相等测试与继承

在前面的例子中，如果发现隐式参数和显式参数的类不匹配，equals方法就返冋false。有时候有些人喜欢用instanceof进行判断

```java
if (!(otherObject instanceof Employee)) return false;
```

当这样处理时没有考虑到otherObject为Employee子类的情况，会出现问题

**Java 语言规范要求equals方法具有下面的特性：**

> 1. 自反性：对于任何非空引用x, x.equals(x)应该返回true
> 2. 对称性：对于任何引用x和y, 当且仅当y.equals(x)返回true, x.equals(y)也应该返回true。
> 3. 传递性：对于任何引用x、y 和z, 如果x.equals(y)返回true，y.equals(z)返回true,x.equals(z)也应该返回true。
> 4. 一致性：如果x和y引用的对象没有发生变化，反复调用x.equals(y) 应该返回同样的结果。
> 5. 对于任意非空引用x,x.equals(null) 应该返回false

**使用instance进行比较的坏处**

> 请看下面这个调用：
> e.equals(m)
> 这里的e是一个Employee对象，m是一个Manager对象，并且两个对象具有相同的姓名、薪水和雇佣日期。如果在Employee.equals中用instanceof进行检测，则返回true。对称性要求反过来调用：m.equals(e)也需要返回true，不允许返回false，或者抛出异常。这就使得Manager类受到了束缚。这个类的equals 方法必须能够用自己与任何一个Employee 对象进行比较，而不考虑经理拥有的那部分特有信息！

**使用getClass进行比较的坏处**

> 利用getClass检测不符合置换原则
>
> AbstractSet类的equals方法用于检测两个集合是否有相同的元素。AbstractSet类有两个具体子类：TreeSet和HashSet, 它们分别使用不同的算法实现查找集合元素的操作。无论集合采用何种方式实现，都需要拥有对任意两个集合进行比较的功能然而，理论上应该将AbstractSet.equals声明为final,因为对于任何子类其对于比较元素所包含的意思是一致的（事实上，这个方法并没有被声明为final。这样做，可以让子类选择更加有效的算法对集合进行是否相等的检测）

**因此需要对于不同的情境设计不同的检测方式**

> - 如果子类能够拥有自己的相等概念，不同子类不一致 所以需要采用getClass保证类型一致
>
> - 如果由超类决定相等的概念，那么就可以使用instanceof进行检测，这样可以在同一父类下的不同子类的对象之间进行相等的比较。
>
> 在雇员和经理的例子中,如果两个Manager对象所对应的姓名、薪水和雇佣日期均相等，而奖金不相等，就认为它们是不相同的，因为需要用到bonus这个Manager独有域，所以可以使用getClass 检测。
>
> 但是，假设使用雇员的ID作为相等的检测标准，这个域为所有子类所共有，就可以使用instanceof 进行检测，并应该将Employee.equals声明为final。

**一个完美的equals方法编写建议：**

> - 显式参数命名为otherObject, 稍后需要将它转换成另一个叫做other的变量。
>
> - 检测this与otherObject是否引用同一个对象：if (this == otherObject) return true;
>
>   这条语句只是一个优化，比一个一个地比较类中的域所付出的代价小得多。
>
> - 检测otherObject是否为null, 如果为null, 返回false。if (otherObject == null ) return false;
>
> - 比较this与otherObject是否属于同一个类。
>
>   如果equals 的语义在每个子类中有所改变，就使用getClass检测：if (getClass() != otherObject.getClass()) return false;
>
>   如果所有的子类都拥有统一的语义，就使用instanceof检测：if (!(otherObject instanceof ClassName)) return false;
>
> - 将otherObject 转换为相应的类型变量：ClassName other = (ClassName) otherObject
>
> - 现在开始对所有需要比较的域进行比较了。使用==比较基本类型域，使用equals 比较对象域,使用静态的Arrays.equals方法检测数组类型相应的数组元素是否相等。如果所有的域都匹配，就返回true;否则返回false。
>   return field == other.field
>   && Objects.equals(field2, other.field2)
>
> - 如果在子类中重新定义equals,就要在其中包含调用super.equals(other)。

警告：下面是实现equals 方法的一种常见的错误。可以找到其中的问题吗？

```java
public class Employee
{
	public boolean equals(Employee other)// Object的equals类显式参数为Object类型 所以该方法其实是覆写了一个方法而不是重载 通过@Override注解防止出错
	{
		return other != null
		&& getClassO == other.getClass()
		&& Objects.equals(name, other.name)
		&& salary == other.salary
		&& Objects.equals(hireDay, other.hireDay);
    }
}
```

### hashcode方法

散列码(hash code)是由对象导出的一个整型值。散列码是没有规律的。如果x和y是两个不同的对象，x.hashCode()与y.hashCode()基本上不会相同。

下表中为几个通过调用String类的hashCode方法得到的散列码。

| 字符串 |   散列码    |
  | :----: | :---------: |
  | Hello  |  69609650   |
  | Harry  |  69496448   |
  | Hacker | -2141031506 |

String类使用下列算法计算散列码：

```java
int hash = 0;
for (int i = 0; i < length()；i ++)
	hash = 31 * hash + charAt(i) ;
```

由于hashCode方法定义在Object类中，因此每个对象都有一个默认的散列码，其值为对象的`存储地址`。来看下面这个例子。

```java
String s = "Ok";
StringBuilder sb = new StringBuilder(s) ;
System.out.println(s.hashCode() + " " + sb.hashCode());
String t = new String("Ok");
StringBuilder tb = new StringBuilder(t)；
System.out.println(t.hashCode() + " " + tb.hashCode()) ;
```
| 对象 |  散列码  |
| :--: | :------: |
|  s   |   2556   |
|  sb  | 20526976 |
|  t   |   2556   |
|  tb  | 20527144 |

s和t散列值一致是因为String覆写了hashCode方法所以内容一致所以hashcode也一致，而StringBuilder没有，所以默认为存储地址

例如，下面是Employee类的hashCode方法。

```java
public class Employee
  {
  public int hashCode()
  {
      return 7 * name.hashCode()
          + 11 * new Double(salary).hashCode() // 创建了新的Double对象，用Double.hashCode(salary)避免
          + 13 * hireDay.hashCode();// hireDay对象可能为null,用Objects,hashCode(hireDay)代替 默认为0
  }
    
  // 更简写法
  public int hashCode()
  {
      return Objects.hash(name, salary, hireDay);
  }
}
```

**hashCode计算注意事项**

> - equals与hashCode的定义必须一致：如果x.equals(y) 返回true, 那么x.hashCode()就必须与y.hashCode() 具有相同的值。
> - 如果存在数组类型的域，那么可以使用静态的Arrays.hashCode方法计算一个数组的散列码，这个散列码由数组元素的散列码类似于字符串乘以一个权重以后累加得到。

### toString方法

toString方法用于返回表示对象值的字符串。

绝大多数（但不是全部）的toString 方法都遵循这样的格式："类名[域名=值,域名=值...]"如Point类的toString方法将返回
"java.awt.Point[x=10，y=20]"

下面是Employee 类中的toString 方法的实现：

```java
public String toString()
{
    return "Employee[name=" + name
        + ",salary=" + salary
        + ",hireDay=" + hireDay
        + "]"
}
  // 实际上，还可以设计得更好一些。最好通过调用getClass().getName() 获得类名的字符串，而不要将类名硬加到toString 方法中。
public String toString
{
    return getClass().getName()
        + "[name=" + name
        + ",salary=" + salary
        + ",hireDay=" + hireDay
        + "]";
}

  // 如果超类使用了getClass().getName(),因为动态调用的关系，实际显示的是子类的类名, 那么子类只要调用super.toString()就可以了
public class Manager extends Employee
{
    public String toString()
    {
        return super.toString()
            + "[bonus=" + bonus
            + "]";
    }
}
```

> - 只要对象与一个字符串通过操作符"+"连接起来，Java编译就会自动地调用toString方法，以便获得这个对象的字符串描述。
>
> - System.out.println(x);方法就会直接地调用x.toString()

默认情况下Object定义的toString方法，会打印输出对象所属的类名和散列码。例如，调用System.out.println(System.out)将输出下列内容：
java.io.PrintStream@2f6684

同理打印数组对象也会输出一串恼人的字符串"[I@la46e30"（前缀[I表明是一个整型数组）。修正的方式是调用静态方法Arrays.toString。代码：

```java
String s = Arrays.toString(luckyNumbers);// “[2,3,5,7,11,13]”
// 要想打印多维数组则需要调用Arrays.deepToString方法。
```

## 泛型数组列表

为了解决数组初始化后无法修改大小的问题，Java使用另外一个被称为ArrayList的类。它使用起来有点像数组，但在添加或删除元素时，具有自动调节数组容量的功能。

ArrayList 是一个采用类型参数(type parameter) 的泛型类(generic class)。为了指定数组列表保存的元素对象类型，需要用一对尖括号将类名括起来加在后面，例如，ArrayList<Employee>。

下面声明和构造一个保存Employee 对象的数组列表：

```java
ArrayList<Employee> staff = new ArrayList<Employee>(); //Java SE 7中，可以省去右边的类型参数
ArrayList<Employee> staff = new ArrayList()；// 此时没有给出类型 内部数组会是一个Object数组 且add时不做类型检查
```

如果赋值给一个变量，或传递到某个方法，或者从某个方法返回，编译器会检査这个变量、参数或方法的泛型类型，然后将这个类型放在o中。

**添加元素 add**

使用add 方法可以将元素添加到数组列表中。

```java
staff.add(new Employee("Harry Hacker",...));
staff.add(new Eraployee("Tony Tester",...));
staff.add(n,e) ;// 在索引的位置插入对象e
```

数组列表管理着对象引用的一个内部数组。最终，如果调用add且内部数组已经满了，数组列表就将自动地创建一个更大的数组，并将所有的对象从较小的数组中拷贝到较大的数组中。

**给予数组列表初始容量的办法**

```java
staff.ensureCapacity(1OO);
ArrayList<Employee> staff = new ArrayList<>(1OO);
```

**减少无谓占用的方法**
trimToSize 方法。这个方法将存储区域的大小调整为当前元素数量所需要的存储空间数目。垃圾回收器将回收多余的存储空间。

### 访问数组列表元素

**设置第i个元素**

```java
staff.set(i,harry); // 只有i小于或等于数组列表的大小时，才能够调用，只能替换已经存在的元素不能新增
```

**获得数组列表的元素**

```java
Employee e = (Employee) staff.get(i);//当初始化时没有泛型类时，get返回的是Object需要强制类型转换
```

**列表转数组**

```java
T[] a = new T[list.size];
list.toArray(a);
```

**从数组列表删除一个元素**

```java
Employee e = staff.remove(n);// 删除位于索引n的元素
```

**循环遍历**

```java
for (Employee e : staff){
    // dosomething with e
}
```

### 类型化与原始数组列表的兼容性

在你自己的代码中， 你可能更愿意使用类型参数来增加安全性。这一节中，你会了解如
何与没有使用类型参数的遗留代码交互操作。
假设有下面这个遗留下来的类：

```java
public class EmployeeDB
{
    public void update(ArrayList list){ . . . };
    public ArrayList find(String query) { . . . };
}

ArrayList<Employee> result = employeeDB.find(query);// Raw use of parameterized class 'ArrayList'. Unchecked assignment: 'java.util.ArrayList' to 'java.util.ArrayList<code.Employee>'. Provide the parametrized type for this generic.
ArrayList<Employee> result = (ArrayList<Employee>) employeeDB.find(query);//Unchecked cast: 'java.util.ArrayList' to 'java.util.ArrayList<code.Employee>'. Remove this unnecessary cast to "ArrayList". 

// 因为update可能会往list里添加非Employee 而在find返回的原始数组列表操作元素时可能对非Employee元素进行操作存在危险 编译不会出错但是在编写代码时需要注意
```

## 对象包装器与自动装箱

所有的基本类型都有一个与之对应的类。Integer、Long、Float、Double、Short、Byte、Character、Void和Boolean(前6个派生于公共的超类Number)。对象包装器类是不可变的，一旦构造了包装器，就不允许更改包装在其中的值。同时，对象包装器类还是final, 因此不能定义它们的子类。

整型数组列表的类型参数不允许是基本类型，不允许写成ArrayList<int>。因此应用包装类声明

```java
ArrayList<Integer> list = new ArrayList<>()；
```

### 自动装箱

```java
list.add(3);
//将自动地变换成对应的包装类
list.add(Integer.value0f(3)) ;
```

### 自动拆箱

```java
int n = list.get(i);
// 翻译成
int n = list.get(i).intValue();
```

### 包装类缓存
自动装箱规范要求boolean、byte、char<=127， 介于-128 ~ 127 之间的short和int被包装到固定的对象中(通过自动装箱或者调用class.valueOf())，意思是在这个范围内返回的包装类对象为同一个对象

```java
Integer i1 = 1;
Integer i2 = 1;
System.out.print(i1 == i2) // true
```

**注意事项**

> 1. 首先， 由于包装器类引用可以为null , 所以自动装箱有可能会抛出一个NullPointerException 异常：
>
>    ```java
>    Integer n = null;
>    System.out.println(9 * n); // Throws NullPointerException
>    ```
>
> 2. 另外，如果在一个条件表达式中混合使用包装类型，比如Integer和Double类型，Integer 值就会拆箱，提升为double, 再装箱为Double(具体提升规则见ChapterOne - 特殊运算符说明)
>
>    ```java
>    Integer n = 1;
>    Double x = 2.0;
>    System.out.println(true ? n : x); // Prints 1.0
>    ```
>
> 3. 装箱和拆箱是在编译器搞定的。编译器在生成类的字节码时，插入必要的方法调用。虚拟机只是执行这些字节码。
>
> 4. ```java
>    public static void triple(Integer x) 
>    {
>        x = 3 * x; // modifies local variable。Integer 对象是不可变的，包含在包装器中的内容不会改变:
>    }
>       
>    public static void triple(IntHolder x)
>    {
>       x.value = 3 * x.value;// 会修改x中的值
>    }
>    ```

## 参数数量可变的方法

```java
System.out.printf("%d", n);
System.out.printf("%d %s", n, "widgets");
```

在两条语句中，尽管包含不同的参数数量，但它们调用的都是同一个方法。该方法定义如下

```java
public class PrintStream
{
    public PrintStream printf(String fmt , Object... args) 
    { 
        return format(fmt, args); 
    }
}
```

省略号`...`是Java代码的一部分，它表明这个方法除fmt参数之外可以接收任意数量的对象,该可变参数会变成Object[] 数组

```java
public static double max(double... values)
{
    double largest = Double.NEGATIVEINFINITY;
    for (double v : values) 
        if (v > largest) 
            largest = v;
    return largest;
}

可以像下面这样调用这个方法：
double m = max(3.1, 40.4, -5);
// 也可以将数组整体传进去
double m = max(new double[] {3.1, 40.4, -5});
```

> main方法也可以声明为下列形式：
>
> ```java
> public static void main(String... args)
> ```
>
> 

## 枚举类

```java
public abstract class Enum<E extends Enum<E>>
        implements Comparable<E>, Serializable {
    
    private final String name;

    public final String name() {
        return name;
    }

    private final int ordinal;

    public final int ordinal() {
        return ordinal;
    }

    protected Enum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public String toString() {
        return name;
    }

    public final boolean equals(Object other) {
        return this==other;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public final int compareTo(E o) {
        Enum<?> other = (Enum<?>)o;
        Enum<E> self = this;
        if (self.getClass() != other.getClass() && // optimization
            self.getDeclaringClass() != other.getDeclaringClass())
            throw new ClassCastException();
        return self.ordinal - other.ordinal;
    }

    @SuppressWarnings("unchecked")
    public final Class<E> getDeclaringClass() {
        Class<?> clazz = getClass();
        Class<?> zuper = clazz.getSuperclass();
        return (zuper == Enum.class) ? (Class<E>)clazz : (Class<E>)zuper;
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType,
                                                String name) {
        T result = enumType.enumConstantDirectory().get(name);
        if (result != null)
            return result;
        if (name == null)
            throw new NullPointerException("Name is null");
        throw new IllegalArgumentException(
            "No enum constant " + enumType.getCanonicalName() + "." + name);
    }

    protected final void finalize() { }

    private void readObject(ObjectInputStream in) throws IOException,
        ClassNotFoundException {
        throw new InvalidObjectException("can't deserialize enum");
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("can't deserialize enum");
    }
}

```

读者在第3章已经看到如何定义枚举类型。下面是一个典型的例子：

```java
public enum Size { SMALL, MEDIUM, LARGE, EXTRALARGE };
```

实际上，这个声明定义的类型是一个类，它刚好有4个`实例`，在此尽量不要构造新对象。因此，在比较两个枚举类型的值时，永远不需要调用equals, 而直接使用“==” 就可以了。
可以在枚举类型中添加一些构造器、方法和域。当然， 构造器只是在构造枚举常量的时候被调用。下面是一个示例：

```java
public enum Size
{
    SMALL("S"),
    MEDIUM("M"),
    LARGE("L"),
    EXTRA_LARGE("XL");
    
    private String abbreviation;// 其实应该是final
    
    private Size(String abbreviation) 
    { 
        this.abbreviation = abbreviation; 
    }
    
    public String getAbbreviation() 
    { 
        return abbreviation; 
    }
}
```

所有的枚举类型都是Enum 类的子类。它们继承了这个类的许多方法。

```java
//toString 返回枚举常量名
Size.SMALL.toString()将返回字符串“SMALL”。
//Enum.valueOf toString()的逆方法 返回常量名对应的枚举
Size s = Enum.valueOf(Size.class, "SMALL"); //Size.SMALL
//values方法 返回一个包含全部枚举值的数组。
Size[] values = Size.values();
//ordinal方法 返冋enum声明中枚举常量的位置 从0开始计数
Size.MEDIUM.ordinal() // 1
```

> ?没懂
>
> 如同Class 类一样， 鉴于简化的考虑，Enum 类省略了一个类型参数。例如，实际上，应该将枚举类型Size广展为Enum<Size>。类型参数在compareTo 方法中使用
> ( comPareTo 方法在第6 章中介绍， 类型参数在第8 章中介绍）。

## 反射

能够分析类能力的程序称为反射(reflective)。反射机制可以用来：

- 在运行时分析类的能力。

- 在运行时查看对象，例如，编写一个toString方法供所有类使用。

- 实现通用的数组操作代码。

- 利用Method对象。

### Class类

在程序运行期间，系统始终为所有的对象维护一个被称为运行时的类型标识。这个信息跟踪着每个对象所属的类。虚拟机利用运行时类型信息选择相应的方法执行。然而，可以通过专门的Java类访问这些信息。保存这些信息的类被称为Class,这个名字很容易让人混淆。Object类中的getClass() 方法将会返回一个Class 类型的实例。

```java
Employee e;
Class cl = e.getClass();
```

**getName**

返回类的名字

```java
Random generator = new Random():
Class cl = generator.getClass() ;
String name = cl.getName(); // name is set to "java.util.Random"
```

**forName**

通过类名获得对应的Class对象。

```java
String className = "java.util.Random";
Class cl = Class.forName(className);
//只有在className是类名或接口名时才能够执行。否则，forName方法将抛出一个checkedexception(已检查异常)
```

**.class**

获得Class类对象的第三种方法非常简单。如果T是任意的Java类型（或void 关键字)，T.class 将代表匹配的类对象。

```java
Class cl = Random.class; // if you import java.util
Class cl2 = int.class; // int
Class cl3 = Double[].class; // [Ljava.lang.Double
Class cl3 = int[].class; // [I
Class cl3 = double[].class; // [D
```

> 一个Class 对象实际上表示的是一个类型，而这个类型未必一定是一种类。例如，int不是类，但int.class 是一个Class 类型的对象。

**==**

可以直接用==运算符实现两个类对象比较的操作。
if (e.getClass() == Employee.class) ...

**newlnstance()**

用来动态地创建一个类的实例
e.getClass().newlnstance();
创建了一个与e具有相同类类型的实例。newInstance方法只能调用默认的无参构造器初始化新创建的对象。如果没有，就会抛出一个异常。

将forName与newInstance配合起来使用，可以根据存储在字符串中的类名创建一个对象

```java
String s = "java.util.Random";
Object m = Class.forName(s).newInstance();
```

**Constructor类中的newInstance方法**

可以调用有参构造器初始化对象

```java
Class clazz = Employee.class;
Constructor<Employee> constructor = clazz.getConstructor(String.class, double.class, int.class,
        int.class, int.class);// 获取指定参数类型的构造类
Employee a = constructor.newInstance("1", 1.0, 1, 1, 1); //使用构造器初始化出对应对象
```

### 利用反射能力分析类的能力

在java.lang.reflect包中有三个类Field、Method和Constructor分别用于描述类的域、方法和构造器。这三个类都有一个叫做getName的方法，用来返回对应项目的名称。Field类有一个getType方法，用来返回描述域所属类型的Class对象。Method.getParameterTypes和Constructor.getParameterTypes类能够报告入参类型，Method.getReturnType类可以报告返回类型。这2个类还有一个叫做getModifiers的方法，它将返回一个整型数值，用不同的二进制位描述public和static这样的修饰符使用状况。另外，还可以利用Modifier类的静态方法分析getModifiers返回的整型数值，其中的isPublic、isPrivate 或isFinal判断方法或构造器是否是public、private 或final。Modifier.toString打印修饰符。

Class 类中的getFields、getMethods和getConstructors方法将分别返回类提供的public域、方法和构造器数组，其中包括超类的公有成员。Class 类的getDeclareFields、getDeclareMethods 和getDeclaredConstructors方法将分别返回类中声明的全部域、方法和构造器，其中包括私有和受保护成员，但`不`包括超类的成员。

### 在运行时使用反射分析对象

**Field.get()**

获取包含该域的对象的当前值

```java
Employee harry = new Employee("Harry Hacker", 35000, 10, 1, 1989);
Class cl = harry.getClass()；
// the class object representing Employee
Field f = cl.getDeclaredField("name");
// the name field of the Employee class

f.setAccessible(true);
// 私有域访问权限
Object v = f.get(harry);
// the value of the name field of the harry object, i.e., the String object "Harry Hacker"
// 实际上，这段代码存在一个问题。由于name是一个私有域，所以get方法将会抛出一个IllegalAccessException。但只有利用get方法才能得到可访问域的值。除非拥有访问权限，否则Java安全机制只允许査看任意对象有哪些域，而不允许读取它们的值。反射机制默认受限于Java的访问控制。为了覆盖访问控制，需要调用Field、Method或Constructor 对象的setAccessible方法。
f.setAccessible(true); 
//setAccessible 方法是AccessibleObject 类中的一个方法，它是Field、Method 和Constructor类的公共超类。这个特性是为调试、持久存储和相似机制提供的。get方法还有一个需要解决的问题。name域是一个String,因此把它作为Object 返回没有什么问题。但是，假定我们想要查看salary域。它属于double类型，而Java中数值类型不是对象。要想解决这个问题，可以使用Field类中的getDouble方法，也可以调用get方法，此时，反射机制将会自动地将这个域值打包到相应的对象包装器中，这里将打包成Doubleo当然，可以获得就可以设置。
```

**f.set(obj，value)**

设置obj f域的值为value

> **应用场景**
>
> 可以利用运行时反射分析编写通用的toString方法 但是要注意解决循环引用无限递归的问题，原书中使用ArrayList存储已经递归过的对象来去重。

### 使用反射编写泛型数组代码

java.lang.reflect 包中的Array类允许动态地创建数组。

**Array.copyOf**

```java
Employee[] a = new Employee[100]:
// array is full
a = Arrays.copyOf(a, 2 * a.length);
// 这段代码返回的数组类型是对象数组(Object[])类型，而一个对象数组不能转换成雇员数组(Employee[])[ArrayList<Employee> aaa = (ArrayList<Employee>) new ArrayList()这样强转可以].Java 数组会记住每个元素的类型，即创建数组时new表达式中使用的元素类型（譬如Employee[] employees = new Manager[10]; 则元素类型为Manager）。将一个Employee[] 临时地转换成Object[]，然后再把它转换回来是可以的，但一从开始就是Object[]的数组却永远不能转换成Employe[]
public static Object[] badCopyOf(Object[] a, int newLength){ // not useful
    Object[] newArray = new Object[newlength]:
    System.arraycopy(a, 0, newArray, 0, Math.min(a.length, newLength)) ;
    return newArray;
}

public static Object goodCopyOf(Object a, int newLength)
{
    Class cl = a.getClass();
    if (!cl.isArray()) return null;
    Class componentType = cl.getComponentType();
    int length = Array.getLength(a);
    Object newArray = Array.newInstance(componentType, newLength);
    System.arraycopy(a, 0, newArray, 0, Math.min(length, newLength));
    return newArray;
}
```

### 调用任意方法

**Method.invoke**

调用包装在当前Method对象中的方法。invoke 方法的签名是：

```java
Object invoke(Object obj, Object... args)// 调用静态方法时 obj置为null
```

**Method.getMethod**

```java
Method getMethod(String name, Class... parameterTypes)//方法名和入参类型作为方法签名两者缺一不可
// 参数类型double.class和Double.class完全不一样
```

## 继承的设计技巧

1. 将公共操作和域放在超类

2. 不要使用受保护的域
  第一，子类集合是无限制的，任何一个人都能够由某个类派生一个子类，并编写代码以直接访问protected的实例域，从而破坏了封装性。第二，在Java 程序设计语言中，在同一个包中的所有类都可以访问proteced域，而不管它是否为这个类的子类。

3. 使用继承实现“is-a” 关系

  但要记住不能乱用。父类应是子类的共集，域和方法保有一致性。譬如钟点工有计时工资但没有固定工资，并不是一种特殊的雇员

4. 除非所有继承的方法都有意义，否则不要使用继承

5. 在覆盖方法时，不要改变预期的行为(？没懂 是指某个方法在被覆写之后要和原方法保持连贯性？)
  在覆盖一个方法的时候，不应该毫无原由地改变行为的内涵。就这一点而言，编译器无法提供任何帮助，因为它不会检查重新定义的方法是否有意义。例如，可以重定义Holiday类中add方法“修正”原方法的问题，或什么也不做，或抛出一个异常，或继续到下一个假日。然而这些都违反了置换原则。比如以下语句，不管x属于Calendar类，还是属于Holiday类，执行上述语句后都应该得到预期的行为。所以Holiday不覆盖add方法。

  ```java
  int d1 = x.get(Calendar.DAY_OF_MONTH) ;
  x.add(Calendar.DAY_OF_MONTH, 1);
  int d2 = x.get(Calendar.DAY_OF_MONTH) ;
  System.out.println(d2 - d1);
  ```

6. 使用多态，而非类型信息去区分
   无论什么时候，对于下面这种形式的代码

   ```java
   if (x is of type1)
   action1(x)
   else if (x is of type2)
   action2(x) ;
   ```

   action1与action2表示的是相同的概念吗？如果是相同的概念，就应该为这个概念定义一个方法，并将其放置在两个类的超类或接口中，然后，就可以调用X.action()通过多态性提供的动态分派机制执行相应的动作。

7. 不要过多地使用反射
  反射是很脆弱的，其不确定性导致编译器很难帮助人们发现程序中的错误，因此只有在运行时才发现错误并导致异常。