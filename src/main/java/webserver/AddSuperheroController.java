package webserver;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddSuperheroController {

  @RequestMapping("/new/superhero")
  public Reply newSuperhero(@RequestParam(value = "name") String name) {
    return new Reply(Status.OkSuperheroAdded);
  }
}
