package controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import superheroes.Superhero;
import superheroes.Superhero.SuperheroImagination;
import superheroes.SuperheroCollection;
import webserver.Reply;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class AddSuperheroController {

  private static final List<String> EMPTY_LIST = Collections.emptyList();
  private final SuperheroCollection superheroCollection;

  public AddSuperheroController(SuperheroCollection superheroCollection) {
    this.superheroCollection = superheroCollection;
  }

  public AddSuperheroController() {
    this(new SuperheroCollection());
  }

  @RequestMapping("/new/superhero")
  public Reply.Status newSuperhero(@RequestParam(value = "name", required = true, defaultValue = "") String name,
                                   @RequestParam(value = "pseudonym", required = false, defaultValue = "") String pseudonym,
                                   @RequestParam(value = "skills", required = false, defaultValue = "") String skills,
                                   @RequestParam(value= "allies", required = false, defaultValue = "") String allies,
                                   @RequestParam(value = "publisher", required = false, defaultValue = "") String publisher,
                                   @RequestParam(value = "dataOfFirstAppearance", required = false, defaultValue = "") String dataOfFirstAppearance) {
    return superheroCollection.addNewSuperhero(new SuperheroImagination()
        .name(name)
        .withPseudonym(pseudonym)
        .withSkills(getListOf(skills))
        .withAllies(getListOf(allies))
        .withPublisher(publisher)
        .withDateOfFirstAppearance(getDateFromString(dataOfFirstAppearance))
        .create());
  }

  // TODO: move to utils class + use the same in CouchDbDatabases
  private Date getDateFromString(String dataOfFirstAppearance) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return dateFormat.parse(dataOfFirstAppearance);
    } catch (ParseException e) {
      return new Date(0);
      // TODO: Raise invalid parameter
    }
  }

  // TODO: move to utils class
  private List<String> getListOf(String propertiesString) {
    return propertiesString.equals("") ? EMPTY_LIST : Arrays.asList(propertiesString.split(",", -1));
  }

  @RequestMapping("/get/all")
  public List<Superhero> allSuperheroes() {
    return superheroCollection.getAll();
  }
}
