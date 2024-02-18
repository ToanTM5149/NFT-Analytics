package dao.blog_news;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dao.DAOService;
import model.Article;

public abstract class ADAOBlognews implements DAOService{
	
	@Override
	public String getName() {
		return "DAOBlognews";
	}
	
	public abstract List<Article> getArticles() throws FileNotFoundException;
	public abstract void saveDataToFile(List<Article> articles) throws IOException;
	public abstract void clearData() throws IOException;
}
