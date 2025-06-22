package com.edu.imageconversion.services;

import com.edu.imageconversion.model.NewsItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface NewsService {
    List<NewsItem> getAggregatedNews() throws ExecutionException, InterruptedException;
}
