package controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import superheroes.Superhero;
import superheroes.Superhero.SuperheroImagination;
import superheroes.SuperheroCollection;
import util.Utils;
import webserver.Reply;

import java.util.*;

@RestController
public class AddSuperheroController {

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
        .withDateOfFirstAppearance(Utils.getDateFromString(dataOfFirstAppearance))
        .create());
  }

  @RequestMapping("/update/superhero")
  public Reply.Status updateSuperhero(@RequestParam(value = "name", required = true) String name,
                                   @RequestParam(value = "pseudonym", required = false, defaultValue = "") String pseudonym,
                                   @RequestParam(value = "skills", required = false, defaultValue = "") String skills,
                                   @RequestParam(value= "allies", required = false, defaultValue = "") String allies,
                                   @RequestParam(value = "publisher", required = false, defaultValue = "") String publisher,
                                   @RequestParam(value = "dataOfFirstAppearance", required = false, defaultValue = "") String dataOfFirstAppearance) {
    return superheroCollection.updateSuperhero(new SuperheroImagination()
        .name(name)
        .withPseudonym(pseudonym)
        .withSkills(getListOf(skills))
        .withAllies(getListOf(allies))
        .withPublisher(publisher)
        .withDateOfFirstAppearance(Utils.getDateFromString(dataOfFirstAppearance))
        .create());
  }

  @RequestMapping("/delete/superhero")
  public Reply.Status deteleSuperhero(@RequestParam(value = "name", required = true) String name) {
    return superheroCollection.deleteSuperhero(new SuperheroImagination()
        .name(name)
        .create());
  }

  @RequestMapping("/get/all")
  public List<Superhero> allSuperheroes() {
    return superheroCollection.getAll();
  }

  private List<String> getListOf(String propertiesString) {
    return propertiesString.equals("") ? Utils.EMPTY_LIST : Arrays.asList(propertiesString.split(",", -1));
  }
}
