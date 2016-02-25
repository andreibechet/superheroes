package webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AddSuperheroShould {

  private static String SOME_SUPERHERO = "Iron Man";
  private MockMvc mvc;

  @Before
  public void setup() {
    mvc = standaloneSetup(new AddSuperheroController()).build();
  }

  @Test
  public void replyStatusIsOkForRequest() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String expectedValue = mapper.writeValueAsString(new Reply(Status.OkSuperheroAdded));

    mvc.perform(MockMvcRequestBuilders.get("/new/superhero?name=" + SOME_SUPERHERO)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string(expectedValue));
  }
}
