package com.progsoft.assignment.repository;

import com.progsoft.assignment.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    Optional<Deal> getFirstByFileName(final String fileName);

    @Query("select distinct fileName from Deal")
    List<String> findDistinctFileNames();

    List<Deal> findAllByFileName(final String fileName);
}
