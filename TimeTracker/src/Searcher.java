import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe padre de las classe SearcherByTag y SearcherByName,
 * que sirve como clase abstracta y almacena una lista de objetos
 * donde guardaremos las actividades que satisfazcan los parámetros
 * de cada búsqueda.
 */

public abstract class Searcher implements Visitor {
  private List<Activity> objList;

  public Searcher() {
    objList = new LinkedList<Activity>();
  }

  public List<Activity> getObjList() {
    return objList;
  }

}
