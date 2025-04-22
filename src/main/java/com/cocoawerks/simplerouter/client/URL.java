package com.cocoawerks.simplerouter.client;

import static elemental2.dom.DomGlobal.window;

import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * URL is a wrapper around native URL with
 * extra functionality
 */
public class URL {
  final elemental2.dom.URL url;

  public URL(String urlString) {
    this.url = new elemental2.dom.URL(urlString, getRoot());
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

  public String getRoot() {
     return window.location.protocol + "//" + window.location.hostname;
  }

  public String getHref() {
    return url.href;
  }

  public String getOrigin() {
    return url.origin;
  }

  public String getProtocol() {
    return url.protocol;
  }

  public String getHost() {
    return url.host;
  }

  public String getHostname() {
    return url.hostname;
  }

  public String getPort() {
    return url.port;
  }

  public String getPath() {
    return normalize(url.pathname);
  }

  public String getQuery() {
    return url.search;
  }

  public String getPathAndQuery() {
    String path = normalize(url.pathname);
    if (url.search.isBlank()) {
      return path;
    }
    return path.substring(0, path.length() - 1) + url.search;
  }

  /**
   * appends a path component, maintaining the query params
   * @param pathComponent
   * @return URL with appended path component
   */
  public URL deriveURLByAppendingPathComponent(String pathComponent) {
    return new URL(normalize(getPath() + pathComponent) + getQuery());
  }

  public URL deriveURLByAppendingQuery(String key, String value) {
    URL derivedURL = new URL(normalize(getPath()) + getQuery());
    derivedURL.url.searchParams.append(key, value);
    return derivedURL;
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

  /**
   * Returns the path component at position ${index}
   * @param index
   * @return
   */
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
    URL route = (URL) o;
    return Objects.equals(url.href, route.url.href);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(url.href);
  }
}
