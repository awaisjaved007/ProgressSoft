package com.progsoft.assignment;

import com.progsoft.assignment.exceptions.MissingDataException;
import com.progsoft.assignment.model.Deal;
import com.progsoft.assignment.repository.DealJpaRepository;
import com.progsoft.assignment.repository.StatisticsRepository;
import com.progsoft.assignment.service.CustomCSVReader;
import com.progsoft.assignment.service.FileProcessingService;
import com.progsoft.assignment.service.FileProcessingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class FileProcessingServiceTest {
    private final CustomCSVReader customCSVReader = new CustomCSVReader();
    private final StatisticsRepository statisticsRepository = mock(StatisticsRepository.class);
    private final DealJpaRepository dealJpaRepository = mock(DealJpaRepository.class);

    private final FileProcessingService fileProcessingService = new FileProcessingServiceImpl(customCSVReader,
            dealJpaRepository,
            statisticsRepository);


    @Before
    public void setUp() {

    }

    @Test(expected = MissingDataException.class)
    public void whenUploadFileNameAlreadyExists_Return() {
        Deal deal = new Deal();
        Optional<Deal> optionalDeal = Optional.of(deal);
        Mockito.when(this.dealJpaRepository.getFirstByFileName("test.csv")).thenReturn(optionalDeal);
        InputStream inputStream = this.getClass().getResourceAsStream("/src/test/resources/testdata/test.csv");
        fileProcessingService.process(inputStream, "test.csv");
    }

    @Test()
    public void whenUploadNewFileName_ReturnMap() throws FileNotFoundException {
        Optional<Deal> optionalDeal = Optional.empty();
        FileInputStream inputStream = new FileInputStream(this.getClass().getResource("/test.csv").getPath());
        Mockito.when(this.dealJpaRepository.getFirstByFileName(anyString())).thenReturn(optionalDeal);
        Mockito.when(this.dealJpaRepository.saveDeals(anyMap())).thenReturn(Boolean.TRUE);
        Mockito.when(this.statisticsRepository.saveAll(anyList())).thenReturn(Mockito.anyList());
        Map response = fileProcessingService.process(inputStream, "test.csv");
        assert response != null;
    }
}
