package com.woozy.carrentals.bdd.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScenarioContext {
    private static final ThreadLocal<Map<String, Object>> scenarioContext = ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, Object value) {
        scenarioContext.get().put(key, value);
    }

    public static Object get(String key) {
        return scenarioContext.get().get(key);
    }

    public static void remove(String key) {
        scenarioContext.get().remove(key);
    }

    public static void clear() {
        scenarioContext.remove();
    }

    public static int getContextSize() {
        return scenarioContext.get().size();
    }
}