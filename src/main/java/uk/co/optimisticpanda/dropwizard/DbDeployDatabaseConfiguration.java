package uk.co.optimisticpanda.dropwizard;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.yammer.dropwizard.db.DatabaseConfiguration;

public class DbDeployDatabaseConfiguration extends DatabaseConfiguration {

    @JsonProperty
    private Map<String,String> scripts = Maps.newHashMap();
    
    @JsonProperty
    DbDeployProperties dbdeploy;
    
    public Map<String, String> getScripts() {
        return scripts;
    }

    public String getScript(String script) {
        return scripts.get(script);
    }
    
    public DbDeployProperties getDbdeploy() {
        return dbdeploy;
    }
}
