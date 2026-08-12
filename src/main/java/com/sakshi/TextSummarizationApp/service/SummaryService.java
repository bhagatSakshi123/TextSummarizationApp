package com.sakshi.TextSummarizationApp.service;

import com.sakshi.TextSummarizationApp.ai.SummarizationModel;
import com.sakshi.TextSummarizationApp.entity.Summary;
import com.sakshi.TextSummarizationApp.repository.SummaryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummaryService {

    private final SummarizationModel summarizationModel;
    private final SummaryRepository summaryRepository;

    // BART ko bahut bada text ek baar mein nahi bhejna.
    // Isliye article ko approximately 3000 characters ke chunks mein divide karenge.
    private static final int CHUNK_SIZE = 3000;

    public SummaryService(SummarizationModel summarizationModel,
                          SummaryRepository summaryRepository) {

        this.summarizationModel = summarizationModel;
        this.summaryRepository = summaryRepository;
    }

    public Summary summarizeAndSave(String originalText) {

        // Input validation
        if (originalText == null || originalText.isBlank()) {
            throw new IllegalArgumentException(
                    "Text cannot be empty."
            );
        }

        if (originalText.trim().length() < 200) {
            throw new IllegalArgumentException(
                    "Please enter at least 200 characters."
            );
        }

        try {

            // Generate summary
            String generatedSummary =
                    summarizeLongText(originalText);

            if (generatedSummary == null ||
                    generatedSummary.isBlank()) {

                throw new RuntimeException(
                        "AI could not generate a summary."
                );
            }

            // Create Summary object
            Summary summary = new Summary(
                    originalText,
                    generatedSummary
            );

            // Save in MySQL
            return summaryRepository.save(summary);

        } catch (IllegalArgumentException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to generate summary. Please try again.",
                    e
            );
        }
    }

    private String summarizeLongText(String text) {

        // If text is small enough, send it directly to BART.
        if (text.length() <= CHUNK_SIZE) {

            return summarizationModel.generateSummary(text);
        }

        // Long text → divide into chunks
        List<String> chunks = splitIntoChunks(text);

        List<String> chunkSummaries = new ArrayList<>();

        // Summarize every chunk
        for (String chunk : chunks) {

            String summary =
                    summarizationModel.generateSummary(chunk);

            chunkSummaries.add(summary);
        }

        // Combine all chunk summaries
        String combinedSummary =
                String.join(" ", chunkSummaries);

        // If combined summary is still too long,
        // summarize it again.
        if (combinedSummary.length() > CHUNK_SIZE) {

            return summarizeLongText(combinedSummary);
        }

        // Final summary
        return summarizationModel.generateSummary(combinedSummary);
    }

    private List<String> splitIntoChunks(String text) {

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < text.length()) {

            int end = Math.min(
                    start + CHUNK_SIZE,
                    text.length()
            );

            // Try to end the chunk at a space
            // instead of cutting a word in half.
            if (end < text.length()) {

                int lastSpace = text.lastIndexOf(" ", end);

                if (lastSpace > start) {
                    end = lastSpace;
                }
            }

            String chunk = text.substring(start, end).trim();

            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            start = end;
        }

        return chunks;
    }
}