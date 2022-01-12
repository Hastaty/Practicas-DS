package core;

import java.util.List;

public class SearcherById extends Searcher{
  private int id;
  private boolean id_correct() {
    if (id < 0) {
      return false;
    }
    return true;
  }
  public SearcherById(int id)
  {
    if (id < 0) {
      throw new IllegalArgumentException("name null");
    }
    this.id=id;
    assert id_correct() : "name null";
  }

  @Override
  public void visit(Task task) {
    if (task == null) {
      throw new IllegalArgumentException("project null");
    }
    if (task.getId()< 0) {
      throw new IllegalArgumentException("project null");
    }
    if(task.getId()==id)
    {
      List<Activity> list = getObjList();
      list.add(task);
    }

  }

  @Override
  public void visit(Project project) {
    if (project == null) {
      throw new IllegalArgumentException("project null");
    }
    if (project.getId()< 0) {
      throw new IllegalArgumentException("project null");
    }
    if(project.getId()==id)
    {
      List<Activity> list = getObjList();
      list.add(project);
    }
  }

  @Override
  public void visit(Interval interval) {
    return;
  }
}
