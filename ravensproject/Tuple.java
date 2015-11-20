package ravensproject;

/*
 * Usage:
 * Tuple<Integer, String, Double> Tuple = Tuple.createTuple(1, "test", 2.0);
 * Tuple.getElement0();
 * Tuple.getElement1();
 * Tuple.getElement2();
 * 
 * This class is immutable.
 */
public class Tuple<A, B, C> {

  private final A element0;
  private final B element1;
  private final C element2;

  public static <A, B, C> Tuple<A, B, C> createTuple(A element0, B element1, C element2) {
    return new Tuple<A, B, C>(element0, element1, element2);
  }

  public Tuple(A element0, B element1, C element2) {
    this.element0 = element0;
    this.element1 = element1;
    this.element2 = element2;
  }

  public A getElement0() {
    return element0;
  }

  public B getElement1() {
    return element1;
  }

  public C getElement2() {
    return element2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Tuple<?, ?, ?> tuple = (Tuple<?, ?, ?>) o;

    if (element0 != null ? !element0.equals(tuple.element0) : tuple.element0 != null)
      return false;
    if (element1 != null ? !element1.equals(tuple.element1) : tuple.element1 != null)
      return false;
    return !(element2 != null ? !element2.equals(tuple.element2) : tuple.element2 != null);

  }

  @Override
  public int hashCode() {
    int result = element0 != null ? element0.hashCode() : 0;
    result = 31 * result + (element1 != null ? element1.hashCode() : 0);
    result = 31 * result + (element2 != null ? element2.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append("(");
    builder.append(element0);
    builder.append(", ");
    builder.append(element1);
    builder.append(", ");
    builder.append(element2);
    builder.append(")");

    return builder.toString();
  }
}

