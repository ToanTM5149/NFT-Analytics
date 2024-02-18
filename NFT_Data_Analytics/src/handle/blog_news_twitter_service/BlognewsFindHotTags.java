package handle.blog_news_twitter_service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Article;

class BlognewsFindHotTags {
	private List<Article> data;
	
	public BlognewsFindHotTags(List<Article> data) {
		this.data = data;
	}
	
	private List<String> getMostUsedTags(List<Article> articles) {
	    	
        Map<String, Integer> tagCountMap = new HashMap<String, Integer>();
        
        for (Article article : articles) {
        	
            for (String tag : article.getTags()) {
                 if (!tag.toLowerCase().startsWith("#nfts")) {
                 tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
                 }
            }
        }

        List<Map.Entry<String, Integer>> sortedEntries = tagCountMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(Collectors.toList());

        return sortedEntries.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
	
	public List<String> getHotTags(TimePeriodType periodType) {
		switch (periodType) {
        case DAILY:
            List<Article> articlesInDay = data.stream()
                    .filter(article -> article.getPublishDate().isEqual(LocalDate.now()))
                    .collect(Collectors.toList());
            return getMostUsedTags(articlesInDay);
        case WEEKLY:
            LocalDate endOfWeek = LocalDate.now().minusDays(7);
            List<Article> articlesInWeek = data.stream()
                    .filter(article -> article.getPublishDate().isAfter(endOfWeek.minusDays(1))
                            && article.getPublishDate().isBefore(LocalDate.now().plusDays(1)))
                    .collect(Collectors.toList());
            return getMostUsedTags(articlesInWeek);
        case MONTHLY:
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            List<Article> articlesInMonth = data.stream()
                    .filter(article -> article.getPublishDate().isAfter(startOfMonth.minusDays(1))
                            && article.getPublishDate().isBefore(endOfMonth.plusDays(1)))
                    .collect(Collectors.toList());
            return getMostUsedTags(articlesInMonth);
		}
		return new ArrayList<String>();
    }	
}
