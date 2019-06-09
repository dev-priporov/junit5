package org.junit.jupiter.params.provider;

import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JsonTemplateArgumentsProviderTest {

    @ParameterizedTest
    @JsonTemplateSource({
            @Template(name = "test.json", type = SomeDto.class)
    })
    void validSignatures(SomeDto dto) {
        assertNotNull(dto);
    }

    public static class SomeDto {
        public String getFieldA() {
            return fieldA;
        }

        public void setFieldA(String fieldA) {
            this.fieldA = fieldA;
        }

        public Integer getFieldB() {
            return fieldB;
        }

        public void setFieldB(Integer fieldB) {
            this.fieldB = fieldB;
        }

        private String fieldA;
        private Integer fieldB;
    }
}