package dao.twitter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import helper.JsonIO;
import model.Tweet;

public class DAOTwitter extends ADAOTwitter {
	private static final String PATHSAVEDATA = ".\\data\\twitter\\";

	@Override
	public List<Tweet> getTweets() throws FileNotFoundException {
		Type typeOfTrending = new TypeToken<List<Tweet>>() {}.getType();
		return JsonIO.getInstance().readFromJson(PATHSAVEDATA + "\\twitter.json", typeOfTrending);
	}

	@Override
	public void saveDataToFile(List<Tweet> tweets) throws IOException {
		JsonIO.getInstance().writeToJson(tweets, PATHSAVEDATA + "\\twitter.json");
	}

	@Override
	public void saveDataToFile(List<Tweet> tweets, String nameNFTs) throws IOException {
		JsonIO.getInstance().writeToJson(tweets, PATHSAVEDATA + "\\" + formatNameNFTs(nameNFTs));
	}
	
	@Override
	public void clearData() throws IOException {
		JsonIO.getInstance().clearData(PATHSAVEDATA + "twitter.json");
	}

	private String formatNameNFTs(String name) {
		return name.replace(' ', '_').toLowerCase();
	}
}
