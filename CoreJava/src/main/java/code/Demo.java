package code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Demo {

  public static void main(String[] args) {
    Collection<?>[] collections =
        {new HashSet<String>(), new ArrayList<String>(), new HashMap<String, String>().values()};
    Super subToSuper = new Sub();
    long test =012;
    for (Collection<?> collection : collections) {
      System.out.println(subToSuper.getType(collection));
    }
  }

  abstract static class Super {

    public String getType(Collection<?> collection) {
      return "Super:collection";
    }

    public static String getType(List<?> list) {
      return "Super: list";
    }

    public static String getType(Set<?> set) {
      return "Super:set";
    }

    public String getType(ArrayList<?> list) {
      return "Super: arrayList";
    }

    public String getType(HashSet<?> set) {
      return "Super: hashSet";
    }
  }

  static class Sub extends Super {

    public String getType(Collection<?> collection) {
      return "Sub:collection";
    }
  }
}