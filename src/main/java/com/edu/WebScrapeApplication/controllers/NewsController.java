package com.edu.imageconversion.controllers;

import com.edu.imageconversion.model.NewsItem;
import com.edu.imageconversion.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<NewsItem> getAggregatedNews() throws ExecutionException, InterruptedException {
        return newsService.getAggregatedNews();
    }
}
