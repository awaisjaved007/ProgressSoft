package com.progsoft.assignment.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
public class InValidDeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uniqueId;
    @Column(name = "from_currency_iso")
    private String fromCurrencyISO;

    @Column(name = "to_currency_iso")
    private String toCurrencyISO;

    @Column(name = "time_stamp")
    private String timestamp;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "file_name")
    private String fileName;

    public InValidDeal(String uniqueId, String fromCurrencyISO,
                       String toCurrencyISO, String timestamp, BigDecimal amount, String fileName) {
        this.uniqueId = uniqueId;
        this.fromCurrencyISO = fromCurrencyISO;
        this.toCurrencyISO = toCurrencyISO;
        this.timestamp = timestamp;
        this.amount = amount;
        this.fileName = fileName;
    }
}
