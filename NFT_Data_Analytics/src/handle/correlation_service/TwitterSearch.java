package handle.correlation_service;

import java.util.ArrayList;
import java.util.List;

import dao.ServiceLocator;
import dao.twitter.ADAOTwitter;
import model.Article;
import model.CollectionFilter;
import model.Tweet;

public class TwitterSearch {
	private ADAOTwitter DBTwitter;
	private List<String> keywords = new ArrayList<String>();
	
	public TwitterSearch() {
		DBTwitter = (ADAOTwitter) ServiceLocator.getService("DAOTwitter");
		keywords.add("trader");
		keywords.add("change");
		keywords.add("fluctuate");
	}
	
	public List<Tweet> getTweetByCollectionFilter(CollectionFilter col) {
		try {
			List<Tweet> tweets = DBTwitter.getTweets();
			List<Tweet> result = new ArrayList<Tweet>();
			for (Tweet tweet : tweets) {
	            if (tweet == null)
	                break;
	            
	            String content = tweet.getContent();
	            if(content.contains(col.getName()) || content.contains(col.getMarketplaceName())) {
	            	for(String key : keywords) {
	            		if(content.contains(key)) {	            			
	            			result.add(tweet);
	            		}
	            		break;
	            	}
	            	result.add(tweet);
	            }
	        }
			return result;
		} catch (Exception e) {
			return new ArrayList<Tweet>();
		}
	}

	

}
