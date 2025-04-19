package com.cocoawerks.mosaic.router.client;

import static elemental2.dom.DomGlobal.console;

import com.cocoawerks.gwt.mosaic.client.ui.Button;
import com.cocoawerks.gwt.mosaic.client.utils.DOM;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class Entry implements EntryPoint {

  @Override
  public void onModuleLoad() {
    Button button0 = new Button("View 1");

    button0.addClickHandler(
      event -> {
        Router.get().routeTo(null, new Route("/two/2"));
        DOM.invokeLater(new Scheduler.ScheduledCommand() {
          @Override
          public void execute() {
            Window.alert(Router.get().currentRoute().getLastPathComponent());
          }
        });
      }
    );

    Button button1 = new Button("View 2");

    button1.addClickHandler(
      event -> {
        Router.get().routeTo(null, new Route("/one"));
      }
    );

    Router.get().mapRoute("/one", button0);
    Router.get().mapRoute("/two/*", button1);
  }
}
