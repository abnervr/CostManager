package org.abner.manager.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelProperties {

    private static final Map<Class<?>, List<Field>> fields = new HashMap<Class<?>, List<Field>>();

    /**
     *  Todos os Fields que forem encontrados na hierarquia da classe.<br>
     *  Fields com o modificador <code>transient</code> e  <code>static</code> s�o ignorados.
     *  
     * @param model
     * @return fields que ser�o persistidos
     */
    public static List<Field> getFields(Class<?> model) {
        return getFields(model, true);
    }

    public static List<Field> getFields(Class<?> model, boolean ignoreTransient) {

        if (Long.class.isAssignableFrom(model)) {
            return null;
        } else if (fields.containsKey(model)) {
            return fields.get(model);
        }

        List<Field> fields = new ArrayList<Field>();
        Class<?> clazz = model;

        while (clazz != null) {

            for (Field field : clazz.getDeclaredFields()) {

                if ((!ignoreTransient || (field.getModifiers() & Modifier.TRANSIENT) == 0) && (field.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(field);
                }
            }

            clazz = clazz.getSuperclass();
        }

        ModelProperties.fields.put(model, fields);
        return fields;
    }

}
