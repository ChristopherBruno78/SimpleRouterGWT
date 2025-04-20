package com.cocoawerks.simplerouter.client;

import static elemental2.dom.DomGlobal.window;

import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Route is a wrapper around URL with
 * extra functionality
 */
public class Route {
  private final URL url;

  public Route(String urlString) {
    this.url = new URL(urlString, window.location.href);
  }

  public String getPath() {
    return Path.normalize(url.pathname);
  }

  public Route deriveRouteByAppendingPathComponent(String pathComponent) {
    return new Route(Path.normalize(getPath() + pathComponent));
  }

  public String[] getPathComponents() {
    List<String> pathComponents = new ArrayList<>();
    String[] parts = getPath().split("/");
    for (String part : parts) {
      if (!part.isBlank()) {
        pathComponents.add(part);
      }
    }
    return pathComponents.toArray(new String[0]);
  }

  public String getPathComponent(Integer index) {
    if (index > -1) {
      String[] parts = getPathComponents();
      if (index < parts.length) {
        return parts[index];
      }
    }
    return "";
  }

  public String getPathParameter(String name) {
    Path path = Router.get().getPath(this);
    assert path != null : "Route is not yet registered with the Router";
    Integer index = path.getParamIndex(name);
    if (index > -1) {
      return getPathComponent(index);
    }
    return "";
  }

  public String getQueryParameter(String name) {
    return url.searchParams.get(name);
  }

  public Widget getView() {
    Widget view = Router.get().getView(this);
    assert view != null : "Route is not yet registered with the Router";
    return view;
  }

  @Override
  public String toString() {
    return url.href;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Route route = (Route) o;
    return Objects.equals(url.href, route.url.href);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(url.href);
  }
}
