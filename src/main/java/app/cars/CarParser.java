package app.cars;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class CarParser {
    public static class CarNode {
        @JsonProperty("id")
        public String id;
        @JsonProperty("year")
        String year;
        @JsonProperty("model")
        String model;
        @JsonProperty("make")
        String make;
        @JsonProperty("variant")
        String variant;
        @JsonProperty("location")
        String location;
        @JsonProperty("engine")
        String engine;
        @JsonProperty("rego")
        String rego;
        @JsonProperty("gearbox")
        String gearbox;
        @JsonProperty("vin")
        String vin;
        @JsonProperty("owners")
        String owners;
        @JsonProperty("other")
        String other;
        @JsonProperty("colour")
        String colour;
    }
    static Collection<CarNode> parse(final String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        CarNode[] carNodes = objectMapper.readValue(jsonString, CarNode[].class);
        return Arrays.asList(carNodes);
    }

    public static CarNode parseOne(final String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        CarNode carNode = objectMapper.readValue(jsonString, CarNode.class);
        return carNode;
    }

    static String marshall(Collection<CarNode> carNodes) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper.writeValueAsString(carNodes);

    }

}