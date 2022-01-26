package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

class Arraytest{
  int a[] = new int[6];
  public static void main ( String arg[] ) throws FileNotFoundException {
    Float f1 = 1.0f -0.9f;
    Float f2 = 0.9f -0.8f;
    BigDecimal bigDecimal = new BigDecimal(f1);
    BigDecimal bigDecimal1 = new BigDecimal(f2);
    System.out.println(".equals()");
  }
}