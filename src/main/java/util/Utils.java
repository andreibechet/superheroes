package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Utils {
  public static final List<String> EMPTY_LIST = Collections.emptyList();

  public static Date getDateFromString(String dataOfFirstAppearance) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      return dateFormat.parse(dataOfFirstAppearance);
    } catch (ParseException e) {
      return new Date(0);
    }
  }
}
