package main.java.com.goxr3plus.xr3player.remote.dropbox.authorization;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.com.goxr3plus.xr3player.application.tools.ActionTool;
import main.java.com.goxr3plus.xr3player.application.tools.InfoTool;
import main.java.com.goxr3plus.xr3player.application.tools.NotificationType;

/**
 * Opens a browser inside the application for DropBox Authentication Process
 * 
 * @author GOXR3PLUS
 *
 */
public class DropboxAuthenticationBrowser extends StackPane {
	
	@FXML
	private WebView webView;
	
	@FXML
	private ProgressBar progressBar;
	
	@FXML
	private VBox errorPane;
	
	@FXML
	private JFXButton tryAgain;
	
	@FXML
	private ProgressIndicator tryAgainIndicator;
	
	//----------------------------------------------------------------
	
	private WebEngine webEngine;
	
	/**
	 * AccessToken Property
	 */
	private final StringProperty accessToken = new SimpleStringProperty("");
	
	/**
	 * The window
	 */
	private final Stage window = new Stage();
	
	//Identifying information about XR3Player		 
	private DbxAppInfo appInfo = new DbxAppInfo("5dx1fba89qsx2l6", "z2avrmbnnrmwvqa");
	DbxRequestConfig requestConfig = new DbxRequestConfig("XR3Player");
	DbxWebAuth webAuth;
	DbxWebAuth.Request webAuthRequest;
	
	/**
	 * Constructor
	 */
	public DropboxAuthenticationBrowser() {
		
		// ------------------------------------FXMLLOADER ----------------------------------------
		FXMLLoader loader = new FXMLLoader(getClass().getResource(InfoTool.FXMLS + "AuthanticationBrowser.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		//Create the Window	
		window.setTitle("Dropbox Sign In");
		window.getIcons().add(InfoTool.getImageFromResourcesFolder("icon.png"));
		window.initModality(Modality.APPLICATION_MODAL);
		window.setScene(new Scene(this));
	}
	
	/**
	 * Called as soon as .FXML is loaded from FXML Loader
	 */
	@FXML
	private void initialize() {
		
		//WebEngine
		webEngine = webView.getEngine();
		webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
		
		// progressBar
		progressBar.visibleProperty().bind(webEngine.getLoadWorker().runningProperty());
		progressBar.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
		
		//Add listener to the WebEngine
		webEngine.getLoadWorker().stateProperty().addListener((observable , oldState , newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				
				//Check for error pane
				errorPane.setVisible(false);
				
				//Check if this is the final authentication page
				if ("https://www.dropbox.com/1/oauth2/authorize_submit".equalsIgnoreCase(webEngine.getLocation())) {
					Document doc = webEngine.getDocument();
					try {
						Transformer transformer = TransformerFactory.newInstance().newTransformer();
						transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
						transformer.setOutputProperty(OutputKeys.METHOD, "xml");
						transformer.setOutputProperty(OutputKeys.INDENT, "yes");
						transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
						transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
						
						//transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(System.out, "UTF-8")))
						
						StringWriter writer = new StringWriter();
						transformer.transform(new DOMSource(doc), new StreamResult(writer));
						String output = writer.toString();
						//System.out.println(output)
						
						org.jsoup.nodes.Document html = Jsoup.parse(output);
						Element div = html.body().getElementById("auth-code");
						
						//Finish Authorization
						String code = div.getElementsByTag("input").first().attr("data-token");
						
						//Finish
						produceAccessToken(code);
						
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} else if (newState == Worker.State.FAILED) {
				
				//Set Error Pane Visible
				errorPane.setVisible(true);
			}
		});
		
		//tryAgain
		tryAgain.setOnAction(a -> checkForInternetConnection());
		
	}
	
	/**
	 * Shows the window
	 */
	public void showAuthenticationWindow() {
		
		
		//---------------------NEED TO FIX PROBLEM HERE----------------------
		//Clear the previous cookies
		//java.net.CookieHandler.setDefault(new com.sun.webkit.network.CookieManager());
		
		//Load it
		webEngine.load(getAuthonticationRequestURL());
		
		//Show the Window
		window.show();
	}
	
	/**
	 * Starts authorization and returns a "authorization URL" on the Dropbox website that gives the lets the user grant your app access to their Dropbox
	 * account.
	 * 
	 * @return The "authorization URL" on the Dropbox website that gives the lets the user grant your app access to their Dropbox account.
	 */
	public String getAuthonticationRequestURL() {
		
		// Run through Dropbox API authorization process	
		webAuth = new DbxWebAuth(requestConfig, appInfo);
		webAuthRequest = DbxWebAuth.newRequestBuilder().withNoRedirect().build();
		
		return webAuth.authorize(webAuthRequest);
	}
	
	/**
	 * Given the Authorization Code it produces the AccessToken needed to access DropBox Account
	 * 
	 * @param code
	 *            The OAuth2 DropBox AuthorizationCode
	 */
	public void produceAccessToken(String code) {
		try {
			// Run through Dropbox API authorization process
			DbxAuthFinish authFinish = webAuth.finishFromCode(code);
			
			//Set the access token
			accessToken.set(authFinish.getAccessToken());
			
			//System.out.println("Browser -> [" + accessToken.get() + "]")
			
			//Close the window
			window.close();
		} catch (DbxException ex) {
			ex.printStackTrace();
			ActionTool.showNotification("Error", "Error during DropBox Authentication \n please try again :)", Duration.millis(2000), NotificationType.ERROR);
		}
	}
	
	/**
	 * Checks for internet connection
	 */
	void checkForInternetConnection() {
		
		//tryAgainIndicator
		tryAgainIndicator.setVisible(true);
		
		//Check for internet connection
		Thread thread = new Thread(() -> {
			boolean hasInternet = InfoTool.isReachableByPing("www.google.com");
			Platform.runLater(() -> {
				errorPane.setVisible(!hasInternet);
				tryAgainIndicator.setVisible(false);
			});
		}, "Internet Connection Tester Thread");
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * @return the accessToken
	 */
	public StringProperty accessTokenProperty() {
		return accessToken;
	}
	
	/**
	 * @return the window
	 */
	public Stage getWindow() {
		return window;
	}
	
}
