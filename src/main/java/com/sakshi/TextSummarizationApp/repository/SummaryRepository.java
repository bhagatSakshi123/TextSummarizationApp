package com.sakshi.TextSummarizationApp.repository;

import com.sakshi.TextSummarizationApp.entity.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SummaryRepository extends JpaRepository<Summary, Long> {

    List<Summary> findAllByOrderByCreatedAtDesc();

}