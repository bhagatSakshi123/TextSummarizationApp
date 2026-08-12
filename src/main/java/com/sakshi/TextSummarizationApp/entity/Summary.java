package com.sakshi.TextSummarizationApp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "summaries")
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String originalText;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String generatedSummary;

    private LocalDateTime createdAt;

    public Summary() {
    }

    public Summary(String originalText, String generatedSummary) {
        this.originalText = originalText;
        this.generatedSummary = generatedSummary;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getGeneratedSummary() {
        return generatedSummary;
    }

    public void setGeneratedSummary(String generatedSummary) {
        this.generatedSummary = generatedSummary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getOriginalLength() {
        return originalText != null ? originalText.length() : 0;
    }

    public int getSummaryLength() {
        return generatedSummary != null ? generatedSummary.length() : 0;
    }

    public double getCompressionPercentage() {

        if (getOriginalLength() == 0) {
            return 0;
        }

        return ((double) (getOriginalLength() - getSummaryLength())
                / getOriginalLength()) * 100;
    }
}