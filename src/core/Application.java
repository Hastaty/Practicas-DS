package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;


/**
 * <h2>Clase Principal({@code Application})</h2>
 * El TimeTracker es un tipo de aplicación que nos permite
 * crear una jerarquía de actividades( proyectos o tareas)
 * y controlar el tiempo que dedicamos a cada una de estas actividades.
 * Esta es la clases que controla al programa. Aquí podemos encontrar los tests para las
 * diferentes funcionalidades y la función main para que será el núcleo de ejecución de la
 * aplicación. También encontramos métodos para la
 * persistencia de la sesión del TimeTracker.
 */

public class Application {

  /**
   * classe Aplication
   * {@code rootProject} es el proyecto principal a partir del cual construiremos la jerarquía
   * de proyectos y tareas.
   */

  private static Project rootProject = new Project("My activities");
  private static final Logger logger = LoggerFactory.getLogger("Application");
  private static final Marker fita2 = MarkerFactory.getMarker("F2");
  private static final Marker fita1 = MarkerFactory.getMarker("F1");

  /**
   * La función principal que ejecutará las diferentes
   * funcionalidades del proyecto así como los tests.
   *
   * @param args este parámetro actualmente no se usa.
   */

  public static void main(String[] args) {
    // Clock c = Clock.getInstance();
    // c.startTimer();
    // rootProject.setTag("ROOT");
    makeTreeCourses();
    //List<Activity> matchingActivities = searchbytag("ROOT");
    //logger.trace("DONE APPLICATION");
    //c.stopTimer();
    writejson();
    //buildTreefromjson();
    //printTree();
    //testCreateHierarchy();
  }

  /**
   * test para comprobar si el reloj funciona.
   * Creamos una jerarquía simple con un proyecto y una tarea
   * y imprimimos ese arbol cada 2 segundos
   * para ver como se va actualizando
   */

