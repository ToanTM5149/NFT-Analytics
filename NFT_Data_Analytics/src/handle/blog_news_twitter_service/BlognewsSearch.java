package handle.blog_news_twitter_service;

import java.util.ArrayList;
import java.util.List;

import model.Article;

class BlognewsSearch {
	private List<Article> data;
	
	public BlognewsSearch(List<Article> data) {
		this.data = data;
	}
	
	public List<Article> searchArticlesByTag(String tagArticle) {

        List<Article> result = new ArrayList<>();

        for (Article article : data) {
            if (article == null)
                break;
            boolean foundInTags = false;

            if (article.getTags() != null) {
                for (String tag : article.getTags()) {
                    if (tag != null && tag.toLowerCase().contains(tagArticle.toLowerCase())) {
                        foundInTags = true;
                        break;
                    }
                }
            }
            if (foundInTags) {
                result.add(article);
            } else {
                if (article.getFullContent() != null && article.getFullContent().toLowerCase().contains(tagArticle.toLowerCase())) {
                    result.add(article);
                }
            }
        }
        return result;
    }
}
