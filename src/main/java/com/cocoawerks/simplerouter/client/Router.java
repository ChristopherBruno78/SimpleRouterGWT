package com.cocoawerks.simplerouter.client;

import static elemental2.dom.DomGlobal.*;

import com.cocoawerks.simplerouter.client.event.HasStateChangeHandlers;
import com.cocoawerks.simplerouter.client.event.StateChangeEvent;
import com.cocoawerks.simplerouter.client.event.StateChangeHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.PopStateEvent;
import java.util.HashMap;
import java.util.Map;

public class Router implements HasStateChangeHandlers {
  private static final Router INSTANCE = new Router();

  public static Router get() {
    return INSTANCE;
  }

  private final Map<RegExp, Widget> views = new HashMap<>();

  private Widget currentView;
  private Widget notFoundView;

  private Router() {}

  public void install() {
    window.addEventListener(
      "popstate",
      event -> {
        PopStateEvent popStateEvent = (PopStateEvent) event;
        handlerManager.fireEvent(new StateChangeEvent(popStateEvent.state));
      }
    );

    addStateChangeHandler(
      event -> {
        displayCurrentView();
      }
    );

    Scheduler
      .get()
      .scheduleDeferred(
        () -> {
          handlerManager.fireEvent(new StateChangeEvent(null));
        }
      );
  }

  public Route currentRoute() {
    return new Route(window.location.pathname);
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

  public void mapRoute(String pattern, Widget view) {
    if (pattern != null) {
      if (pattern.endsWith("**")) {
        final String regExpPattern =
          "^" + pattern.substring(0, pattern.length() - 2) + "\\S+$";
        views.put(RegExp.compile(regExpPattern), view);
      } else if (pattern.endsWith("*")) {
        views.put(RegExp.compile(pattern), view);
      } else {
        final String normalizedPath = Route.normalizePath(pattern);
        final String regExpPattern = "^" + normalizedPath + "$";
        views.put(RegExp.compile(regExpPattern), view);
      }
    }
  }

  public void setNotFoundView(Widget notFoundView) {
    this.notFoundView = notFoundView;
  }

  protected Widget getView(Route route) {
    for (Map.Entry<RegExp, Widget> entry : views.entrySet()) {
      if (entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return notFoundView;
  }

  public void pushState(Object state) {
    pushState(state, currentRoute());
  }

  public void pushState(Object state, Route route) {
    history.pushState(state, "", route.getPath());
  }

  private final HandlerManager handlerManager = new HandlerManager(this);

  @Override
  public HandlerRegistration addStateChangeHandler(StateChangeHandler handler) {
    return handlerManager.addHandler(StateChangeEvent.getType(), handler);
  }
}
