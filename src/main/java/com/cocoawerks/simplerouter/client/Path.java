package com.cocoawerks.simplerouter.client;

import com.google.gwt.regexp.shared.RegExp;

import java.util.HashMap;
import java.util.Map;

import static elemental2.dom.DomGlobal.console;

/**
 * Path is a relative path that ultimate the Router
 * uses to map with views. It can contain path parameters
 * prefixed with : and wildcards using *. Examples
 *   /todoApp/
 *   /document/:id
 *   /app/*
 */
public class Path {
  private final String path;

  private final Map<String, Integer> pathParams = new HashMap<>();

  public Path(String path) {
    this.path = normalize(path);
    parse();
  }

  /**
   * Converts a string and prefixes it with a / and
   * appends a / if necessary. Aka "normalizes" the
   * path string. This keeps all path strings
   * with the same format
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

  private void parse() {
    String[] parts = path.split("/");
    Integer i = 0;
    for (String pathComponent : parts) {
      if (pathComponent.isBlank()) {
        continue;
      }
      if (pathComponent.startsWith(":")) {
        pathParams.put(pathComponent.substring(1), i);
      }
      i++;
    }
  }

  public Integer getParamIndex(String paramName) {
    if (pathParams.containsKey(paramName)) {
      return pathParams.get(paramName);
    }
    return -1;
  }

  @Override
  public String toString() {
    return path;
  }

  public RegExp toRegExp() {
    StringBuilder regexPattern = new StringBuilder();
    String[] parts = path.split("/");
    regexPattern.append("^");
    for (String part : parts) {
      if (!part.isBlank()) {
        regexPattern.append("/");
        if (part.equals("*")) {
          regexPattern.append("\\S+");
        } else if (part.startsWith(":")) {
          regexPattern.append("[^/]+");
        } else {
          regexPattern.append(part);
        }
      }
    }
    regexPattern.append("/");
    regexPattern.append("$");
    console.log(regexPattern.toString());
    return RegExp.compile(regexPattern.toString());
  }
}
