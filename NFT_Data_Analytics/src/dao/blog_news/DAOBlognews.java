package dao.blog_news;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import helper.JsonIO;
import model.Article;

public class DAOBlognews extends ADAOBlognews{

	private static final String PATHSAVEDATA = ".\\data\\blog_news\\blog_news.json";
	
	@Override
	public List<Article> getArticles() throws FileNotFoundException {
		Type typeOfTrending = new TypeToken<List<Article>>() {}.getType();
		return JsonIO.getInstance().readFromJson(PATHSAVEDATA, typeOfTrending);
	}

	@Override
	public void saveDataToFile(List<Article> articles) throws IOException {
		JsonIO.getInstance().writeToJson(articles, PATHSAVEDATA);
	}

	@Override
	public void clearData() throws IOException {
		 JsonIO.getInstance().clearData(PATHSAVEDATA);
	}
	
}
