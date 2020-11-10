package uk.gov.digital.ho.systemregister;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;

import io.quarkus.jsonb.JsonbConfigCustomizer;

@Singleton
public class SerializerConfig implements JsonbConfigCustomizer {

    @Override
    public void customize(JsonbConfig jsonbConfig) {
        jsonbConfig.withNullValues(true);
    } 
}