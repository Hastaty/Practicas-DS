import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;


/**
 * <h2>Clase Reloj({@code Clock})</h2>
 * Esta clase implementará un reloj que nos
 * permitirá a partir del patrón {@code Observer},
 * implementando la parte {@code Observable} ,
 * notificar a los {@code Intervals}( que implementan
 * a la clase {@code Observer})
 * sobre el tiempo que es y así estos pueden
 * actualizar su tiempo final y su duración.
 * Además contiene el atributo
 * {@code timer} que nos va a permitir planificar
 * tareas en la ejecución en base al tiempo.
 */

public class Clock extends Observable {

  private static Timer timer;

  /**
   * Definimos el objeto como estático como
   * parte de la aplicación del patrón Singleton.
   * Tendremos un solo objeto de la clase
   * {@code Clock} en todo el programa
   */

  private static Clock clock = new Clock();
  // hacer el constructor privado

  private Clock() {

    timer = new Timer();

  }

  /**
   * Método que planifica la ejecución
   * del método {@code tick() cada 2 segundos}.
   */

  public void startTimer() {

    TimerTask task = new TimerTask() {
      @Override
      public void run() {

        synchronized (this) {

          tick();

        }

      }
    };

    timer.scheduleAtFixedRate(task, Date.from(Instant.now()), 2000);

  }

  /**
   * damos tiempo.
   *
   * @return time --> tiempo.
   */

  public Timer getTimer() {

    return timer;

  }

  /**
   * Método que para las planificaciones que se hayan hecho con el {@code timer}.
   */
  public void stopTimer() {

    timer.cancel();

  }

  /**
   * Método que notifica a los Observers el tiempo actual.
   */

  public void tick() {

    setChanged();
    notifyObservers(LocalDateTime.now());

  }

  /**
   * Función que implementa parte del patrón.
   * Singleton y nos sirve para recuperar el objeto {@code Clock}.
   *
   * @return Devuelve el único objeto {@code Clock}.
   */

  public static Clock getInstance() {

    return clock;

  }

}
