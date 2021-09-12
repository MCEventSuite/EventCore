package dev.imabad.mceventsuite.core.config;

import dev.imabad.mceventsuite.core.api.BaseConfig;

public class CoreConfig extends BaseConfig {

  private String identifier = "unknown";
  private String currentYear = "2021";

  @Override
  public String getName() {
    return "core";
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getCurrentYear() {
    return currentYear;
  }

  public int getCurrentYearAsInt(){
    return Integer.parseInt(currentYear);
  }
}
