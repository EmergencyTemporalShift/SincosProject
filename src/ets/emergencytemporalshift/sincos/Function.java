package src.ets.emergencytemporalshift.sincos;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

@SuppressWarnings("unused")
public class Function {
  final public static float PPI = Util.PPI;

  @SuppressWarnings("unused")
  static float function(float theta) {
    return (float) (sin(4 * PPI *theta)
               -1/2f*cos(5 * PPI *theta));
           //+(1f/10)*cos(200*ppi*theta); //Extra high frequency waves
  }

  @SuppressWarnings("unused")
  public static float function(float theta, float ot, float a) {
    float output = (float) (sin(a * PPI * theta));
            output += (float) cos(PPI * ot);
            output -= (float) (cos((5+ot)*PPI*theta));
            output += (float) ((1f/20) * cos(20f * PPI * (4*theta + ot))); //Extra high frequency waves
            output += (float) ((1f/10) * cos(35f * PPI * (4*theta + a )));
    return output;
  }
}
