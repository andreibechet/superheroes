package hello;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static hello.GreetingController.GREETINGS_TEMPLATE;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GreetingControllerShould {

  public static final int FIRST_ID = 1;
  public static String USER = "User";
  private MockMvc mvc;

  @Before
  public void setup() {
    mvc = standaloneSetup(new GreetingController()).build();
  }

  @Test
  public void replyStatusIsOkForRequest() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String expectedValue = mapper.writeValueAsString(
        new Greeting(FIRST_ID, String.format(GREETINGS_TEMPLATE, USER)));

    mvc.perform(MockMvcRequestBuilders.get("/greeting?name=" + USER)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(content().string(expectedValue));

  }
}
