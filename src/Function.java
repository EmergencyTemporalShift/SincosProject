package src;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Function {
  public static float ppi = Util.ppi;
  @SuppressWarnings("unused")
  static float function(float theta) {
    return (float) (sin(4 * ppi*theta)
               -1/2f*cos(5 * ppi*theta));
           //+(1f/10)*cos(200*ppi*theta); //Extra high frequency waves
  }

  @SuppressWarnings("unused")
  public static float function(float theta, float ot) {
    return (float) (sin(4*ppi*theta));
                   //-1*cos((5+ot)*ppi*theta));
           //+(1f/10)*cos(200*ppi*theta); //Extra high frequency waves
  }
}
