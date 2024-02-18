package dao.twitter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import dao.DAOService;
import model.Tweet;

public abstract class ADAOTwitter implements DAOService{
	
	@Override
	public String getName() {
		return "DAOTwitter";
	}
	
	public abstract List<Tweet> getTweets() throws FileNotFoundException;
	public abstract void saveDataToFile(List<Tweet> tweets) throws IOException;
	public abstract void saveDataToFile(List<Tweet> tweets, String nameNFTs) throws IOException; 
	public abstract void clearData() throws IOException;
}
