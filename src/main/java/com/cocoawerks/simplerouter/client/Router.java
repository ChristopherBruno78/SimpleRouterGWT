package com.cocoawerks.simplerouter.client;

import static elemental2.dom.DomGlobal.window;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;
import java.util.Map;

public class Router {
  private static final Router INSTANCE = new Router();

  public static Router get() {
    return INSTANCE;
  }

  final Map<RegExp, Path> paths = new HashMap<>();
  final Map<RegExp, Widget> views = new HashMap<>();

  private Widget currentView;
  private Widget notFoundView;

  private Router() {}

  private boolean installed = false;

  public void install() {
    if (!installed) {
      Scheduler.get().scheduleDeferred(this::displayCurrentView);
      installed = true;
    }
  }

  public Route currentRoute() {
    return new Route(window.location.toString());
  }

  private void displayCurrentView() {
    Route currentRoute = currentRoute();
    Widget nextView = getView(currentRoute);
    if (nextView == currentView) {
      return;
    }
    if (currentView != null) {
      RootPanel.get().remove(currentView);
    }
    currentView = nextView;
    if (currentView != null) {
      RootPanel.get().add(currentView);
    }
  }

  public void map(Path path, Widget view) {
    if (path != null) {
      RegExp regExp = path.toRegExp();
      views.put(regExp, view);
      paths.put(regExp, path);
      install();
    }
  }

  public void setNotFoundView(Widget notFoundView) {
    this.notFoundView = notFoundView;
  }

  Path getPath(Route route) {
    for (Map.Entry<RegExp, Path> entry : paths.entrySet()) {
      if (entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return null;
  }

  Widget getView(Route route) {
    for (Map.Entry<RegExp, Widget> entry : views.entrySet()) {
      if (entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return notFoundView;
  }
}
