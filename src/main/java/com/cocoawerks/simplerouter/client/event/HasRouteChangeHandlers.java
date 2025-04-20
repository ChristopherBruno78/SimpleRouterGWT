package com.cocoawerks.simplerouter.client.event;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasRouteChangeHandlers {
  HandlerRegistration addRouteChangeHandler(RouteChangeHandler handler);
}
