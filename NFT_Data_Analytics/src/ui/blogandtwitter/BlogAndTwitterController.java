package ui.blogandtwitter;
 
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import handle.blog_news_twitter_service.BlogNewsTwitterService;
import handle.blog_news_twitter_service.IBlogNewsTwitterService;
import handle.blog_news_twitter_service.TimePeriodType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Article;
import model.Tweet;
 
public class BlogAndTwitterController implements Initializable {
	@FXML
	private TextField searchField;
	@FXML
	private Button searchButton;
	@FXML
	private TabPane resultPane;
	@FXML
	private Tab blogTab, twitterTab;
	@FXML
	private VBox articlesBlog, articlesTwitter, tagsBox;
	@FXML
	private Pagination blogPagination, tweetPagination;
	@FXML
	private ComboBox<String> hourChoice, typeTagChoice;
	@FXML 
	private ScrollPane tagsPane;
 
	private IBlogNewsTwitterService service = new BlogNewsTwitterService();
    private List<Article> currentArticles = service.getAllArticles();
    private List<Tweet> currentTweets = service.getAllTweets();
    private final int itemsPerPage = 5; 
    private Stage stage;
	private Scene scene;
	private Parent root;

    public void switchToHome(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Loading.fxml"));
		  root = loader.load();
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.show();
	}
	public void switchToSceneMarketplace(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/marketplace/Collection.fxml"));
		  root = loader.load();
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setTitle("Markertplace");
		  stage.setScene(scene);
		  stage.show();
	}
	public void switchToSceneConTrast(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/correlation/Correlation.fxml"));
		  root = loader.load();
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setTitle("Correlation");
		  stage.setScene(scene);
		  stage.show();
	}
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
 
        setChoiceBox();
        typeTagChoice.getSelectionModel().selectedItemProperty()
        .addListener((v, oldValue, newValue) -> updateDisplayTags());
        hourChoice.getSelectionModel().selectedItemProperty()
        		.addListener((v, oldValue, newValue) -> updateDisplayTags());
        updateDisplayTags();
        setupPagination(currentArticles);
        setupPagination(currentTweets);
        resultPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    search();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                search();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void setChoiceBox() {
    	hourChoice.getItems().addAll("One day", "One week", "One month");
    	hourChoice.setValue("One day");
    	typeTagChoice.getItems().addAll("Blog", "Twitter");
    	typeTagChoice.setValue("Twitter");
    }
    public void search() throws IOException {
    	String searchText = searchField.getText().toLowerCase();
        Tab selectedTab = resultPane.getSelectionModel().getSelectedItem();
        if (selectedTab == blogTab) {
        	currentArticles = service.getArticlesByTag(searchText);
        	if (currentArticles.isEmpty()) {
        		articlesBlog.getChildren().clear();
        	} else {
        		setupPagination(currentArticles);
        	}
        } else if (selectedTab == twitterTab) {
        	currentTweets = service.getTweetsByTag(searchText);
        	System.out.println(currentTweets);
        	if (currentTweets.isEmpty()) {
        		articlesTwitter.getChildren().clear();
        	} else {
        		setupPagination(currentTweets);
        	}
        }
    }
 
    private void setupPagination(List<?> items) {
        int pageCount = (int) Math.ceil((double) items.size() / itemsPerPage);
        Pagination targetPagination = new Pagination();
        if (items == null || items.isEmpty()) {
            articlesTwitter.getChildren().clear();
            articlesBlog.getChildren().clear();
            return;
        }
        if(items.size() != 0) {
	        if (items.get(0) instanceof Article) {
	        	targetPagination = blogPagination;
	        } else if (items.get(0) instanceof Tweet) {
	        	targetPagination = tweetPagination;
	        }
        }
        targetPagination.setPageCount(pageCount);
        targetPagination.setPageFactory(pageIndex -> createPage(items, pageIndex));
    }
 
    private Node createPage(List<?> items, int pageIndex) {
        if (items.isEmpty()) {
            return new ScrollPane();
        }
 
        VBox targetBox = new VBox();
        if (items.size() != 0) {
        	if (items.get(0) instanceof Article) {
        		articlesBlog.getChildren().clear();
        		targetBox = articlesBlog;
        	} else if (items.get(0) instanceof Tweet) {
        		articlesTwitter.getChildren().clear();
        		targetBox = articlesTwitter;
        	}
 
        	int start = pageIndex * itemsPerPage;
        	int end = Math.min(start + itemsPerPage, items.size());
        	for (int i = start; i < end; i++) {
        		Object item = items.get(i);
        		Node node = (item instanceof Article) ? 
        				createArticleNode((Article) item) : createTweetNode((Tweet) item);
        		targetBox.getChildren().add(node);
        		if (i < end - 1) {
        			targetBox.getChildren().add(new Separator());
        		}
        	}
        }
        return new ScrollPane(targetBox);
    }
 
    private Node createArticleNode(Article article) {
        VBox articleBox = new VBox(10);
        articleBox.getStyleClass().add("article-box");
 
        Label titleLabel = new Label(article.getTitle());
        titleLabel.getStyleClass().add("article-title");
        titleLabel.setWrapText(true);
 
        Label dateLabel = new Label(article.getPublishDate().toString());
        dateLabel.getStyleClass().add("article-date");
 
        Label contentLabel = new Label(article.getFullContent());
        contentLabel.getStyleClass().add("article-content");
        contentLabel.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);
        articleBox.getChildren().addAll(titleLabel, contentLabel, dateLabel);
        return articleBox;
    }
    public Node createTweetNode(Tweet tweet) {
    	try {
        VBox tweetBox = new VBox(10);
        tweetBox.getStyleClass().add("tweet-box");
 
        Label authorLabel = new Label(tweet.getAuthor());
        authorLabel.getStyleClass().add("tweet-author");
 
        Label contentLabel = new Label(tweet.getContent());
        contentLabel.getStyleClass().add("tweet-content");
        contentLabel.setWrapText(true);
 
        tweetBox.getChildren().addAll(authorLabel, contentLabel);
        return tweetBox;
    	} catch (Exception e) {
    	}
    	return new VBox();
    }
    private void updateDisplayTags() {
        String selectedType = typeTagChoice.getValue().toString();
        TimePeriodType timePeriod = getTimePeriodFromChoice(hourChoice.getValue().toString());
        if (selectedType.equals("Twitter")) {
            displayAllTags(tagsBox, timePeriod, "Twitter");
        } else {
            displayAllTags(tagsBox, timePeriod, "Blog");
        }
    }
    private TimePeriodType getTimePeriodFromChoice(String timeChoice) {
        switch (timeChoice) {
        	case "One day":
        		return TimePeriodType.DAILY;
        	case "One week":
        		return TimePeriodType.WEEKLY;
        	case "One month":
        		return TimePeriodType.MONTHLY;
        	default:
        		return TimePeriodType.DAILY;
        }
    }
    private void displayAllTags(VBox tagsContainer, TimePeriodType timePeriodType, String typeTag) {
        tagsContainer.getChildren().clear();
        List<String> tags = new ArrayList<>();
 
        if (typeTag.equals("Twitter")) {
            tags = service.getHotTagsTwitter(timePeriodType);
        } else if (typeTag.equals("Blog")) {
            tags = service.getHotTagsBlogNews(timePeriodType);
        }
 
        for (String tag : tags) {
            Label tagLabel = new Label(tag);
            tagLabel.getStyleClass().add("tag-label");
            tagsContainer.getChildren().add(tagLabel);
            tagsContainer.getChildren().add(new Separator());
        }
    }
 
}