package ru.egormit.starship.api.util;

import javax.naming.InitialContext;

public class JndiUtils {
    @SuppressWarnings("unchecked")
    public static <T> T getFromContext(Class<T> clazz, String path) {
        try {
            return (T) new InitialContext().lookup(path);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to retrieve item from context. Path = "+path);
        }
    }
}
