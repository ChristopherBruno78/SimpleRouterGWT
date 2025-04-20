package com.cocoawerks.simplerouter.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class RouteChangeEvent extends GwtEvent<RouteChangeHandler> {
  private static final Type<RouteChangeHandler> TYPE = new Type<RouteChangeHandler>();

  @Override
  public Type<RouteChangeHandler> getAssociatedType() {
    return TYPE;
  }

  public static Type<RouteChangeHandler> getType() {
    return TYPE;
  }

  @Override
  protected void dispatch(RouteChangeHandler routeChangeHandler) {
    routeChangeHandler.onRouteChange(this);
  }
}
