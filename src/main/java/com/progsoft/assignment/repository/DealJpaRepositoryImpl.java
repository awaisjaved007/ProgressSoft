package com.progsoft.assignment.repository;

import com.progsoft.assignment.model.Deal;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DealJpaRepositoryImpl implements DealJpaRepository {

    private DealRepository dealRepository;

    @Autowired
    public DealJpaRepositoryImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    @Value("${hibernate.jdbc.batch_size}")
    private Integer HIBERNATE_BATCH_SIZE;

    @Override
    public Boolean saveDeals(Map<String, List> records) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.unwrap(Session.class).setJdbcBatchSize(HIBERNATE_BATCH_SIZE);
        entityManager.getTransaction().begin();
        records.get("validRecords").forEach(deal -> entityManager.persist(deal));
        records.get("inValidRecords").forEach(invalidDeal -> entityManager.persist(invalidDeal));
        entityManager.getTransaction().commit();
        return Boolean.TRUE;
    }

    @Override
    public Optional<Deal> getFirstByFileName(final String fileName) {
        return dealRepository.getFirstByFileName(fileName);
    }

    @Override
    public List<String> findDistinctFileNames() {
        return dealRepository.findDistinctFileNames();
    }

    @Override
    public List<Deal> findAllByFileName(String fileName) {
        return dealRepository.findAllByFileName(fileName);
    }
}
