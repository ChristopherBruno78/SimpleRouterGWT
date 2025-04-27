package com.cocoawerks.simplerouter.client;

import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.URLSearchParams;

import java.util.*;

import static elemental2.dom.DomGlobal.window;

/**
 * URL is a wrapper around native URL with
 * extra functionality
 */
public class URL {
  final elemental2.dom.URL url;

  public URL(String relativePath, String basePath) {
    this.url =
      new elemental2.dom.URL(
        relativePath,
        getHostUrl() + stripRelativePath(basePath)
      );
  }

  public URL(String relativePath) {
    this(relativePath, "");
  }

  /**
   * Converts a string and prefixes it with a backslash and
   * appends one if necessary. This "normalizes" the
   * path string, ensuring all path strings have the same format
   * @param path
   * @return normalized path
   */
  static String normalizeRelativePath(String path) {
    if (!path.startsWith("/")) {
      path = "/" + path;
    }
    if (!path.endsWith("/")) {
      path += "/";
    }
    return path;
  }

  static String stripRelativePath(String path) {
    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    if (path.endsWith("/")) {
      path = path.substring(0, path.length() - 1);
    }
    return path;
  }

  public String getHostUrl() {
    return window.location.protocol + "//" + window.location.host;
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
    return normalizeRelativePath(url.pathname);
  }

  public String getQuery() {
    return url.search;
  }

  public String getPathAndQuery() {
    String path = normalizeRelativePath(url.pathname);
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
    return new URL(
      normalizeRelativePath(getPath() + stripRelativePath(pathComponent)) +
      getQuery()
    );
  }

  public URL deriveURLByAppendingQuery(String key, String value) {
    URL derivedURL = new URL(getHref());
    derivedURL.url.searchParams.append(key, value);
    return derivedURL;
  }

  public URL deriveURLByAppendingQueries(Map<String, String> query) {
    URL derivedURL = new URL(getHref());
    for (Map.Entry<String, String> entry : query.entrySet()) {
      derivedURL.url.searchParams.append(entry.getKey(), entry.getValue());
    }
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

  private static final String ROUTE_NOT_REGISTERED_ERROR_MSG =
    "Route is not yet registered with the Router.";

  public String getPathParameter(String name) {
    Route route = Router.get().getRoute(this);
    assert route != null : ROUTE_NOT_REGISTERED_ERROR_MSG;
    Integer index = route.getParamIndex(name);
    if (index > -1) {
      return getPathComponent(index);
    }
    return "";
  }

  public String getQueryParameter(String name) {
    return url.searchParams.get(name);
  }

  public Map<String, String> getQueryParameters() {
    URLSearchParams params = url.searchParams;
    Map<String, String> paramsMap = new HashMap<>();
    params.forEach((v, k) -> paramsMap.put(k, v));
    return paramsMap;
  }

  public Widget getView() {
    Widget view = Router.get().getView(this);
    assert view != null : ROUTE_NOT_REGISTERED_ERROR_MSG;
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
