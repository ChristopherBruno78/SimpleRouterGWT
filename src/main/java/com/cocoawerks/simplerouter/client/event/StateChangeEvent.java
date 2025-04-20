package com.cocoawerks.simplerouter.client.event;

import com.cocoawerks.simplerouter.client.Route;
import com.google.gwt.event.shared.GwtEvent;

public class StateChangeEvent extends GwtEvent<StateChangeHandler> {
  private final Object state;

  public StateChangeEvent(Object state) {
    this.state = state;
  }

  public Object getState() {
    return state;
  }

  private static final Type<StateChangeHandler> TYPE = new Type<StateChangeHandler>();

  @Override
  public Type<StateChangeHandler> getAssociatedType() {
    return TYPE;
  }

  public static Type<StateChangeHandler> getType() {
    return TYPE;
  }

  @Override
  protected void dispatch(StateChangeHandler stateChangeHandler) {
    stateChangeHandler.onStateChange(this);
  }
}
