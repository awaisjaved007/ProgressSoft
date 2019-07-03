package com.progsoft.assignment.service;

import com.progsoft.assignment.exceptions.MissingDataException;
import com.progsoft.assignment.model.Deal;
import com.progsoft.assignment.model.Statistics;
import com.progsoft.assignment.repository.DealJpaRepository;
import com.progsoft.assignment.repository.DealRepository;
import com.progsoft.assignment.repository.StatisticsRepository;
import com.progsoft.assignment.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
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

    @Override
    public Map process(final InputStream inputStream, final String fileName) {
        final long startTime = System.nanoTime();

        if (isFileAlreadyProcessed(fileName)) throw new MissingDataException("File name already exists");

        /*Reading CSV file*/
        Map<String, List> result = this.customCSVReader.read(inputStream, fileName);

        /*Appending fileName in ValidRecords and counting deals on the basis of ISO code of currency*/
        Map isoCodeDealCountMap = this.setProjectIdAndCountByISOCode(result.get("validRecords"), fileName);

        /*Saving Data into DB*/
        this.dealJpaRepository.publish(result);

        /*Saving ISO currency deals count*/
        this.updateStatics(isoCodeDealCountMap);

        final double duration = (System.nanoTime() - startTime) / 1000000000d;
        log.info("Processing end at:" + duration);

        return ResponseBuilder.create()
                .put("DealsCount", result.get("validRecords").size())
                .put("InvalidDealsCount", result.get("inValidRecords").size())
                .put("ProcessingTime", duration).build();
    }

    private Map<String, Integer> setProjectIdAndCountByISOCode(final List<Deal> deals, final String fileName) {
        final Map<String, Integer> isoCodeDealCountMap = new HashMap<>();
        deals.forEach(deal -> {
            deal.setFileName(fileName);
            isoCodeDealCountMap.merge(deal.getFromCurrencyISO(), 1, Integer::sum);
        });
        return isoCodeDealCountMap;
    }

    @Transactional(readOnly = true)
    Boolean isFileAlreadyProcessed(final String fileName) {
        return dealJpaRepository.getFirstByFileName(fileName).isPresent() ? true : false;
    }

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

    @Override
    public List<String> fetchAllFileNames() {
        return dealJpaRepository.findDistinctFileNames();
    }

    @Override
    public List<Deal> fetchAllByFileName(String fileName) {
        return dealJpaRepository.findAllByFileName(fileName);
    }
}
