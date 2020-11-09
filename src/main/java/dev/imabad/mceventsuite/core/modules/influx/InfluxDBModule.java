package dev.imabad.mceventsuite.core.modules.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.internal.InfluxDBClientImpl;
import com.influxdb.client.write.Point;
import dev.imabad.mceventsuite.core.api.IConfigProvider;
import dev.imabad.mceventsuite.core.api.modules.Module;
import dev.imabad.mceventsuite.core.config.database.InfluxConfig;

import java.util.Collections;
import java.util.List;

public class InfluxDBModule extends Module implements IConfigProvider<InfluxConfig> {

    private InfluxDBClient client;
    private InfluxConfig config;

    @Override
    public String getName() {
        return "influx";
    }

    @Override
    public void onEnable() {
        client = InfluxDBClientFactory.create(config.getUrl(), config.getToken().toCharArray(), config.getOrg(), config.getBucket());
    }

    @Override
    public void onDisable() {
        client.close();
    }

    @Override
    public List<Class<? extends Module>> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Class<InfluxConfig> getConfigType() {
        return InfluxConfig.class;
    }

    @Override
    public InfluxConfig getConfig() {
        return config;
    }

    @Override
    public String getFileName() {
        return "influx.json";
    }

    @Override
    public void loadConfig(InfluxConfig config) {
        this.config = config;
    }

    @Override
    public void saveConfig() {

    }

    @Override
    public boolean saveOnQuit() {
        return false;
    }

    public void writePoints(List<Point> dataPoints){
        try(WriteApi writeApi = client.getWriteApi()){
            writeApi.writePoints(dataPoints);
        }
    }
}
