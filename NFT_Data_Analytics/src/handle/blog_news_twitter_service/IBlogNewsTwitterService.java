package handle.blog_news_twitter_service;

import java.util.List;

import model.Article;
import model.Tweet;

public interface IBlogNewsTwitterService {
	List<String> getHotTagsBlogNews(TimePeriodType periodType);
	List<String> getHotTagsTwitter(TimePeriodType periodType);
	List<Tweet> getAllTweets();
	List<Article> getAllArticles();
	List<Tweet> getTweetsByTag(String tag);
	List<Article> getArticlesByTag(String tag);
}
