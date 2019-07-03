package com.progsoft.assignment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iso_code", columnDefinition = "varchar(25) unique default 0")
    private String isoCode;

    @Column(name = "deal_count", nullable = false, columnDefinition = "integer default 0")
    private Integer dealCount;

    public Statistics(String isoCode, Integer dealCount) {
        this.isoCode = isoCode;
        this.dealCount = dealCount;
    }
}
