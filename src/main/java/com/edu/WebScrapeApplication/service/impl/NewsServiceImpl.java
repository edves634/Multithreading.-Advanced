package com.edu.imageconversion.services.impl;

import com.edu.imageconversion.model.NewsApiResponse;
import com.edu.imageconversion.model.NewsItem;
import com.edu.imageconversion.service.NewsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;

@Service
public class NewsServiceImpl implements NewsService {

    private final ExecutorService executorService;
    private final RestTemplate restTemplate;

    private final List<String> newsSources = Arrays.asList(
            "https://newsapi.org/v2/top-headlines?country=ru&apiKey=API_KEY",
            "https://newsapi.org/v2/everything?domains=bbc.co.uk&apiKey=API_KEY",
            "https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=API_KEY"
    );

    public NewsServiceImpl(ExecutorService executorService, RestTemplate restTemplate) {
        this.executorService = executorService;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<NewsItem> getAggregatedNews() throws ExecutionException, InterruptedException {
        List<Future<List<NewsItem>>> futures = new ArrayList<>();

        for (String source : newsSources) {
            Callable<List<NewsItem>> task = () -> fetchNewsFromSource(source);
            futures.add(executorService.submit(task));
        }

        List<NewsItem> allNews = new ArrayList<>();
        for (Future<List<NewsItem>> future : futures) {
            allNews.addAll(future.get());
        }

        allNews.sort(Comparator.comparing(NewsItem::getPublishedAt).reversed());
        return allNews;
    }

    private List<NewsItem> fetchNewsFromSource(String apiUrl) {
        try {
            NewsApiResponse response = restTemplate.getForObject(apiUrl, NewsApiResponse.class);
            return response != null ? response.getArticles() : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching news from " + apiUrl + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
