package ui.correlation;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Article;
import model.CollectionFilter;
import model.Tweet;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import handle.correlation_service.CorrelationService;
import handle.correlation_service.ICorrelationService;

public class CorrelationController {

    @FXML
    private TableView<CollectionFilter> tableView;

    @FXML
    private TableColumn<CollectionFilter, Integer> columnNumber;

    @FXML
    private TableColumn<CollectionFilter, String> columnLogo;

    @FXML
    private TableColumn<CollectionFilter, String> columnName;

    @FXML
    private TableColumn<CollectionFilter, Double> columnVolume;

    @FXML
    private TableColumn<CollectionFilter, Double> columnVolumeChange;

    @FXML
    private TableColumn<CollectionFilter, Double> columnFloorPrice;

    @FXML
    private TableColumn<CollectionFilter, Double> columnFloorPriceChange;

    @FXML
    private TableColumn<CollectionFilter, Integer> columnItems;

    @FXML
    private TableColumn<CollectionFilter, Integer> columnOwners;

    @FXML
    private TableColumn<CollectionFilter, String> columnCurrency;

    @FXML
    private TableColumn<CollectionFilter, String> columnChain;

    @FXML
    private TableColumn<CollectionFilter, String> columnPeriod;

    @FXML
    private TableColumn<CollectionFilter, String> columnMarketplaceName;

    @FXML
    private TextField searchTextField; 


    @FXML
    private Button searchButton;
    
    @FXML
    private VBox tweetVBox;
    
    @FXML
    private VBox blogVBox;
    
    @FXML
	private Tab blogTab, twitterTab;
    
    @FXML
	private Pagination blogPagination, tweetPagination;
    
    @FXML
	private TabPane resultPane;
    
    private final int itemsPerPage = 5; 
    
    private ICorrelationService service = new CorrelationService();
    private Set<CollectionFilter> collectionList;
    private List<Tweet> tweets;
    private List<Article> articles;

	private Stage stage;
	private Scene scene;
	private Parent root;


	public void switchToHome(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/home/Home.fxml"));
		  root = loader.load();
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setTitle("Home");
		  stage.setScene(scene);
		  stage.show();
	}

