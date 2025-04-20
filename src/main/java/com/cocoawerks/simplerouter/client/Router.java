package com.cocoawerks.simplerouter.client;

import static elemental2.dom.DomGlobal.history;
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

  public RouteURL currentURL() {
    return new RouteURL(window.location.toString());
  }

  private void displayCurrentView() {
    Scheduler
      .get()
      .scheduleDeferred(
        () -> {
          RouteURL currentURL = currentURL();
          Widget nextView = getView(currentURL);
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
      );
  }

  public void map(String path, Widget view) {
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
  public void routeTo(String route) {
    RouteURL routeURL = new RouteURL(route);
    history.pushState(null, "", routeURL.getPath());
    displayCurrentView();
  }

  /**
   * When a route is not found, the view to display
   * @param notFoundView
   */
  public void setNotFoundView(Widget notFoundView) {
    this.notFoundView = notFoundView;
  }

  Route getRoute(RouteURL route) {
    for (Map.Entry<RegExp, Route> entry : paths.entrySet()) {
      if (entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return null;
  }

  Widget getView(RouteURL route) {
    for (Map.Entry<RegExp, Widget> entry : views.entrySet()) {
      if (entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return notFoundView;
  }
}
