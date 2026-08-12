package com.sakshi.TextSummarizationApp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SummaryRequest {

    @NotBlank(message = "Text cannot be empty")
    @Size(min = 50, message = "Please enter at least 50 characters")
    private String text;

    public SummaryRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}