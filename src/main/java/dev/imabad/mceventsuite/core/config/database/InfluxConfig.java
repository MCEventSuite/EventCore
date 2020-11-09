package dev.imabad.mceventsuite.core.config.database;

import dev.imabad.mceventsuite.core.api.BaseConfig;
import dev.imabad.mceventsuite.core.api.database.BaseDatabaseConfig;

public class InfluxConfig extends BaseConfig {

    private String url;
    private String org;
    private String bucket;

    @Override
    public String getName() {
        return "influx";
    }

    public String getUrl() {
        return url;
    }

    public String getOrg() {
        return org;
    }

    public String getBucket() {
        return bucket;
    }
}
