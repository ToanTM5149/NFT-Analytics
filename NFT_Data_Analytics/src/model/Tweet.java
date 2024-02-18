package model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Tweet {

	private int id;
	private String author;
	private String content;
	private List<String> tags;
	private String imageURL;
	private LocalDate date;

	private static int nextId = 1;

	public Tweet(String author, String content, List<String> tags, String imageURL, LocalDate date) {
		this();
		this.author = author;
		this.content = content;
		this.tags = tags;
		this.imageURL = imageURL;
		this.date = date;
	}

	public Tweet() {
		this.id = nextId;
		nextId++;
	}

	public int getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public List<String> getTags() {
		return tags;
	}

	public String getImageURL() {
		return imageURL;
	}

	public LocalDate getDate() {
		return date;
	}
	
	public String toString() {
		return id + ": " + author + " : " + content + " " + Arrays.toString((tags).stream().toArray()) + " " + date.toString();
	}

}
