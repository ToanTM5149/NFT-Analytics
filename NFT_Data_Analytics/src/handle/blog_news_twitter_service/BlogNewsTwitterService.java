 package handle.blog_news_twitter_service;

import java.util.ArrayList;
import java.util.List;

import dao.ServiceLocator;
import dao.blog_news.ADAOBlognews;
import dao.twitter.ADAOTwitter;
import model.Article;
import model.Tweet;

public class BlogNewsTwitterService implements IBlogNewsTwitterService{
	
	private ADAOBlognews DBArticle;
	private ADAOTwitter DBTwitter;
	private List<Tweet> tweets;
	private List<Article> aritcles; 
	private TwitterSearch twitterSearch;
	private TwitterFindHotTags twitterFindHotTags;
	private BlognewsSearch blognewsSearch;
	private BlognewsFindHotTags blognewsFindHotTags;
	
	public BlogNewsTwitterService() {
		DBArticle = (ADAOBlognews) ServiceLocator.getService("DAOBlognews");
		DBTwitter = (ADAOTwitter) ServiceLocator.getService("DAOTwitter");
		try {
			tweets = DBTwitter.getTweets();
			aritcles = DBArticle.getArticles();
		} catch (Exception e) {
			e.printStackTrace();
			tweets = new ArrayList<Tweet>();
			aritcles = new ArrayList<Article>();
		}
		twitterSearch = new TwitterSearch(tweets);
		twitterFindHotTags = new TwitterFindHotTags(tweets);
		blognewsSearch = new BlognewsSearch(aritcles);
		blognewsFindHotTags = new BlognewsFindHotTags(aritcles);
	}
	
	@Override
	public List<String> getHotTagsBlogNews(TimePeriodType periodType) {
		return blognewsFindHotTags.getHotTags(periodType);
	}

	@Override
	public List<String> getHotTagsTwitter(TimePeriodType periodType) {
		return twitterFindHotTags.getHotTags(periodType);
	}

	@Override
	public List<Tweet> getAllTweets() {
		return tweets;
	}
	
	@Override
	public List<Article> getAllArticles() {
		return aritcles;
	}

	@Override
	public List<Tweet> getTweetsByTag(String tag) {
		return twitterSearch.searchTweetsByTag(tag);
	}

	@Override
	public List<Article> getArticlesByTag(String tag) {
		return blognewsSearch.searchArticlesByTag(tag);
	}
	
}
