package com.dimotim.kubSolver;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Uzors {
    private static Uzors instance = null;
    private final Map<String, Solution> uzors;

    @SneakyThrows
    public static synchronized Uzors getInstance() {
        if(instance == null) {
            instance  = new Uzors();
        }

        return instance;
    }

    public Map<String, Solution> getUzors(){
        return new HashMap<>(uzors);
    }

    private Uzors() throws IOException {
        Properties properties = new Properties();
        try(InputStream is = Uzors.class.getResourceAsStream("/uzors.properties"); InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            properties.load(isr);
        }

        uzors = properties.entrySet().stream()
                .collect(Collectors.toMap(
                        kv -> kv.getKey().toString(),
                        kv -> Solution.parse(kv.getValue().toString())
                ));
    }
}
