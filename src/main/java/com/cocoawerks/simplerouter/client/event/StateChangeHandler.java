package com.cocoawerks.simplerouter.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface StateChangeHandler extends EventHandler {
  void onStateChange(StateChangeEvent event);
}
