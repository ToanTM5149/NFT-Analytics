package ui.home;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import crawler.CrawlerService;
import crawler.ICrawlerService;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Duration;
public class HomeController implements Initializable{
	@FXML
	private ProgressBar loadingProgressBar;
	
	@FXML
    private ImageView gifLoading;
	
	@FXML
	private Label percentLabel;
	
	@FXML
	private Label loadingLabel;
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	
    private Timeline timeline;
    
	public void increaseProgress() {
		double increment = 0.1;
		loadingProgressBar.setProgress(Math.min(1.0, loadingProgressBar.getProgress() + increment));
        percentLabel.setText(String.format("%d%%", (int) Math.round(loadingProgressBar.getProgress() * 100)));
        if (loadingProgressBar.getProgress() >= 1.0) {
            timeline.stop();
            loadingLabel.setText("Done");
        }
	}
	
	public void clickBtnCrawl(ActionEvent event) {
		ICrawlerService crawler = new CrawlerService();
		try {
//			crawler.crawl();
			timeline = new Timeline(
	                new KeyFrame(Duration.seconds(0.5), e -> increaseProgress())
	        );
	        timeline.setCycleCount(Timeline.INDEFINITE);
	        timeline.play();
//		} catch (crawler.exception.CrawlTimeoutException e) {
//			e.printStackTrace();
//		} catch (crawler.exception.InternetConnectionException e) {
//			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void switchToMarketplace(ActionEvent event) throws IOException {
		  FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/marketplace/Collection.fxml"));
		  root = loader.load();
		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		  scene = new Scene(root);
		  stage.setScene(scene);
		  stage.setTitle("Marketplace");
		  stage.show();
		 }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadingProgressBar.setStyle("-fx-accent: rgba(31, 248, 153, 1);");
	}
}
