package handle.blog_news_twitter_service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Tweet;

class TwitterFindHotTags {
	private List<Tweet> data;
		
	public TwitterFindHotTags(List<Tweet> data) {
		this.data = data;
	}
	
	private List<String> getMostUsedTags(List<Tweet> tweets) {
	    	
        Map<String, Integer> tagCountMap = new HashMap<String, Integer>();
        
        for (Tweet tweet : tweets) {
        	
            for (String tag : tweet.getTags()) {
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
            List<Tweet> tweetsInDay = data.stream()
                    .filter(tweet -> tweet.getDate().isEqual(LocalDate.now()))
                    .collect(Collectors.toList());
            return getMostUsedTags(tweetsInDay);
        case WEEKLY:
            LocalDate endOfWeek = LocalDate.now().minusDays(7);
            List<Tweet> tweetsInWeek = data.stream()
                    .filter(tweet -> tweet.getDate().isAfter(endOfWeek.minusDays(1))
                            && tweet.getDate().isBefore(LocalDate.now().plusDays(1)))
                    .collect(Collectors.toList());
            return getMostUsedTags(tweetsInWeek);
        case MONTHLY:
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
            List<Tweet> tweetsInMonth = data.stream()
                    .filter(tweet -> tweet.getDate().isAfter(startOfMonth.minusDays(1))
                            && tweet.getDate().isBefore(endOfMonth.plusDays(1)))
                    .collect(Collectors.toList());
            return getMostUsedTags(tweetsInMonth);
		}
		return new ArrayList<String>();
    }	
}
