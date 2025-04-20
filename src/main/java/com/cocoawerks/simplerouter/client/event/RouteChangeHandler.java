package com.cocoawerks.simplerouter.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface RouteChangeHandler extends EventHandler {
  void onRouteChange(RouteChangeEvent event);
}
