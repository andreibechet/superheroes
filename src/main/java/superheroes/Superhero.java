package superheroes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Superhero {
  public final String name;
  public final String pseudonym;
  public final String publisher;
  public final List<String> skills;
  public final List<String> allies;
  public final Date dateOfFirstAppearance;

  public Superhero(String name, String pseudonym, String publisher, List<String> skills,
                   List<String> allies, Date dateOfFirstAppearance) {
    this.name = name;
    this.pseudonym = pseudonym;
    this.publisher = publisher;
    this.skills = skills;
    this.allies = allies;
    this.dateOfFirstAppearance = dateOfFirstAppearance;
  }

  @Override
  public String toString() {
    return "Superhero{" +
        "name='" + name + '\'' +
        ", pseudonym='" + pseudonym + '\'' +
        ", publisher='" + publisher + '\'' +
        ", skills=" + skills +
        ", allies=" + allies +
        ", dateOfFirstAppearance=" + dateOfFirstAppearance +
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

  public static class SuperheroImagination {
    private String name = "";
    private String pseudonym = "";
    private String publisher = "";
    private List<String> skills = Collections.emptyList();
    private List<String> allies = Collections.emptyList();
    private Date dateOfFirstAppearance = new Date(0);

    public SuperheroImagination name(String name) {
      this.name = name;
      return this;
    }

    public SuperheroImagination withPseudonym(String pseudonym) {
      this.pseudonym = pseudonym;
      return this;
    }

    public SuperheroImagination withPublisher(String publisher) {
      this.publisher = publisher;
      return this;
    }

    public SuperheroImagination withSkills(List<String> skills) {
      this.skills = skills;
      return this;
    }

    public SuperheroImagination withAllies(List<String> allies) {
      this.allies = allies;
      return this;
    }

    public SuperheroImagination withDateOfFirstAppearance(Date dateOfFirstAppearance) {
      this.dateOfFirstAppearance = dateOfFirstAppearance;
      return this;
    }

    public Superhero create() {
      return new Superhero(this.name, this.pseudonym, this.publisher, this.skills, this.allies,
          this.dateOfFirstAppearance);
    }

  }
}
