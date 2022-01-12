package core;

public class IdProvider {
  private static int id=0;
  public static int getId()
  {
    return id++;
  }
}
