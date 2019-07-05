package com.progsoft.assignment.repository;

import com.progsoft.assignment.model.Deal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DealJpaRepository {
    Boolean saveDeals(Map<String, List> records);

    Optional<Deal> getFirstByFileName(final String fileName);

    List<String> findDistinctFileNames();

    List<Deal> findAllByFileName(final String fileName);
}
