package com.woozy.carrentals.bdd.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {
    private final ThreadLocal<Map<ContextItem, Object>> context = ThreadLocal.withInitial(HashMap::new);

    public void set(ContextItem key, Object value) {
        context.get().put(key, value);
    }

    public <T> T get(ContextItem key, Class<T> type) {
        return type.cast(context.get().get(key));
    }

    public void remove(ContextItem key) {
        context.get().remove(key);
    }

    public void clear() {
        context.remove();
    }

    public int getContextSize() {
        return context.get().size();
    }
}