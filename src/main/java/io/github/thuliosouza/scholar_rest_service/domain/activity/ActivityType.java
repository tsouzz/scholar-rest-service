package io.github.thuliosouza.scholar_rest_service.domain.activity;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum ActivityType {

    AC_1    ("Avaliação Contínua 1",  new BigDecimal(0.05)),
    AC_2    ("Avaliação Contínua 2",  new BigDecimal(0.05)),

    LC_1    ("Lição de Casa 1",       new BigDecimal(0.0125)),
    LC_2    ("Lição de Casa 2",       new BigDecimal(0.0125)),
    LC_3    ("Lição de Casa 3",       new BigDecimal(0.0125)),
    LC_4    ("Lição de Casa 4",       new BigDecimal(0.0125)),
    LC_5    ("Lição de Casa 5",       new BigDecimal(0.0125)),
    LC_6    ("Lição de Casa 6",       new BigDecimal(0.0125)),
    LC_7    ("Lição de Casa 7",       new BigDecimal(0.0125)),
    LC_8    ("Lição de Casa 8",       new BigDecimal(0.0125)),

    LW_1    ("Lição Web 1",           new BigDecimal(0.0125)),
    LW_2    ("Lição Web 2",           new BigDecimal(0.0125)),
    LW_3    ("Lição Web 3",           new BigDecimal(0.0125)),
    LW_4    ("Lição Web 4",           new BigDecimal(0.0125)),
    LW_5    ("Lição Web 5",           new BigDecimal(0.0125)),
    LW_6    ("Lição Web 6",           new BigDecimal(0.0125)),
    LW_7    ("Lição Web 7",           new BigDecimal(0.0125)),
    LW_8    ("Lição Web 8",           new BigDecimal(0.0125)),

    TO_MID  ("Teste Oral Mid",        new BigDecimal(0.10)),
    TO_FINAL("Teste Oral Final",      new BigDecimal(0.20)),

    TE_MID  ("Teste Escrito Mid",     new BigDecimal(0.20)),
    TE_FINAL("Teste Escrito Final",   new BigDecimal(0.20));

    private final String label;
    private final BigDecimal weight;

    ActivityType(String label, BigDecimal weight) {
        this.label = label;
        this.weight = weight;
    }

}
