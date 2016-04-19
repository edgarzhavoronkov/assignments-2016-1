package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.util.*;


public final class Injector {
    private Injector() {
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        Class<?> clazz = Class.forName(rootClassName);
        Constructor<?> constructor = clazz.getConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        List<Object> parameters = new ArrayList<>();

        Map<Class, List<Class>> dependenciesGraph = new HashMap<>();

        for (Class<?> parameterType : parameterTypes) {
            if (!parameterType.isInterface()) {
                Constructor<?> parameterConstructor = parameterType.getConstructors()[0];
                Class<?>[] parameterParameterTypes = parameterConstructor.getParameterTypes();
                dependenciesGraph.put(parameterType, Arrays.asList(parameterParameterTypes));
            }
        }

        //cycle deps
        for (Class key : dependenciesGraph.keySet()) {
            List<Class> deps = dependenciesGraph.get(key);

            for (Class anotherKey : dependenciesGraph.keySet()) {
                if (!key.equals(anotherKey) && deps.contains(anotherKey)) {
                    throw new InjectionCycleException();
                }
            }
        }

        for (Class<?> parameterType : parameterTypes) {
            int implementationsCount = 0;

            for (String implementationClassName : implementationClassNames) {
                Class<?> implementationClass = Class.forName(implementationClassName);
                if (parameterType.isAssignableFrom(implementationClass)) {
                    implementationsCount++;
                    if (!parameters.add(implementationClass.newInstance())) {
                        throw new AmbiguousImplementationException();
                    }
                }
            }

            if (implementationsCount == 0) {
                throw new ImplementationNotFoundException();
            }
        }


        return constructor.newInstance(parameters.toArray());
    }
}
