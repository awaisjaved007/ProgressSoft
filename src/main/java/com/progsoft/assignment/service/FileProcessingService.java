package com.progsoft.assignment.service;

import com.progsoft.assignment.model.Deal;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface FileProcessingService {
    Map process(final InputStream inputStream, final String fileName);
    List<String> fetchAllFileNames();
    List<Deal> fetchAllByFileName(final String fileName);
}
