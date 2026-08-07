package io.github.thuliosouza.scholar_rest_service.domain.classgroup;

import lombok.Getter;

@Getter
public enum Module {

    BASIC_1       ("Básico 1",          "BAS1"),
    BASIC_2       ("Básico 2",          "BAS2"),

    INTERMEDIATE_1("Intermediário 1",   "INT1"),
    INTERMEDIATE_2("Intermediário 2",   "INT2"),

    PRE_ADVANCED_1("Pré Avançado 1",    "PADV1"),
    PRE_ADVANCED_2("Pré Avançado 2",    "PADV2"),

    ADVANCED_1    ("Avançado 1",        "ADV1"),
    ADVANCED_2    ("Avançado 2",        "ADV2"),

    MASTER_1      ("Master 1",          "MAST1"),
    MASTER_2      ("Master 2",          "MAST2");

    private final String name;
    private final String acronym;

    Module(String name, String acronym) {
        this.name = name;
        this.acronym = acronym;
    }

}
