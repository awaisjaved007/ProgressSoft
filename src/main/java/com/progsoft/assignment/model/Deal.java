package com.progsoft.assignment.model;

import com.univocity.parsers.annotations.Format;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Validate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@Entity
public class Deal {

    @Id
    @Parsed
    @Validate
    @Column(name = "id")
    private Long uniqueDealId;

    @Parsed
    @Validate
    @Column(name = "from_currency_iso")
    private String fromCurrencyISO;

    @Parsed
    @Validate
    @Column(name = "to_currency_iso")
    private String toCurrencyISO;

    @Parsed
    @Format(formats = "dd/MM/yyyy")
    @Validate
    @Column(name = "time_stamp")
    private Date date;

    @Parsed
    @Validate
    @Column(name = "amount")
    private String amount;

    @Column(name = "file_name")
    private String fileName;

}
