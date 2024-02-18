package handle.correlation_service;

import java.util.ArrayList;
import java.util.List;

import dao.ServiceLocator;
import dao.blog_news.ADAOBlognews;
import model.Article;
import model.CollectionFilter;

public class BlognewsSearch {
	private ADAOBlognews DBBlognews;
	private List<String> keywords = new ArrayList<String>();
	public BlognewsSearch() {
		DBBlognews = (ADAOBlognews) ServiceLocator.getService("DAOBlognews");
		keywords.add("trader");
		keywords.add("change");
		keywords.add("fluctuate");
		keywords.add("sold");
		keywords.add("NFTs");
	}
	
	public List<Article> getArticleByCollectionFilter(CollectionFilter col) {
		try {
			List<Article> articles = DBBlognews.getArticles();
			List<Article> result = new ArrayList<Article>();
			for (Article article : articles) {
	            if (article == null)
	                break;
	            
	            String content = article.getFullContent();
	            if(content.contains(col.getName()) || content.contains(col.getMarketplaceName()) || true) {
	            	for(String key : keywords) {
	            		content.contains(key);
	            		result.add(article);
	            		break;
	            	}	
	            	result.add(article);
	            }
	        }
			System.out.println(result.size());
			return result;
		} catch (Exception e) {
			return new ArrayList<Article>();
		}
	}


}
