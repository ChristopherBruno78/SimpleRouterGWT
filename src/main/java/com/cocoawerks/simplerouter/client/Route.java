package com.cocoawerks.simplerouter.client;

import static elemental2.dom.DomGlobal.window;

import elemental2.dom.URL;
import java.util.Objects;

public class Route {
  private final URL url;

  static String normalizePath(String path) {
    if (!path.endsWith("/")) {
      path += "/";
    }
    return path;
  }

  public Route(String path) {
    this.url = new URL(path, window.location.href);
  }

  public String getPath() {
    return normalizePath(url.pathname);
  }

  public String getLastPathComponent() {
    String[] parts = url.pathname.split("/");
    if (parts.length > 1) {
      return parts[parts.length - 1];
    }
    return "";
  }

  public String getParameter(String name) {
    return url.searchParams.get(name);
  }

  @Override
  public String toString() {
    return url.href;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Route route = (Route) o;
    return Objects.equals(getPath(), route.getPath());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getPath());
  }
}
