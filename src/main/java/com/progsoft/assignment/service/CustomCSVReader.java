package com.progsoft.assignment.service;

import com.progsoft.assignment.exceptions.MissingDataException;
import com.progsoft.assignment.model.Deal;
import com.progsoft.assignment.model.InValidDeal;
import com.progsoft.assignment.utils.CommonUtils;
import com.univocity.parsers.common.DataProcessingException;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.RetryableErrorHandler;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvRoutines;
import com.univocity.parsers.csv.CsvWriterSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Description("Service to parse CSV file.")
@Service
public class CustomCSVReader {

    /**
     * Read Data from uploaded CSV input stream
     *
     * @param inputStream
     * @param fileName
     * @return Map consists on two list (Valid and Invalid Deals)
     */
    public Map<String, List> read(InputStream inputStream, final String fileName) {
        log.info(CommonUtils.concat("Reading file " + fileName));
        List<Deal> deals;
        List<InValidDeal> inValidDeals = new LinkedList<>();
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            CsvParserSettings parserSettings = this.getParserSettings(fileName, inValidDeals);
            CsvWriterSettings csvWriterSettings = this.getWriterSettings();
            CsvRoutines routines = new CsvRoutines(parserSettings, csvWriterSettings);
            deals = routines.parseAll(Deal.class, inputStreamReader);

        } catch (IOException ie) {
            log.error("IO Exception found", ie);
            throw new MissingDataException("Could not read data from file");
        }
        Map<String, List> results = new HashMap<>();
        results.put("validRecords", deals);
        results.put("inValidRecords", inValidDeals);

        log.info(CommonUtils.concat("Reading process finished: "));

        return results;
    }

    /**
     * Handling invalid records in CSV
     *
     * @param fileName
     * @param inValidDeals
     * @return CSV parser setting object
     */
    private CsvParserSettings getParserSettings(final String fileName, List<InValidDeal> inValidDeals) {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setColumnReorderingEnabled(false);
        settings.detectFormatAutomatically();
        settings.setProcessorErrorHandler(new RetryableErrorHandler<ParsingContext>() {
            @Override
            public void handleError(DataProcessingException error, Object[] inputRow, ParsingContext context) {
                inValidDeals.add(new InValidDeal(String.valueOf(inputRow[0]),
                        String.valueOf(inputRow[1]), String.valueOf(inputRow[2]),
                        String.valueOf(inputRow[3]), BigDecimal.valueOf(Double.valueOf(inputRow[4].toString())), fileName));
            }
        });
        return settings;
    }

    /**
     * @return CSV Writer to write data into Deals object
     */
    private CsvWriterSettings getWriterSettings() {
        CsvWriterSettings writerSettings = new CsvWriterSettings();
        writerSettings.getFormat().setLineSeparator("\r\n");
        writerSettings.getFormat().setDelimiter(';');
        writerSettings.setQuoteAllFields(true);
        return writerSettings;
    }


}