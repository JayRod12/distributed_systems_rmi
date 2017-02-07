
package common;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {

  public static void printSummary(int count, int total) {
    String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

    String missing = "<" + timeStamp + "> " + (total - count) + " messages out of " + total + " were lost.";
    System.out.println(missing);
  }

}
