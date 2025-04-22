package com.cocoawerks.simplerouter.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

import static elemental2.dom.DomGlobal.history;
import static elemental2.dom.DomGlobal.window;

public class Router {
  private static final Router INSTANCE = new Router();

  public static Router get() {
    return INSTANCE;
  }

  final Map<RegExp, Route> paths = new HashMap<>();
  final Map<RegExp, Widget> views = new HashMap<>();

  private Widget currentView;
  private Widget notFoundView;

  private Router() {}

  private boolean installed = false;

  public void install() {
    if (!installed) {
      window.addEventListener(
        "popstate",
        event -> {
          displayCurrentView();
        }
      );
      displayCurrentView();
      installed = true;
    }
  }

  public URL currentURL() {
    return new URL(window.location.toString());
  }

  private void displayCurrentView() {
    Scheduler
      .get()
      .scheduleDeferred(
        () -> {
          URL currentURL = currentURL();
          Widget nextView = getView(currentURL);
          if (nextView == currentView) {
            return;
          }
          RootPanel.get().clear();
          currentView = nextView;
          if (currentView != null) {
            RootPanel.get().add(currentView);
          }
        }
      );
  }

  /**
   * Assign a route to a GWT view
   * A GWT view must inherit Widget
   * @param path
   * @param view
   */
  public void route(String path, Widget view) {
    if (path != null) {
      Route route = new Route(path);
      RegExp regExp = route.toRegExp();
      views.put(regExp, view);
      paths.put(regExp, route);
      install();
    }
  }

  /**
   * Sends the browser to another Route and updates
   * the view
   * @param route
   */
  public void navigateTo(String route) {
    navigateTo(new URL(route));
  }

  public void navigateTo(URL url) {
    history.pushState(null, "", url.getPathAndQuery());
    displayCurrentView();
  }

  /**
   * When a route is not found, the view to display
   * @param notFoundView
   */
  public void setNotFoundView(Widget notFoundView) {
    this.notFoundView = notFoundView;
  }

  Route getRoute(URL route) {
    for (Map.Entry<RegExp, Route> entry : paths.entrySet()) {
      if (entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return null;
  }

  Widget getView(URL route) {
    for (Map.Entry<RegExp, Widget> entry : views.entrySet()) {
      if (entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return notFoundView;
  }
}
