package com.cocoawerks.simplerouter.client;

import static elemental2.dom.DomGlobal.window;

import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * RouteURL is a wrapper around URL with
 * extra functionality
 */
public class RouteURL {
  final URL url;

  public RouteURL(String urlString) {
    this.url = new URL(urlString, window.location.href);
  }

  /**
   * Converts a string and prefixes it with a backslash and
   * appends one if necessary. This "normalizes" the
   * path string, ensuring all path strings have the same format
   * @param path
   * @return normalized path
   */
  static String normalize(String path) {
    if (!path.startsWith("/")) {
      path = "/" + path;
    }
    if (!path.endsWith("/")) {
      path += "/";
    }
    return path;
  }

  public String getPath() {
    return normalize(url.pathname);
  }

  public RouteURL deriveRouteByAppendingPathComponent(String pathComponent) {
    return new RouteURL(normalize(getPath() + pathComponent));
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
    Route route = Router.get().getRoute(this);
    assert route != null : "Route is not yet registered with the Router";
    Integer index = route.getParamIndex(name);
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
    RouteURL route = (RouteURL) o;
    return Objects.equals(url.href, route.url.href);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(url.href);
  }
}
