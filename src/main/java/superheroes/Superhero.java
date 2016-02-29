package superheroes;

public class Superhero {
  public final String name;

  public Superhero(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Superhero{" +
        "name='" + name + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Superhero superhero = (Superhero) o;

    return !(name != null ? !name.equals(superhero.name) : superhero.name != null);

  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
