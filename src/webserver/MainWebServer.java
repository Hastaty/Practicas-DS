package webserver;

import core.Activity;
import core.Application;
import core.Clock;

public class MainWebServer {
  public static void main(String[] args) {
    webServer();
  }

  public static void webServer() {
    final Activity root = Application.makeTreeCourses();
    // implement this method that returns the tree of
    // appendix A in the practicum handout

    // start your clock

    new WebServer(root);
    Clock.getInstance().stopTimer();
  }
}
