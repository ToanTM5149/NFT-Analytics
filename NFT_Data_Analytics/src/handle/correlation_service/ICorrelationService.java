package handle.correlation_service;

import java.util.List;
import java.util.Set;

import model.Article;
import model.CollectionFilter;
import model.Tweet;

public interface ICorrelationService {
	Set<CollectionFilter> filterCollectionByName(String name);
	List<Article> getArticleByCollectionFilter(CollectionFilter col);
	List<Tweet> getTweetByCollectionFilter(CollectionFilter col);
}
