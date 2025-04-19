package com.cocoawerks.mosaic.router.client;

import com.cocoawerks.mosaic.router.client.event.HasRouteChangeHandlers;
import com.cocoawerks.mosaic.router.client.event.RouteChangeEvent;
import com.cocoawerks.mosaic.router.client.event.RouteChangeHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;
import java.util.Map;

import static elemental2.dom.DomGlobal.*;

public class Router implements HasRouteChangeHandlers {
  private final Map<RegExp, Widget> views = new HashMap<>();

  static final Router INSTANCE = new Router();

  private Widget currentView;

  public static Router get() {
    return INSTANCE;
  }

  public Route currentRoute() {
    return new Route(window.location.pathname);
  }

  private Router() {
    window.addEventListener(
      "popstate",
      event -> {
        handlerManager.fireEvent(new RouteChangeEvent());
      }
    );

    addRouteChangeHandler(
      event -> {
        displayCurrentView();
      }
    );

    Scheduler.get().scheduleDeferred(this::displayCurrentView);
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
    if(pattern != null) {
      if(pattern.endsWith("**")) {
        String regExpPattern = "^"+pattern.substring(0, pattern.length() - 2)+"\\S+$";
        console.log(regExpPattern);
        views.put(RegExp.compile(regExpPattern), view);
      }
      else if(pattern.endsWith("*")) {
        views.put(RegExp.compile(pattern), view);
      }
      else {
        String normalizedPath = Route.normalizePath(pattern);
        String regExpPattern = "^"+normalizedPath+"$";
        views.put(RegExp.compile(regExpPattern), view);
      }
    }
  }

  public Widget getView(Route route) {
    for (Map.Entry<RegExp, Widget> entry : views.entrySet()) {
      console.log(route.getPath());
      console.log(entry.getKey().toString());
      if(entry.getKey().test(route.getPath())) {
        return entry.getValue();
      }
    }
    return null;
  }

  public void routeTo(IsSerializable data, Route route) {
    Route currentRoute = currentRoute();

    if (!currentRoute.equals(route)) {
      history.pushState(data, "", route.getPath());
      handlerManager.fireEvent(new RouteChangeEvent());
    }
  }

  private final HandlerManager handlerManager = new HandlerManager(this);

  @Override
  public HandlerRegistration addRouteChangeHandler(RouteChangeHandler handler) {
    return handlerManager.addHandler(RouteChangeEvent.getType(), handler);
  }
}
