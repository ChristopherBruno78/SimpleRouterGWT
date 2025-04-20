package com.cocoawerks.simplerouter.client;

import static com.cocoawerks.simplerouter.client.RouteURL.normalize;

import com.google.gwt.regexp.shared.RegExp;
import java.util.HashMap;
import java.util.Map;

/**
 * A relative path that the Router uses to match with
 * Views. It can contain path parameters prefixed with :
 * and wildcards using *. Examples:
 *   /todoApp/
 *   /document/:id
 *   /app/*
 */
public class Route {
  private final String path;

  private final Map<String, Integer> pathParams = new HashMap<>();

  public Route(String path) {
    this.path = normalize(path);
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

  /**
   * Converts the Path to a Regular Expression for mathcing
   * with Routes.
   * @return Regular Expression representation of Path
   */
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
    return RegExp.compile(regexPattern.toString());
  }
}