	public void switchToSceneBlogAndTwitter(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/blogandtwitter/BlogAndTwitter.fxml"));
		  root = loader.load();
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setTitle("Hastag");
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
	

    public void initialize() {
        // Set up cell value factories for each column using PropertyValueFactory

    	columnNumber.setCellValueFactory(new Callback<CellDataFeatures<CollectionFilter, Integer>, ObservableValue<Integer>>() {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<CollectionFilter, Integer> c) {
				int rowIndex = c.getValue() != null ? tableView.getItems().indexOf(c.getValue()) + 1 : 0;
				return new ReadOnlyObjectWrapper<>(rowIndex);
			}
		});
    	columnNumber.setCellFactory(new Callback<TableColumn<CollectionFilter, Integer>, TableCell<CollectionFilter, Integer>>() {
			@Override
			public TableCell<CollectionFilter, Integer> call(TableColumn<CollectionFilter, Integer> param) {
				return new TableCell<CollectionFilter, Integer>() {
					@Override
					protected void updateItem(Integer item, boolean empty) {
						super.updateItem(item, empty);

						if (this.getTableRow() != null && item != null) {
							setText(String.valueOf(this.getTableRow().getIndex() + 1));
						} else {
							setText("");
						}
					}
				};
			}
		});
    	columnNumber.setSortable(false);


    	columnLogo.setCellValueFactory(new PropertyValueFactory<CollectionFilter, String>("logo"));
		columnLogo.setCellFactory(param -> new TableCell<CollectionFilter, String>() {
			private final ImageView imageView = new ImageView();

			@Override
			protected void updateItem(String logoUrl, boolean empty) {
				super.updateItem(logoUrl, empty);

				if (empty || logoUrl == null || logoUrl.isEmpty()) {
					setGraphic(null);
				} else {
					Image image = new Image(logoUrl, true);
					System.out.println(logoUrl);
					imageView.setImage(image);
					imageView.setFitWidth(75);
					imageView.setFitHeight(75);
					setGraphic(imageView);
				}
			}
		});
    	columnLogo.setCellValueFactory(new PropertyValueFactory<CollectionFilter, String>("logo"));
    	columnLogo.setCellFactory(param -> new TableCell<CollectionFilter, String>() {
    		private final ImageView imageView = new ImageView();
    	    private final MediaView mediaView = new MediaView();
    	    private String lastUrl = "";

    	    @Override
    	    protected void updateItem(String logoUrl, boolean empty) {
    	        super.updateItem(logoUrl, empty);

    	        if (empty || logoUrl == null || logoUrl.isEmpty()) {
    	            setGraphic(null);
    	        } else {
    	        	if (isImage(logoUrl)) {
    	                if (!logoUrl.equals(lastUrl)) {
    	                    Image image = new Image(logoUrl, true);
    	                    System.out.println(logoUrl);
    	                    imageView.setImage(image);
    	                    imageView.setFitWidth(75);
    	                    imageView.setFitHeight(75);
    	                    setGraphic(imageView);
    	                }
    	            } else if (isVideo(logoUrl)) {
    	                if (!logoUrl.equals(lastUrl)) {
    	                    Media media = new Media(logoUrl);
    	                    MediaPlayer mediaPlayer = new MediaPlayer(media);
    	                    mediaView.setMediaPlayer(mediaPlayer);
    	                    mediaView.setFitWidth(75);
    	                    mediaView.setFitHeight(75);
    	                    setGraphic(mediaView);
    	                    mediaPlayer.play();
    	                }
    	            } else {
    	                System.out.println("Unsupported media type");
    	                setGraphic(null);
    	            }
    	        	lastUrl = logoUrl;
    	        }
    	    }
    	    private boolean isVideo(String url) {
    	        return url.toLowerCase().endsWith(".mp4");
    	    }

    	    private boolean isImage(String url) {
    	    	try {
    	            new Image(url);
    	            return true;  
    	        } catch (Exception e) {
    	        	e.printStackTrace();
    	            return false;
    	        }
    	    }
    	});


        columnName.setCellValueFactory(new PropertyValueFactory<CollectionFilter, String>("name"));

        columnVolume.setCellValueFactory(new PropertyValueFactory<CollectionFilter, Double>("volume"));
        columnVolume.setCellFactory(column -> {
		    return new TableCell<CollectionFilter, Double>() {
		        @Override
		        protected void updateItem(Double item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item != null && !empty) {
		                setText(String.format("%.3f", item));
		            } else {
		                setText("");
		            }
		        }
		    };
		});

