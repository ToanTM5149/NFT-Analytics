package handle.correlation_service;

import java.util.List;
import java.util.Set;

import model.Article;
import model.CollectionFilter;
import model.Tweet;

public class CorrelationService implements ICorrelationService{	
	private MarketplaceSearch marketplace;
	private BlognewsSearch blognews;
	private TwitterSearch twitter;
	
	public CorrelationService() {
		marketplace = new MarketplaceSearch();
		blognews = new BlognewsSearch();
		twitter = new TwitterSearch();
	}
	
	@Override
	public Set<CollectionFilter> filterCollectionByName(String name) {
		return marketplace.filterCollectionByName(name);
		
	}
	
	@Override
	public List<Article> getArticleByCollectionFilter(CollectionFilter col) {
		return blognews.getArticleByCollectionFilter(col);
	}
	
	@Override
	public List<Tweet> getTweetByCollectionFilter(CollectionFilter col) {
		return twitter.getTweetByCollectionFilter(col);
	}
}
