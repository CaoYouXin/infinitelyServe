package tech.caols.infinitely.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PrePostConfigTest {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        PreConfig preConfig = new PreConfig();
        preConfig.setHostName("localhost");
        preConfig.setPort(9000);
        preConfig.setParameters(Arrays.asList("a", "b"));
        preConfig.setNeedBody(true);
        preConfig.setRegexStr(".*");
        String string = objectMapper.writeValueAsString(preConfig);
        System.out.println(string);
        PreConfig config = objectMapper.readValue(string, PreConfig.class);
        System.out.println(config);
    }

}
