package com.progsoft.assignment.service;

import com.progsoft.assignment.exceptions.MissingDataException;
import com.progsoft.assignment.model.Deal;
import com.progsoft.assignment.model.Statistics;
import com.progsoft.assignment.repository.DealJpaRepository;
import com.progsoft.assignment.repository.StatisticsRepository;
import com.progsoft.assignment.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Description("Service to process Fx Deals CSV file")
@Service
public class FileProcessingServiceImpl implements FileProcessingService {

    private CustomCSVReader customCSVReader;
    private DealJpaRepository dealJpaRepository;
    private StatisticsRepository statisticsRepository;


    @Autowired
    public FileProcessingServiceImpl(CustomCSVReader customCSVReader,
                                     DealJpaRepository dealJpaRepository,
                                     StatisticsRepository statisticsRepository) {
        this.customCSVReader = customCSVReader;
        this.dealJpaRepository = dealJpaRepository;
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * @param inputStream of CSV file
     * @param fileName    uploaded Csv file name
     * @return return Map with Valid and Invalid Deals count and Time duration of file upload
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map process(final InputStream inputStream, final String fileName) {
        final long startTime = System.nanoTime();

        if (isFileAlreadyProcessed(fileName)) throw new MissingDataException("File name already exists");

        /*Reading CSV file*/
        Map<String, List> result = this.customCSVReader.read(inputStream, fileName);

        /*Appending fileName in ValidRecords and counting deals on the basis of ISO code of currency*/
        Map isoCodeDealCountMap = this.setProjectIdAndCountByISOCode(result.get("validRecords"), fileName);

        /*Saving Data into DB*/
        this.dealJpaRepository.saveDeals(result);

        /*Saving ISO currency deals count*/
        this.updateStatics(isoCodeDealCountMap);

        /*Converting nano seconds to seconds by dividing 1000000000d*/
        final double duration = (System.nanoTime() - startTime) / 1000000000d;
        log.info("Processing end at:" + duration);

        return ResponseBuilder.create()
                .put("dealsCount", result.get("validRecords").size())
                .put("invalidDealsCount", result.get("inValidRecords").size())
                .put("processingTime", duration).build();
    }

    /**
     * Assigning filename and calculation of Deals count for ISO code
     *
     * @param deals    object to update property of file name
     * @param fileName uploaded Csv file name
     * @return Map with count of deal group by From ISO Code.
     * ISO code will be key and it's count will be value
     */
    private Map<String, Integer> setProjectIdAndCountByISOCode(final List<Deal> deals, final String fileName) {
        final Map<String, Integer> isoCodeDealCountMap = new HashMap<>();
        deals.forEach(deal -> {
            deal.setFileName(fileName);
            isoCodeDealCountMap.merge(deal.getFromCurrencyISO(), 1, Integer::sum);
        });
        return isoCodeDealCountMap;
    }

    /**
     * Validate if file already uploaded or not in data base
     *
     * @param fileName uploaded Csv file name
     * @return True or False on the base of validation
     */
    @Transactional(readOnly = true)
    Boolean isFileAlreadyProcessed(final String fileName) {
        return dealJpaRepository.getFirstByFileName(fileName).isPresent();
    }

    /**
     * Update Statistics for ISO codes Deals count
     *
     * @param data ISO code with it count for current uploaded file
     */
    @Transactional
    void updateStatics(Map<String, Integer> data) {
        List<Statistics> statisticsList = statisticsRepository.findAll();
        Map<String, Statistics> existingRecords = statisticsList.stream().
                collect(Collectors.toMap(Statistics::getIsoCode, Function.identity()));
        statisticsList.clear();
        data.forEach((k, v) -> {
            if (existingRecords.containsKey(k)) {
                Statistics statistics = existingRecords.get(k);
                statistics.setDealCount(statistics.getDealCount() + v);
                statisticsList.add(statistics);
            } else {
                statisticsList.add(new Statistics(k, v));
            }
        });
        this.statisticsRepository.saveAll(statisticsList);
    }

    /**
     * Fetch all File names to display on UI
     *
     * @return List of File Names exists for Deals
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> fetchAllFileNames() {
        return dealJpaRepository.findDistinctFileNames();
    }

    /**
     * Getting all valid deals for specific file name
     *
     * @param fileName Uploaded Csv file name
     * @return List of valid deal objects
     */
    @Override
    @Transactional(readOnly = true)
    public List<Deal> fetchAllByFileName(String fileName) {
        return dealJpaRepository.findAllByFileName(fileName);
    }
}