        columnVolumeChange.setCellValueFactory(new PropertyValueFactory<CollectionFilter, Double>("volumeChange"));
        columnVolumeChange.setCellFactory(column -> {
		    return new TableCell<CollectionFilter, Double>() {
		        @Override
		        protected void updateItem(Double item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item != null && !empty) {
		                setText(String.format("%.3f", item));
		            } else {
		                setText("");
		            }
		        }
		    };
		});

        columnFloorPrice.setCellValueFactory(new PropertyValueFactory<CollectionFilter, Double>("floorPrice"));
        columnFloorPrice.setCellFactory(column -> {
		    return new TableCell<CollectionFilter, Double>() {
		        @Override
		        protected void updateItem(Double item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item != null && !empty) {
		                setText(String.format("%.4f", item));
		            } else {
		                setText("");
		            }
		        }
		    };
		});

        columnFloorPriceChange.setCellValueFactory(new PropertyValueFactory<CollectionFilter, Double>("floorPriceChange"));
        columnFloorPriceChange.setCellFactory(column -> {
		    return new TableCell<CollectionFilter, Double>() {
		        @Override
		        protected void updateItem(Double item, boolean empty) {
		            super.updateItem(item, empty);

		            if (item != null && !empty) {
		                setText(String.format("%.4f", item));
		            } else {
		                setText("");
		            }
		        }
		    };
		});

        columnItems.setCellValueFactory(new PropertyValueFactory<CollectionFilter, Integer>("items"));
		columnOwners.setCellValueFactory(new PropertyValueFactory<CollectionFilter, Integer>("owners"));
        columnCurrency.setCellValueFactory(new PropertyValueFactory<CollectionFilter, String>("currency"));
        columnChain.setCellValueFactory(new PropertyValueFactory<CollectionFilter, String>("chain"));
        columnPeriod.setCellValueFactory(new PropertyValueFactory<CollectionFilter, String>("period"));

        columnMarketplaceName.setCellValueFactory(new PropertyValueFactory<CollectionFilter, String>("marketplaceName"));
        
        
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
            	System.out.println("Button");
                displayTweetsForSelectedRow(newSelection);
            }
        });
        
        resultPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null) {
                CollectionFilter selectedCollection = tableView.getSelectionModel().getSelectedItem();
                if (selectedCollection != null) {
                    displayTweetsForSelectedRow(selectedCollection);
                } else {
                	tweetVBox.getChildren().clear();
                    blogVBox.getChildren().clear();
                }
            }
        });
    }
    
    public void displayTweetsForSelectedRow(CollectionFilter selectedCollection) {
        try {
            Tab selectedTab = resultPane.getSelectionModel().getSelectedItem();
            
            if (selectedTab == blogTab) {
                articles = service.getArticleByCollectionFilter(selectedCollection);
                System.out.println(articles.size());
                setupPagination(articles);
            } else if (selectedTab == twitterTab) {
                tweets = service.getTweetByCollectionFilter(selectedCollection);
                System.out.println(tweets.size());
                setupPagination(tweets);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void handleSearchButton(ActionEvent event) {
        try {
            String searchTerm = searchTextField.getText().trim();
            collectionList = service.filterCollectionByName(searchTerm);
		    updateTableView(collectionList);
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    public void updateTableView(Set<CollectionFilter> collectionList) {
        ObservableList<CollectionFilter> observableList = FXCollections.observableArrayList(collectionList);
        tableView.setItems(observableList);

//         Show or hide the "No results found" label based on the search results
//        noResultsLabel.setVisible(collectionList.isEmpty());
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
    public Node createArticleNode(Article article) {
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

    public Node createPage(List<?> items, int pageIndex) {
        	VBox targetBox = new VBox();
     

        	List<?> clonedItems = new ArrayList<>(items);
     
        	if (!clonedItems.isEmpty()) {
        	    int start = pageIndex * itemsPerPage;
        	    int end = Math.min(start + itemsPerPage, clonedItems.size());
     
        	    
        	    if (clonedItems.get(0) instanceof Article) {
        	        blogVBox.getChildren().clear();
        	    } else if (clonedItems.get(0) instanceof Tweet) {
        	        tweetVBox.getChildren().clear();
        	    }
     
        	    targetBox.getChildren().clear(); 
     
        	    
        	    start = Math.min(start, clonedItems.size());
     
        	    for (int i = start; i < end; i++) {
        	        if (i > start) {
        	            
        	            targetBox.getChildren().add(new Separator());
        	        }
     
        	        Object item = clonedItems.get(i);
        	        Node node = (item instanceof Article) ? createArticleNode((Article) item) : createTweetNode((Tweet) item);
        	        targetBox.getChildren().add(node);
        	    }
        	}
     
        	return new ScrollPane(targetBox);
        }

    public void setupPagination(List<?> items) {

        	int pageCount = (int) Math.ceil((double) items.size() / itemsPerPage);
            Pagination targetPagination = new Pagination();

            tweetVBox.getChildren().clear();
            blogVBox.getChildren().clear();

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
}