# ChapterFour 接口、lambda表达式与内部类

## 接口

### 接口概念

接口与类的区别在于 类表示某个东西是什么，接口则是这个东西能干什么，比如Employee是一个人所以可以继承Person类，作为一个人，他可以跑，跳，因此实现Action接口，其中包含jump, run等方法

Arrays 类中的sort 方法可以对对象数组进行排序，但要求满足下列前提：对象所属的类必须实现了Comparable接口。下面是Comparable接口的代码：

```java
// 当x小于y时，返回一个负数；当x等于y时，返回0;否则返回一个正数。
public interface Comparable<T>
{
    int compareTo(T other) ; // parameter has type T
}
```

> 接口中的所有方法自动地属于public。因此，在接口中声明方法时，不必提供关键字public。
>
> 接口不能含有实例域，但可以在接口中实现简单方法。
>
> 实现接口使用关键字`implements`
>
> 在实现接口中的方法时必须使用关键字`public`,不加或者使用其他关键字都是不可以的，覆写方法权限控制不得比原方法权限控制更严格

**为什么不能在Employee类直接提供一个compareTo方法，而必须实现Comparable接口呢？**

主要原因在于Java 程序设计语言是一种强类型(strongly typed)语言。在调用方法的时候，编译器将会检查这个方法是否存在。在sort方法中可能存在下面这样的语句：

```java
if (a[i].compareTo(a[j]) > 0)
{
    //rearrange a[i] and a[j]
}
```

为此，编译器必须确认a[i]一定有compareTo方法。如果a是一个Comparable对象的数组，就可以确保拥有compareTo方法，因为每个实现Comparable接口的类都必须提供这个方法的定义。

> 注释：有人认为，将Arrays类中的sort方法定义为接收一个Comparable[]数组就可以在使用元素类型没有实现Comparable接口的数组作为参数调用sort方法时，由编译器给出错误报告。但事实并非如此。在这种情况下，sort方法可以接收一个Object[]数组，并对其进行笨拙的类型转换：
>
> ```java
> // Approach used in the standard library not recommended
> if (((Comparable) a[i]).compareTo(a[j]) > 0)
> {
>     // rearrange a[i] and a[j]
> }
> ```
>
> 如果a[i]不属于实现了Comparable 接口的类，那么虚拟机就会抛出一个异常。

? 没懂 接口也可以强转？

> 同equals一致，compareTo也存在对称性的问题
>
> 1. 如果子类之间的比较含义不一样， 那就属于不同类对象的非法比较。每个compareTo方法都应该在开始时进行下列检测：
>    if (getClass()!= other.getClass()) throw new ClassCastException();
> 2. 如果存在这样一种通用算法，它能够对两个不同的子类对象进行比较，则应该在超类中提供一个compareTo方法，并将这个方法声明为final。

### 接口的特性

### 接口与抽象类

### 静态方法

### 默认方法

### 解决默认方法冲突