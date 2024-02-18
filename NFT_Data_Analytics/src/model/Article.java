package model;
import java.time.LocalDate;
import java.util.List;

public class Article {
	private static int idCounter = 1;

	private int id;
    private String title;
    private String absoluteURL;
    private String fullContent;
    private List<String> tags;
    private LocalDate publishDate;
    
    public Article() {
        this.id = idCounter++;
    }

    public Article(String title, String absoluteURL, String fullContent, List<String> tags, LocalDate publishDate) {
        this.id = idCounter++;
        this.title = title;
        this.absoluteURL = absoluteURL;
        this.fullContent = fullContent;
        this.tags = tags;
        this.publishDate = publishDate;
    }
    
    // Getters/Setters
    public int getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbsoluteURL() {
        return absoluteURL;
    }

    public void setAbsoluteURL(String absoluteURL) {
        this.absoluteURL = absoluteURL;
    }
	
    public String getFullContent() {
		return fullContent;
	}

	public void setFullContent(String fullContent) {
		this.fullContent = fullContent;
	}

	public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }
}