  public static void testClock() {
    Clock c = Clock.getInstance();
    c.startTimer();
    Timer timer = c.getTimer();
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        synchronized (this) {
          printTree();
        }
      }
    }, Date.from(Instant.now()), 2000);
    Project p = new Project("root");
    Task t = new Task("task");
    p.addActivity(t);
    t.startTask();
    try {
      Thread.currentThread().sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    t.stopTask();
    try {
      Thread.currentThread().sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    t.startTask();
    try {
      Thread.currentThread().sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    t.stopTask();
    Clock.getInstance().stopTimer();
  }

  /**
   * En este test creamos una jerarquía de proyectos
   * y tareas partiendo del proyecto {@code
   * rootProject} y simulamos
   * el funcionamiento real del proyecto poniendo
   * tareas en marcha y parandolas. Así pues podemos observar
   * también el correcto funcionamiento del reloj.
   * A partir de {@code rootProject} podremos comprobar
   * si la jerarquía se ha construido correctamente
   * y al igual que en {@code testClock()} planificamos la
   * impresión del árbol cada 2 segundos y ver como se van
   * sucediendo las actualizaciones( como se
   * crean nuevos intervalos
   * y como se actualiza el tiempo de Intervalos, Tareas
   * y Proyectos)
   */

  public static Activity makeTreeCourses() {

    logger.debug(fita2, "Starting application\n");
    Clock c = Clock.getInstance();
    c.startTimer();
    Timer t = c.getTimer();
    /*
    t.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        synchronized (this) {
          printTree();
        }
      }
    }, Date.from(Instant.now()), 2000);*/
    // TOP LEVEL(0)
    Project softwareDesign = new Project("software design");
    Project softwareTesting = new Project("software testing");
    Project databases = new Project("databases");
    Task tasktransportation = new Task("task transportation");
    rootProject.addActivity(softwareDesign);
    rootProject.addActivity(softwareTesting);
    rootProject.addActivity(databases);
    rootProject.addActivity(tasktransportation);

    // LEVEL 1
    Project problems = new Project("problems");
    Project projectTimeTracker = new Project("project time tracker");
    softwareDesign.addActivity(problems);
    softwareDesign.addActivity(projectTimeTracker);

    // LEVEL 2
    Task firstList = new Task("first list");
    Task secondList = new Task("second list");
    problems.addActivity(firstList);
    problems.addActivity(secondList);
    Task readHandout = new Task("read handout");
    Task firstMilestone = new Task("first milestone");
    projectTimeTracker.addActivity(readHandout);
    projectTimeTracker.addActivity(firstMilestone);
    logger.trace(fita2, "Printing the hierarchy of projects and tasks... \n");
    tasktransportation.startTask();
    try {
      Thread.currentThread().sleep(4000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    tasktransportation.stopTask();
    try {
      Thread.currentThread().sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    firstList.startTask();
    try {
      Thread.currentThread().sleep(6000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    secondList.startTask();
    try {
      Thread.currentThread().sleep(4000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    firstList.stopTask();
    try {
      Thread.currentThread().sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    secondList.stopTask();
    try {
      Thread.currentThread().sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    tasktransportation.startTask();
    try {
      Thread.currentThread().sleep(4000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    tasktransportation.stopTask();
    //c.stopTimer();

    return rootProject;
  }

  /**
   * Esta función implementa la
   * persistencia de los datos de
   * nuestra jerarquía. A partir de un objeto
   * de la clase {@code JSONWriter} ,
   * que implementa la
   * interfaz {@code Visitor},guardamos en su atributo
   * {@code jsonArray} un {@code JSONArray}, que
   * contendrá {@code JSONObjects} con información de Proyectos,
   * Tareas o Intervalos, que luego vamos a escribir como {@code String}
   * en un archivo txt. Este
   * archivo txt nos permitirá posteriormente
   * recuperar la sesión.
   */
  public static void writejson() {

    Jsonwriter writer = new Jsonwriter();
    rootProject.accept(writer);

    try {

      FileWriter fw = new FileWriter("jsonfile.txt");
      fw.write(writer.getJsonArray().toString());
      fw.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Este método nos permite recuperar la sesión guardada
   * en el archivo {@code jsonfile.txt}. De
   * este modo construimos la jerarquía que tenemos
   * guardada en el archivo sobre el
   * proyecto raíz {@code rootProject}. Obtendremos
   * la información de los {@code JSONObjects} guardados y los
   * reconstruiremos como los objetos correspondientes
   * a partir de su tipo({@code Project, Task, o Interval})
   */
  public static void buildTreefromjson() {

    String rawdata = "";

    try {

      File file = new File("jsonfile.txt");
      Scanner scanner = new Scanner(file);
      rawdata = scanner.nextLine();
      scanner.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    JSONArray data = new JSONArray(rawdata);
    JSONObject jsonObj = data.getJSONObject(0);
    rootProject.setName(jsonObj.get("name").toString());
    rootProject.setInitialDateTime(LocalDateTime.parse(jsonObj.get("initial_data").toString()));
    rootProject.setFinalDateTime(LocalDateTime.parse(jsonObj.get("final_data").toString()));
    String durationFormatted=jsonObj.get("duration").toString();
    rootProject.setDuration(Duration.parse(durationFormatted));
    data.remove(0);

    for (int i = 0; i < data.length(); i++) {

      JSONObject jsonObject = data.getJSONObject(i);

      switch (jsonObject.get("type").toString()) {

        case "Project":
          Project project = new Project(jsonObject.get("name").toString());
          project.setId(Integer.parseInt(jsonObject.get("id").toString()));
          project.setTag(jsonObject.get("tag").toString());
          project.setInitialDateTime(LocalDateTime.parse(
              jsonObject.get("initial_data").toString()));
          project.setFinalDateTime(LocalDateTime.parse(jsonObject.get("final_data").toString()));
          durationFormatted=jsonObject.get("duration").toString();
          project.setDuration(Duration.parse(durationFormatted));
          SearcherByName psearcher = new SearcherByName(jsonObject.get("parentname").toString());
          rootProject.accept(psearcher);
          Project parent = (Project) (psearcher.getObjList().get(0));
          project.setParentProject(parent);
          parent.addActivity(project);
          break;
        case "Task":
          Task  task = new Task(jsonObject.get("name").toString());
          task.setId(Integer.parseInt(jsonObject.get("id").toString()));
          task.setTag(jsonObject.get("tag").toString());
          task.setInitialDateTime(LocalDateTime.parse(jsonObject.get("initial_data").toString()));
          task.setFinalDateTime(LocalDateTime.parse(jsonObject.get("final_data").toString()));
          durationFormatted=jsonObject.get("duration").toString();
          task.setDuration(Duration.parse(durationFormatted));
          SearcherByName tsearcher = new SearcherByName(jsonObject.get("parentname").toString());
          rootProject.accept(tsearcher);
          Project parentProject = (Project) (tsearcher.getObjList().get(0));
          task.setParentProject(parentProject);
          parentProject.addActivity(task);
          break;
        case "Interval":
          Interval interval = new Interval();
          interval.setStart(LocalDateTime.parse(jsonObject.get("start").toString()));
          interval.setEnd(LocalDateTime.parse(jsonObject.get("start").toString()));
          durationFormatted=jsonObject.get("duration").toString();
          interval.setDuration(Duration.parse(durationFormatted));
          SearcherByName isearcher = new SearcherByName(jsonObject.get("parentname").toString());
          rootProject.accept(isearcher);
          Task parentTask = (Task) (isearcher.getObjList().get(0));
          interval.setParentTask(parentTask);
          parentTask.addInterval(interval);
          break;
        default:
          assert false : "Type  is not Project, task or Interval";
      }

    }
    System.out.print("DONE");
  }

  /**
   * Buscamos por tag.
   *
   * @param tag tag identificar project.
   *
   * @return lista de actividades.
   */

  public static List<Activity> searchbytag(String tag) {

    SearcherByTag searcher = new SearcherByTag(tag);
    rootProject.accept(searcher);
    return searcher.getObjList();

  }

  /**
   * Este método nos permite imprimir la información
   * del árbol por pantalla a partir de un
   * objeto de la clase {@code InfoPrinter} que
   * implementa la interfaz {@code Visitor}.
   */

  public static void printTree() {

    InfoPrinter printer = new InfoPrinter();
    rootProject.accept(printer);

  }

  /** Función que calcula el tiempo total de una actividad.
   *
   * @param activity actividad
   * @param period YEARL, DAY, HOUR
   * @return duracion de una actividad.
   */

  public Duration totaltimespent(Activity activity, Timeperiods period) {

    return activity.totalTimeSpent(period);

  }

}
