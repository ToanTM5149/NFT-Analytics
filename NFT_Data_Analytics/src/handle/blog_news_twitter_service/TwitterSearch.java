package handle.blog_news_twitter_service;

import java.util.ArrayList;
import java.util.List;

import model.Tweet;

class TwitterSearch {
	private List<Tweet> data;
	
	public TwitterSearch(List<Tweet> data) {
		this.data = data;
	}
	
	public List<Tweet> searchTweetsByTag(String tagTweet) {

        List<Tweet> result = new ArrayList<>();

        for (Tweet tweet : data) {
            if (tweet == null)
                break;
            boolean foundInTags = false;

            if (tweet.getTags() != null) {
                for (String tag : tweet.getTags()) {
                    if (tag != null && tag.toLowerCase().contains(tagTweet.toLowerCase())) {
                        foundInTags = true;
                        break;
                    }
                }
            }
            if (foundInTags) {
                result.add(tweet);
            } else {
                if (tweet.getContent() != null && tweet.getContent().toLowerCase().contains(tagTweet.toLowerCase())) {
                    result.add(tweet);
                }
            }
        }
        return result;
    }

}
