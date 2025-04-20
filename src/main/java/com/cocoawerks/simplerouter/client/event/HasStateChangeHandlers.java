package com.cocoawerks.simplerouter.client.event;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasStateChangeHandlers {
  HandlerRegistration addStateChangeHandler(StateChangeHandler handler);
}
