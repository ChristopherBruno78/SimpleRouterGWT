package com.cocoawerks.simplerouter.client;

import static elemental2.dom.DomGlobal.console;

import com.google.gwt.regexp.shared.RegExp;
import java.util.HashMap;
import java.util.Map;

/**
 * An internal class that tracks relative paths allowing the Router uses
 * to match URLs with Views. Paths can contain path parameters
 * prefixed with : and wildcards using *. Examples:
 *   /todoApp/
 *   /document/:id
 *   /app/*
 */
class Route {
  private final String path;

  private final Map<String, Integer> pathParams = new HashMap<>();

  public Route(URL url) {
    this.path = url.getPath();
    parse();
  }

  /**
   * Parses out path parameters
   */
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

  /**
   * Gets the index of the path parameter named paramName.
   * @param paramName
   * @return index of path parameter paramName or -1 if paramName
   * is not a path parameter
   */
  Integer getParamIndex(String paramName) {
    if (pathParams.containsKey(paramName)) {
      return pathParams.get(paramName);
    }
    return -1;
  }

  @Override
  public String toString() {
    return path;
  }

  /**
   * Converts the Path to a Regular Expression for mathcing
   * with Routes.
   * @return Regular Expression representation of Path
   */
  RegExp toRegExp() {
    StringBuilder regexPattern = new StringBuilder();
    console.log("path = " + path);
    String[] parts = path.split("/");
    console.log(parts);
    regexPattern.append("^");
    for (String part : parts) {
      if (!part.isBlank()) {
        regexPattern.append("/");
        if (part.equals("*")) {
          regexPattern.append(".*");
        } else if (part.startsWith(":")) {
          regexPattern.append("[^/]+");
        } else {
          regexPattern.append(part);
        }
      }
    }
    if (regexPattern.charAt(regexPattern.length() - 1) != '*') {
      regexPattern.append("/");
    }

    regexPattern.append("$");
    console.log(regexPattern.toString());
    return RegExp.compile(regexPattern.toString());
  }
}
