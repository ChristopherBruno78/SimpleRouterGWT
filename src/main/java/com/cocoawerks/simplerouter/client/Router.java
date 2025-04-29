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
  private static Router INSTANCE;

  private static final String SINGLE_ROUTER_ERROR_MSG =
    "Router already created: only one Router instance is allowed.";

  public static Router create(String basePath) {
    assert INSTANCE == null : SINGLE_ROUTER_ERROR_MSG;
    INSTANCE = new Router(basePath);
    return INSTANCE;
  }

  public static Router create() {
    return create("/");
  }

  public static Router get() {
    if (INSTANCE != null) {
      return INSTANCE;
    }
    return create();
  }

  private final URLToken baseUrl;

  final Map<RegExp, Route> paths = new HashMap<>();
  final Map<RegExp, Widget> views = new HashMap<>();

  private Widget currentView;
  private Widget notFoundView;

  private Router(String basePath) {
    this.baseUrl = new URLToken(basePath);
  }

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

  public URLToken currentURLToken() {
    return new URLToken(window.location.toString());
  }

  private void displayCurrentView() {
    Scheduler
      .get()
      .scheduleDeferred(
        () -> {
          URLToken token = currentURLToken();
          Widget nextView = getView(token);
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
      Route route = new Route(baseUrl.deriveURLByAppendingPathComponent(path));
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
    navigateTo(new URLToken(route));
  }

  public void navigateTo(URLToken route) {
    URLToken newUrl = baseUrl
      .deriveURLByAppendingPathComponent(route.getPath())
      .deriveURLByAppendingQueries(route.getQueryParameters());
    history.pushState(null, "", newUrl.getPathAndQuery());
    displayCurrentView();
  }

  /**
   * When a route is not found, the view to display
   * @param notFoundView
   */
  public void setNotFoundView(Widget notFoundView) {
    this.notFoundView = notFoundView;
  }

  Route getRoute(URLToken route) {
    for (Map.Entry<RegExp, Route> entry : paths.entrySet()) {
      if (entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return null;
  }

  Widget getView(URLToken route) {
    for (Map.Entry<RegExp, Widget> entry : views.entrySet()) {
      if (entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return notFoundView;
  }
}
