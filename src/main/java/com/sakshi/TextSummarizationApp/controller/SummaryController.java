package com.sakshi.TextSummarizationApp.controller;

import com.sakshi.TextSummarizationApp.entity.Summary;
import com.sakshi.TextSummarizationApp.repository.SummaryRepository;
import com.sakshi.TextSummarizationApp.service.SummaryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SummaryController {

    private final SummaryService summaryService;
    private final SummaryRepository summaryRepository;

    public SummaryController(SummaryService summaryService,
                             SummaryRepository summaryRepository) {

        this.summaryService = summaryService;
        this.summaryRepository = summaryRepository;
    }

    // Home page
    @GetMapping("/")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/new")
    public String showNewSummaryPage() {
        return "index";
    }

    // Generate summary
    @PostMapping("/summarize")
    public String summarizeText(
            @RequestParam("text") String text,
            Model model) {

        try {

            Summary summary =
                    summaryService.summarizeAndSave(text);

            model.addAttribute("summary", summary);

            return "result";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());

            return "index";

        } catch (Exception e) {

            model.addAttribute(
                    "error",
                    "Something went wrong while generating the summary."
            );

            return "index";
        }
    }

    // History page
    @GetMapping("/history")
    public String showHistory(Model model) {

        List<Summary> summaries =
                summaryRepository.findAllByOrderByCreatedAtDesc();

        model.addAttribute("summaries", summaries);

        return "history";
    }

    @GetMapping("/history/delete/{id}")
    public String deleteSummary(@PathVariable Long id) {

        summaryRepository.deleteById(id);

        return "redirect:/history";
    }
}