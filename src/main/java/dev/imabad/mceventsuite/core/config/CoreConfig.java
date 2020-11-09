package dev.imabad.mceventsuite.core.config;

import dev.imabad.mceventsuite.core.api.BaseConfig;

public class CoreConfig extends BaseConfig {

  private String identifier = "unknown";

  @Override
  public String getName() {
    return "core";
  }

  public String getIdentifier() {
    return identifier;
  }
}
