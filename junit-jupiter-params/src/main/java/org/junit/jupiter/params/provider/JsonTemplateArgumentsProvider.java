/*
 * Copyright 2015-2019 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.params.provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @since 5.0
 */
class JsonTemplateArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<JsonTemplateSource> {

    private final BiFunction<Class<?>, String, InputStream> inputStreamProvider = Class::getResourceAsStream;

    private Object[] arguments;
    private List<Pair> pairs = new ArrayList<>();

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Class<?> testClass = context.getTestClass().orElseThrow(() -> new IllegalArgumentException(""));


        List<Arguments> collect = pairs.stream()
                .map(this::mapToObject)
                .map(Arguments::of)
                .collect(Collectors.toList());

        System.out.println(collect);
        return collect.stream();
    }

    private Object mapToObject(Pair it) {
        try {
            InputStream inputStream = inputStreamProvider.apply(this.getClass(), "/test.json");

            return OBJECT_MAPPER.readValue(inputStream, it.aClass);
        } catch (IOException e) {
            throw new RuntimeException("asdasddas");
        }
    }

    @Override
    public void accept(JsonTemplateSource jsonTemplateSource) {
        Arrays.stream(jsonTemplateSource.value())
                .forEach(template -> pairs.add(new Pair(template.type(), template.name())));

        arguments = new String[]{"aaa"};
    }

    @Override
    public Consumer<JsonTemplateSource> andThen(Consumer<? super JsonTemplateSource> after) {
        return null;
    }

    static class Pair {
        final Class<?> aClass;
        final String name;

        public Pair(Class<?> aClass, String name) {
            this.aClass = aClass;
            this.name = name;
        }
    }
}
