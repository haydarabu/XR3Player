/*
 * 
 */
package main.java.com.goxr3plus.xr3player.xplayer.presenter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXToggleButton;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.java.com.goxr3plus.xr3player.application.Main;
import main.java.com.goxr3plus.xr3player.application.presenter.custom.DJDisc;
import main.java.com.goxr3plus.xr3player.application.presenter.custom.DJFilter;
import main.java.com.goxr3plus.xr3player.application.presenter.custom.DJFilter.DJFilterCategory;
import main.java.com.goxr3plus.xr3player.application.presenter.custom.DJFilterListener;
import main.java.com.goxr3plus.xr3player.application.presenter.custom.Marquee;
import main.java.com.goxr3plus.xr3player.application.presenter.custom.flippane.FlipPanel;
import main.java.com.goxr3plus.xr3player.application.settings.ApplicationSettingsController.SettingsTab;
import main.java.com.goxr3plus.xr3player.application.tools.ActionTool;
import main.java.com.goxr3plus.xr3player.application.tools.FileType;
import main.java.com.goxr3plus.xr3player.application.tools.FileTypeAndAbsolutePath;
import main.java.com.goxr3plus.xr3player.application.tools.IOTool;
import main.java.com.goxr3plus.xr3player.application.tools.InfoTool;
import main.java.com.goxr3plus.xr3player.application.tools.JavaFXTools;
import main.java.com.goxr3plus.xr3player.application.tools.NotificationType;
import main.java.com.goxr3plus.xr3player.application.windows.EmotionsWindow.Emotion;
import main.java.com.goxr3plus.xr3player.application.windows.XPlayerWindow;
import main.java.com.goxr3plus.xr3player.smartcontroller.enums.Genre;
import main.java.com.goxr3plus.xr3player.smartcontroller.media.Audio;
import main.java.com.goxr3plus.xr3player.smartcontroller.media.MediaInformation;
import main.java.com.goxr3plus.xr3player.smartcontroller.tags.TagTabCategory;
import main.java.com.goxr3plus.xr3player.xplayer.model.XPlayer;
import main.java.com.goxr3plus.xr3player.xplayer.model.XPlayerModel;
import main.java.com.goxr3plus.xr3player.xplayer.services.XPlayerPlayService;
import main.java.com.goxr3plus.xr3player.xplayer.services.XPlayerSeekService;
import main.java.com.goxr3plus.xr3player.xplayer.visualizer.fxpresenter.VisualizerStackController;
import main.java.com.goxr3plus.xr3player.xplayer.visualizer.fxpresenter.VisualizerWindowController;
import main.java.com.goxr3plus.xr3player.xplayer.visualizer.fxpresenter.XPlayerVisualizer;
import main.java.goxr3plus.javastreamplayer.stream.Status;
import main.java.goxr3plus.javastreamplayer.stream.StreamPlayerEvent;
import main.java.goxr3plus.javastreamplayer.stream.StreamPlayerException;
import main.java.goxr3plus.javastreamplayer.stream.StreamPlayerListener;

/**
 * Represents the graphical interface for the deck.
 *
 * @author GOXR3PLUS
 */
public class XPlayerController extends StackPane implements DJFilterListener, StreamPlayerListener {
	
	public static final Image playImage = InfoTool.getImageFromResourcesFolder("play.png");
	public static final Image pauseImage = InfoTool.getImageFromResourcesFolder("pause.png");
	private static final XPlayerControllerContextMenu contextMenu = new XPlayerControllerContextMenu();
	
	//-----------------------------------------------
	
	@FXML
	private StackPane xPlayerStackPane;
	
	@FXML
	private BorderPane rootBorderPane;
	
	@FXML
	private StackPane modesStackPane;
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private GridPane basicGridPane;
	
	@FXML
	private SplitPane leftSplitPane;
	
	@FXML
	private BorderPane discBorderPane;
	
	@FXML
	private StackPane diskStackPane;
	
	@FXML
	private StackPane diskStackPane1;
	
	@FXML
	private Button backwardButton;
	
	@FXML
	private Button forwardButton;
	
	@FXML
	private Button playPauseButton;
	
	@FXML
	private Button stopButton;
	
	@FXML
	private ToggleButton muteButton;
	
	@FXML
	private Button replayButton;
	
	@FXML
	private Label advModeVolumeLabel;
	
	@FXML
	private Label elapsedTimeLabel;
	
	@FXML
	private Label remainingTimeLabel;
	
	@FXML
	private Label totalTimeLabel;
	
	@FXML
	private Button previousSongButton;
	
	@FXML
	private Button nextSongButton;
	
	@FXML
	private StackPane visualizerStackTopParent;
	
	@FXML
	private StackPane visualizerStackPane;
	
	@FXML
	private Label playerStatusLabel;
	
	@FXML
	private Label visualizerVisibleLabel;
	
	@FXML
	private FlowPane visualizerMaximizedBox;
	
	@FXML
	private Label visualizerMinimize;
	
	@FXML
	private Label visualizerRequestFocus;
	
	@FXML
	private HBox visualizerSettingsHBox;
	
	@FXML
	private Button visualizerSettings;
	
	@FXML
	private ToggleButton visualizerVisible;
	
	@FXML
	private Button maximizeVisualizer;
	
	@FXML
	private Label visualizationsDisabledLabel;
	
	@FXML
	private Button enableHighGraphics;
	
	@FXML
	private HBox mediaNameHBox;
	
	@FXML
	private Button emotionsButton;
	
	@FXML
	private Button mediaTagImageButton;
	
	@FXML
	private Tab equalizerTab;
	
	@FXML
	private BorderPane smBorderPane;
	
	@FXML
	private StackPane smModeCenterStackPane;
	
	@FXML
	private ImageView smImageView;
	
	@FXML
	private Label smMediaTitle;
	
	@FXML
	private Slider smTimeSlider;
	
	@FXML
	private Label smTimeSliderLabel;
	
	@FXML
	private Button smMinimizeVolume;
	
	@FXML
	private Slider smVolumeSlider;
	
	@FXML
	private Button smMaximizeVolume;
	
	@FXML
	private Label smVolumeSliderLabel;
	
	@FXML
	private JFXToggleButton showVisualizer;
	
	@FXML
	private HBox hBox11;
	
	@FXML
	private Button smBackwardButton;
	
	@FXML
	private Button smForwardButton;
	
	@FXML
	private ToggleButton smMuteButton;
	
	@FXML
	private Button smReplayButton;
	
	@FXML
	private Button smPlayPauseButton;
	
	@FXML
	private Button smStopButton;
	
	@FXML
	private Label topInfoLabel;
	
	@FXML
	private Label modeToggleLabel;
	
	@FXML
	private JFXToggleButton modeToggle;
	
	@FXML
	private JFXToggleButton settingsToggle;
	
	@FXML
	private Button extendPlayer;
	
	@FXML
	private Button showMenu;
	
	@FXML
	private Button openFile;
	
	@FXML
	private Button settings;
	
	@FXML
	private MenuButton transferMedia;
	
	@FXML
	private StackPane regionStackPane;
	
	@FXML
	private Label bugLabel;
	
	@FXML
	private Label fxLabel;
	
	@FXML
	private Label dragAndDropLabel;
	
	@FXML
	private Label restorePlayer;
	
	@FXML
	private Label focusXPlayerWindow;
	
	// -----------------------------------------------------------------------------
	
	/** A Fade Transition */
	private FadeTransition fadeTransition;
	
	/**
	 * This Variable Determines if the Player is extended or not ( which means it is being shown on an external window different from the main window )
	 */
	private boolean isPlayerExtended = false;
	
	public final Logger logger = Logger.getLogger(getClass().getName());
	
	// ------------------------- Images/ImageViews --------------------------
	
	private final ImageView eye = InfoTool.getImageViewFromResourcesFolder("eye.png");
	private final ImageView eyeDisabled = InfoTool.getImageViewFromResourcesFolder("eyeDisabled.png");
	
	private static final Image noSeek = InfoTool.getImageFromResourcesFolder("Private-" + ( ImageCursor.getBestSize(64, 64).getWidth() < 64.00 ? "32" : "64" ) + ".png");
	private static final ImageCursor noSeekCursor = new ImageCursor(noSeek, noSeek.getWidth() / 2, noSeek.getHeight() / 2);
	
	// ------------------------- Services --------------------------
	
	/** The seek service. */
	private final XPlayerSeekService seekService = new XPlayerSeekService(this);
	
	/** The play service. */
	private final XPlayerPlayService playService = new XPlayerPlayService(this);
	
	// ------------------------- Variables --------------------------
	/** The key. */
	private final int key;
	
	/** The disc is being mouse dragged */
	public boolean discIsDragging;
	
	//-----CustomDJFilter
	DJFilter volumeDisc;
	
	// -------------------------ETC --------------------------
	
	private XPlayerPlaylist xPlayerPlayList;
	
	private XPlayerWindow xPlayerWindow;
	
	/** The x player settings controller. */
	private XPlayerExtraSettings playerExtraSettings;
	
	/** The x player model. */
	private XPlayerModel xPlayerModel;
	
	/** The x player. */
	private XPlayer xPlayer;
	
	/** The visualizer window. */
	private VisualizerWindowController visualizerWindow;
	
	/**
	 * This controller contains a Visualizer and a Label which describes every time (for some milliseconds) which type of visualizer is being displayed
	 * (for example [ Oscilloscope , Rosette , Spectrum Bars etc...]);
	 */
	private final VisualizerStackController visualizerStackController = new VisualizerStackController();
	
	/** The visualizer. */
	private XPlayerVisualizer visualizer;
	
	/** The equalizer. */
	private XPlayerEqualizer equalizer;
	
	private XPlayerPad xPlayerPad;
	
	/** The disc. */
	private DJDisc disc;
	
	private final Marquee mediaFileMarquee = new Marquee();
	
	private final FlipPanel flipPane = new FlipPanel(Orientation.HORIZONTAL);
	
	//======= Events ===========
	
	public final EventHandler<? super MouseEvent> audioDragEvent = event -> {
		String absolutePath = xPlayerModel.songPathProperty().get();
		if (absolutePath != null) {
			
			/* Allow copy transfer mode */
			Dragboard db = startDragAndDrop(TransferMode.COPY, TransferMode.LINK);
			
			/* Put a String into the dragBoard */
			ClipboardContent content = new ClipboardContent();
			content.putFiles(Arrays.asList(new File(absolutePath)));
			db.setContent(content);
			
			/* Set the DragView */
			new Audio(absolutePath, 0.0, 0, "", "", Genre.SEARCHWINDOW, -1).setDragView(db);
		}
		event.consume();
	};
	
	public final EventHandler<? super DragEvent> audioDropEvent = event -> {
		
		//We don't want the player to start if the drop event is for the XPlayer PlayList
		if (!flipPane.isBackVisible()) {
			
			// File?
			for (File file : event.getDragboard().getFiles()) {
				
				//No directories allowed
				if (!file.isDirectory()) {
					
					//Get it
					FileTypeAndAbsolutePath ftaap = IOTool.getRealPathFromFile(file.getAbsolutePath());
					
					//Check if File exists
					if (!new File(ftaap.getFileAbsolutePath()).exists()) {
						ActionTool.showNotification("File doesn't exist",
								( ftaap.getFileType() == FileType.SYMBOLIC_LINK ? "Symbolic link" : "Windows Shortcut" ) + " points to a file that doesn't exists anymore.",
								Duration.millis(2000), NotificationType.INFORMATION);
						return;
					}
					
					//Check if this File is Supported by XR3Player 
					if (!InfoTool.isAudioSupported(ftaap.getFileAbsolutePath())) {
						ActionTool.showNotification("File not supported", "XR3Player doesn't supports the given File", Duration.millis(2000), NotificationType.INFORMATION);
						return;
					}
					
					//Check if XPlayer is already active
					if (xPlayer.isPausedOrPlaying() && Main.settingsWindow.getxPlayersSettingsController().getAskSecurityQuestion().isSelected()) {
						if (ActionTool.doQuestion("Abort Current Song", "A song is already playing on this deck.\n Are you sure you want to replace it?",
								visualizerWindow.getStage().isShowing() && !xPlayerWindow.getWindow().isShowing() ? visualizerWindow : xPlayerStackPane, Main.window))
							playSong(ftaap.getFileAbsolutePath());
					} else
						playSong(ftaap.getFileAbsolutePath());
					break;
				}
			}
			
			// // URL?
			// if (xPlayer.isPausedOrPlaying()) {
			// // OK?
			// if (ActionTool
			// .doQuestion("A song is already playing on this deck.\n Are you
			// sure you want to replace it?"))
			// xPlayer.playSong(dragDrop.getDragboard().getUrl().toString());
			// } else
			// xPlayer.playSong(dragDrop.getDragboard().getUrl().toString());
			
			event.setDropCompleted(true);
			event.consume();
		}
	};
	
	//============================================================================================
	
	/**
	 * Constructor.
	 *
	 * @param key
	 *            The key that is identifying this player
	 */
	public XPlayerController(int key) {
		this.key = key;
		
		// ----------------------------------- FXMLLoader
		// -------------------------------------
		FXMLLoader loader = new FXMLLoader(getClass().getResource(InfoTool.FXMLS + "XPlayerController.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "XPlayerController FXML can't be loaded!", ex);
		}
		
	}
	
	/**
	 * @return the xPlayerStackPane
	 */
	public StackPane getXPlayerStackPane() {
		return xPlayerStackPane;
	}
	
	/**
	 * @return the settingsToggle
	 */
	public JFXToggleButton getSettingsToggle() {
		return settingsToggle;
	}
	
	/**
	 * Returns the XPlayerStackPane back to the XPlayerController if it is on XPlayer external Window
	 */
	public void restorePlayerStackPane() {
		this.getChildren().add(getXPlayerStackPane());
	}
	
	/** Called as soon as the .fxml has been loaded */
	@FXML
	private void initialize() {
		
		// -----XPlayer and XPlayerModel-------------
		xPlayerModel = new XPlayerModel();
		xPlayer = new XPlayer();
		xPlayer.addStreamPlayerListener(this);
		
		// -----Important-------------
		xPlayerWindow = new XPlayerWindow(this);
		
		//== RadialMenu
		//radialMenu = new XPlayerRadialMenu(this);
		//		radialMenu.mute.selectedProperty().addListener(l -> {
		//			xPlayer.setMute(radialMenu.mute.isSelected());
		//			muteButton.setSelected(radialMenu.mute.isSelected());
		//			
		//			//System.out.println("Entered Radial Menu");
		//		});
		muteButton.selectedProperty().addListener(l -> {
			xPlayer.setMute(muteButton.isSelected());
			//radialMenu.mute.setSelected(muteButton.isSelected());
			
			//System.out.println("Entered Menu Button");
			
			//Update PropertiesDB
			Main.dbManager.getPropertiesDb().updateProperty("XPlayer" + getKey() + "-Muted", String.valueOf(muteButton.isSelected()));
			
		});
		smMuteButton.selectedProperty().bindBidirectional(muteButton.selectedProperty());
		
		//
		xPlayerPlayList = new XPlayerPlaylist(this);
		visualizerWindow = new VisualizerWindowController(this);
		playerExtraSettings = new XPlayerExtraSettings(this);
		
		//== modesStackPane
		modesStackPane.setOnDragOver(event -> {
			//System.out.println(event.getGestureSource())
			
			//Check if FlipPane is on the front side
			if (!flipPane.isBackVisible()) { //event.getGestureSource() != mediaFileMarquee) {
				dragAndDropLabel.setVisible(true);
			}
			
			event.consume();
		});
		
		//== dragAndDropLabel
		dragAndDropLabel.setVisible(false);
		dragAndDropLabel.setOnDragOver(event -> {
			//Check if FlipPane is on the front side
			if (!flipPane.isBackVisible())
				event.acceptTransferModes(TransferMode.LINK);
			
			event.consume();
		});
		dragAndDropLabel.setOnDragExited(event -> {
			dragAndDropLabel.setVisible(false);
			event.consume();
		});
		dragAndDropLabel.setOnDragDropped(audioDropEvent);
		
		//== regionStackPane
		regionStackPane.setVisible(false);
		
		// mediaFileStackPane	
		mediaFileMarquee.getLabel().setTooltip(new Tooltip(""));
		mediaFileMarquee.getLabel().getTooltip().textProperty().bind(mediaFileMarquee.getLabel().textProperty());
		mediaFileMarquee.setText("Drag&Drop a song here");
		mediaFileMarquee.setOnMouseClicked(m -> openAudioInExplorer());
		mediaFileMarquee.setCursor(Cursor.HAND);
		mediaFileMarquee.setOnDragDetected(audioDragEvent);
		mediaNameHBox.getChildren().add(1, mediaFileMarquee);
		HBox.setHgrow(mediaFileMarquee, Priority.ALWAYS);
		
		//smMediaTitle
		smMediaTitle.textProperty().bind(mediaFileMarquee.getLabel().textProperty());
		smMediaTitle.getTooltip().textProperty().bind(mediaFileMarquee.getLabel().textProperty());
		
		// openMediaFileFolder
		mediaTagImageButton.setOnAction(action -> Main.tagWindow.openAudio(xPlayerModel.songPathProperty().get(), TagTabCategory.ARTWORK, true));
		mediaTagImageButton.setOnDragDetected(audioDragEvent);
		
		// openFile
		openFile.setOnAction(action -> openFileChooser());
		
		// showMenu
		showMenu.setOnMouseReleased(m -> {
			
			//If there is no Media
			if (xPlayerModel.getSongPath() == null) {
				ActionTool.showNotification("No Media", "No Media added on Player", Duration.seconds(2), NotificationType.INFORMATION);
				return;
				//Check if Media exists
			} else if (!new File(xPlayerModel.getSongPath()).exists()) {
				ActionTool.showNotification("Media doesn't exist", "Current Media File doesn't exist anymore...", Duration.seconds(2), NotificationType.INFORMATION);
				return;
			}
			
			XPlayerController.contextMenu.showContextMenu(this.xPlayerModel.getSongPath(), m.getScreenX(), m.getScreenY(), showMenu);
		});
		
		// topInfoLabel
		topInfoLabel.setText("Player {" + this.getKey() + "}");
		
		//== forwardButton
		forwardButton.setOnAction(a -> seek(Integer.parseInt(forwardButton.getText())));
		smForwardButton.setOnAction(forwardButton.getOnAction());
		smForwardButton.textProperty().bind(forwardButton.textProperty());
		
		//== backwardButton
		backwardButton.setOnAction(a -> seek(-Integer.parseInt(backwardButton.getText())));
		smBackwardButton.setOnAction(backwardButton.getOnAction());
		smBackwardButton.textProperty().bind(backwardButton.textProperty());
		
		//== playPauseButton
		playPauseButton.setOnAction(fire -> {
			if (xPlayer.isPlaying())
				pause();
			else
				playOrReplay();
			
			//Fix fast the image
			( (ImageView) playPauseButton.getGraphic() ).setImage(xPlayer.isPlaying() ? XPlayerController.pauseImage : XPlayerController.playImage);
			( (ImageView) smPlayPauseButton.getGraphic() ).setImage(xPlayer.isPlaying() ? XPlayerController.pauseImage : XPlayerController.playImage);
		});
		smPlayPauseButton.setOnAction(playPauseButton.getOnAction());
		
		//== replayButton
		replayButton.setOnAction(a -> replay());
		smReplayButton.setOnAction(replayButton.getOnAction());
		
		//== stopButton
		stopButton.setOnAction(a -> stop());
		smStopButton.setOnAction(stopButton.getOnAction());
		
		//flipPane
		flipPane.setFlipTime(150);
		flipPane.getFront().getChildren().addAll(modesStackPane);
		flipPane.getBack().getChildren().addAll(playerExtraSettings);
		
		settingsToggle.selectedProperty().addListener((observable , oldValue , newValue) -> {
			if (newValue) // true?
				flipPane.flipToBack();
			else
				flipPane.flipToFront();
		});
		rootBorderPane.setCenter(flipPane);
		
		//modeToggle
		modeToggle.selectedProperty().addListener((observable , oldValue , newValue) -> {
			if (!newValue) {
				smBorderPane.setVisible(true);
				modeToggleLabel.setText("Basic");
				
				//Fix the Visualizer
				simple_And_Advanced_Mode_Fix_Visualizer();
				
			} else {
				smBorderPane.setVisible(false);
				modeToggleLabel.setText("Advanced");
				
				//Fix the Visualizer
				simple_And_Advanced_Mode_Fix_Visualizer();
			}
			
			//Go away from history
			settingsToggle.setSelected(false);
			
			//Update the properties file
			Main.dbManager.getPropertiesDb().updateProperty("XPlayer" + getKey() + "-Advanced-Mode", String.valueOf(modeToggle.isSelected()));
		});
		
		//showVisualizer
		showVisualizer.selectedProperty().addListener((observable , oldValue , newValue) -> {
			
			//Fix the Visualizer
			simple_And_Advanced_Mode_Fix_Visualizer();
			
			//Update the properties file
			Main.dbManager.getPropertiesDb().updateProperty("XPlayer" + getKey() + "-Simple-Mode-Visualizers-Enabled", String.valueOf(showVisualizer.isSelected()));
		});
		
		//RestorePlayerVBox
		restorePlayer.getParent().visibleProperty().bind(xPlayerWindow.getWindow().showingProperty());
		
		//restorePlayer
		restorePlayer.setOnMouseReleased(m -> {
			if (m.getButton() == MouseButton.PRIMARY)
				xPlayerWindow.close();
		});
		
		//focusXPlayerWindow
		focusXPlayerWindow.setOnMouseReleased(m -> xPlayerWindow.getWindow().requestFocus());
		
		//extendPlayer
		extendPlayer.textProperty().bind(Bindings.when(xPlayerWindow.getWindow().showingProperty()).then("Restore").otherwise("Extend"));
		extendPlayer.setOnAction(ac -> {
			if (!xPlayerWindow.getWindow().isShowing()) {
				xPlayerWindow.show();
				isPlayerExtended = true;
			} else {
				xPlayerWindow.close();
				isPlayerExtended = false;
			}
		});
		
		//transferMedia
		transferMedia.getItems().get(key).setVisible(false);
		transferMedia.getItems().forEach(item -> item.setOnAction(a -> Optional.ofNullable(getxPlayerModel().songPathProperty().getValue()).ifPresent(path -> {
			
			//Start the selected player
			Main.xPlayersList.getXPlayerController(transferMedia.getItems().indexOf(item)).playSong(getxPlayerModel().songPathProperty().get(), getxPlayerModel().getCurrentTime());
			
			//Stop the Current Player
			stop();
			
		})));
		
		//=emotionsButton
		emotionsButton.disableProperty().bind(xPlayerModel.songPathProperty().isNull());
		emotionsButton.setOnAction(a -> updateEmotion(emotionsButton));
		
		//enableHighGraphics
		enableHighGraphics.setOnAction(a -> Main.settingsWindow.showWindow(SettingsTab.GENERERAL));
		
		//=settings
		settings.setOnAction(a -> Main.settingsWindow.showWindow(SettingsTab.XPLAYERS));
		
		//smMinimizeVolume
		smMinimizeVolume.setOnAction(a -> minimizeVolume());
		//smMaximizeVolume
		smMaximizeVolume.setOnAction(a -> maximizeVolume());
		
		// fadeTranstion
		fadeTransition = new FadeTransition(Duration.millis(1500), advModeVolumeLabel);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		advModeVolumeLabel.setOpacity(0);
		
		//----------------------------------SIMPLE MODE PLAYER------------------------------------------------
		
	}
	
	/**
	 * Check's if disc rotation is allowed or not and based on player status it start's it or stops it
	 */
	public void checkDiscRotation() {
		
		//Is Player Playing?
		if (!playService.isDiscImageNull() && xPlayer.isPlaying())
			//Is discRotation allowed?
			if (Main.settingsWindow.getxPlayersSettingsController().getAllowDiscRotation().isSelected())
				disc.resumeRotation();
			else
				disc.stopRotation();
			
	}
	
	/**
	 * Fixes the Visualizer StackPane when adding it on Simple Mode or Advanced Mode
	 */
	private void simple_And_Advanced_Mode_Fix_Visualizer() {
		
		//If we are on the simple Mode
		if (!modeToggle.isSelected()) {
			//If the ShowVisualizer on Simple Mode is Selected
			if (showVisualizer.isSelected()) {
				visualizerStackTopParent.getChildren().remove(visualizerStackPane);
				//Check for no duplicates
				if (!smModeCenterStackPane.getChildren().contains(visualizerStackPane))
					smModeCenterStackPane.getChildren().add(visualizerStackPane);
				//If it isn't
			} else {
				smModeCenterStackPane.getChildren().remove(visualizerStackPane);
				//Check for no duplicates
				if (!visualizerStackTopParent.getChildren().contains(visualizerStackPane))
					visualizerStackTopParent.getChildren().add(visualizerStackPane);
			}
			//If we are on Advanced Mode
		} else {
			smModeCenterStackPane.getChildren().remove(visualizerStackPane);
			//Check for no duplicates
			if (!visualizerStackTopParent.getChildren().contains(visualizerStackPane))
				visualizerStackTopParent.getChildren().add(visualizerStackPane);
		}
	}
	
	/**
	 * This method is called to change the Emotion Image of the Media based on the current Emotion
	 * 
	 * @param emotion
	 */
	public void changeEmotionImage(Emotion emotion) {
		Main.emotionsWindow.giveEmotionImageToButton(emotionsButton, emotion);
	}
	
	/**
	 * Update the emotion the user is feeling for this Media
	 */
	public void updateEmotion(Node node) {
		
		// Show the Window
		Main.emotionsWindow.show(InfoTool.getFileName(xPlayerModel.getSongPath()), node);
		
		// Listener
		Main.emotionsWindow.getWindow().showingProperty().addListener(new InvalidationListener() {
			/**
			 * [[SuppressWarningsSpartan]]
			 */
			@Override
			public void invalidated(Observable o) {
				
				// Remove the listener
				Main.emotionsWindow.getWindow().showingProperty().removeListener(this);
				
				// !showing?
				if (!Main.emotionsWindow.getWindow().isShowing() && Main.emotionsWindow.wasAccepted()) {
					
					//Add it the one of the emotions list
					new Thread(() -> Main.emotionListsController.makeEmotionDecisition(xPlayerModel.songPathProperty().get(), Main.emotionsWindow.getEmotion())).start();
					
					//System.out.println(Main.emotionsWindow.getEmotion())
					
				}
			}
		});
		
	}
	
	/**
	 * Opens the current Media File of the player to the default system explorer
	 */
	public void openAudioInExplorer() {
		if (xPlayerModel.songPathProperty().get() != null)
			ActionTool.openFileLocation(xPlayerModel.songPathProperty().get());
	}
	
	/**
	 * Opens a FileChooser so the user can select a song File
	 */
	public void openFileChooser() {
		File file = Main.specialChooser.selectSongFile(Main.window);
		if (file != null)
			playSong(file.getAbsolutePath());
	}
	
	/**
	 * Returns the volume level of the player.
	 *
	 * @return the volume
	 */
	public int getVolume() {
		return (int) volumeDisc.getValue();
	}
	
	/**
	 * Returns the Disc Color.
	 *
	 * @return the disc color
	 */
	public Color getDiscArcColor() {
		return disc.getArcColor();
	}
	
	/**
	 * You can use this method to add or minus from the player volume For example you can call adjustVolume(+1) or adjustVolume(-1)
	 *
	 * @param value
	 *            the value
	 */
	public void adjustVolume(int value) {
		volumeDisc.setValue(volumeDisc.getValue() + value, true);
	}
	
	/**
	 * Adjust the volume to the maximum value.
	 */
	public void maximizeVolume() {
		volumeDisc.setValue(volumeDisc.getMaximumValue() + 1.00, true);
	}
	
	/**
	 * Adjust the volume to the minimum value.
	 */
	public void minimizeVolume() {
		volumeDisc.setValue(0, true);
	}
	
	/**
	 * Set the volume to this value.
	 *
	 * @param value
	 *            the new volume
	 */
	public void setVolume(int value) {
		volumeDisc.setValue(value, true);
		
	}
	
	/**
	 * Returns the key of the player.
	 *
	 * @return The Key of the Player
	 */
	public int getKey() {
		return key;
	}
	
	public StackPane getRegionStackPane() {
		return regionStackPane;
	}
	
	public Label getFxLabel() {
		return fxLabel;
	}
	
	/**
	 * Used by resume method.
	 */
	private void resumeCode() {
		System.out.println("RESUME code....");
		
		// Stop the fade animation
		disc.stopFade();
		
		// image !=null?
		//	if (!playService.isDiscImageNull())
		checkDiscRotation();
		
		// Start the visualizer
		visualizer.startVisualizer();
		
		// Pause Image
		// radialMenu.resumeOrPause.setGraphic(radialMenu.pauseImageView)
	}
	
	/**
	 * Used by pause method.
	 */
	private void pauseCode() {
		System.out.println("PAUSE code....");
		
		// Play the fade animation
		disc.playFade();
		
		// Pause the Rotation fo disc
		disc.pauseRotation();
		
		// Stop the Visualizer
		visualizer.stopVisualizer();
		
		// Play Image
		// radialMenu.resumeOrPause.setGraphic(radialMenu.playImageView)
	}
	
	/**
	 * Controls the volume of the player.
	 */
	public void controlVolume() {
		
		try {
			//Crazy Code here.......
			// if (key == 1 || key == 2) {
			// if (djMode.balancer.getVolume() < 100) { // <100
			//
			// Main.xPlayersList.getXPlayer(1).setGain(
			// ((Main.xPlayersList.getXPlayerUI(1).getVolume() / 100.00) *
			// (djMode.balancer.getVolume()))
			// / 100.00);
			// Main.xPlayersList.getXPlayer(2).setGain(Main.xPlayersList.getXPlayerUI(2).getVolume()
			// / 100.00);
			//
			// } else if (djMode.balancer.getVolume() == 100) { // ==100
			//
			// Main.xPlayersList.getXPlayer(1).setGain(Main.xPlayersList.getXPlayerUI(1).getVolume()
			// / 100.00);
			// Main.xPlayersList.getXPlayer(2).setGain(Main.xPlayersList.getXPlayerUI(2).getVolume()
			// / 100.00);
			//
			// } else if (djMode.balancer.getVolume() > 100) { // >100
			//
			// Main.xPlayersList.getXPlayer(1).setGain(Main.xPlayersList.getXPlayerUI(1).getVolume()
			// / 100.00);
			// Main.xPlayersList.getXPlayer(2).setGain(((Main.xPlayersList.getXPlayerUI(2).getVolume()
			// / 100.00)
			// * (200 - djMode.balancer.getVolume())) / 100.00);
			//
			// }
			// } else if (key == 0) {
			xPlayer.setGain((double) volumeDisc.getValue() / 100.00);
			// }
			
			//			//Update PropertiesDB
			//			Main.dbManager.getPropertiesDb().updateProperty("XPlayer" + getKey() + "-Volume-Bar", String.valueOf(getVolume()));
			//			
			//			System.out.println(getVolume());
			
			//VisualizerStackController Label
			visualizerStackController.replayLabelEffect("Vol: " + getVolume() + " %");
			
			//Advanced Mode Volume Label
			if (modeToggle.isSelected()) {
				advModeVolumeLabel.setText(getVolume() + " %");
				fadeTransition.playFromStart();
			}
		} catch (Exception ex) {
			
			logger.log(Level.INFO, "\n", ex);
		}
		
	}
	
	/**
	 * Checks if the djDisc is being dragged by user.
	 *
	 * @return True if disc is being dragged
	 */
	public boolean isDiscBeingDragged() {
		return discIsDragging;
	}
	
	/**
	 * This method is Used by VisualizerWindow class.
	 */
	public void reAddVisualizer() {
		visualizerStackPane.getChildren().add(0, visualizerStackController);
	}
	
	/**
	 * This method is making the visualizer of the player.
	 *
	 * @param side
	 *            the side
	 */
	public void makeTheVisualizer(Side side) {
		
		// Visualizer
		visualizer = new XPlayerVisualizer(this);
		visualizer.setShowFPS(Main.settingsWindow.getxPlayersSettingsController().getShowFPS().selectedProperty().get());
		
		// Select the correct toggle
		visualizerWindow.getVisualizerTypeGroup().selectToggle(visualizerWindow.getVisualizerTypeGroup().getToggles().get(visualizer.displayMode.get()));
		
		// When displayMode is being updated
		visualizer.displayMode.addListener((observable , oldValue , newValue) -> {
			
			//Update the properties file
			Main.dbManager.getPropertiesDb().updateProperty("XPlayer" + getKey() + "-Visualizer-DisplayMode", Integer.toString(newValue.intValue()));
			
			//----------
			visualizerWindow.getVisualizerTypeGroup().selectToggle(visualizerWindow.getVisualizerTypeGroup().getToggles().get(newValue.intValue()));
			visualizerStackController.replayLabelEffect( ( (RadioMenuItem) visualizerWindow.getVisualizerTypeGroup().getSelectedToggle() ).getText());
		});
		
		// -----------visualizerTypeGroup
		visualizerWindow.getVisualizerTypeGroup().getToggles()
				.forEach(toggle -> ( (RadioMenuItem) toggle ).setOnAction(a -> visualizer.displayMode.set(visualizerWindow.getVisualizerTypeGroup().getToggles().indexOf(toggle))));
		
		// VisualizerStackController
		visualizerStackController.getChildren().add(0, visualizer);
		visualizerStackController.visibleProperty().bind(visualizerVisible.selectedProperty());
		visualizerStackController.addListenersToButtons(this);
		
		// Add VisualizerStackController to the VisualizerStackPane
		visualizerStackPane.getChildren().add(0, visualizerStackController);
		
		// visualizerSettingsHBox
		visualizerSettingsHBox.visibleProperty().bind(visualizerWindow.getStage().showingProperty().not().and(visualizerStackPane.hoverProperty()));
		
		// visualizerSettings
		visualizerSettings.setOnMouseReleased(m -> {
			Bounds bounds = visualizerSettings.localToScreen(visualizerSettings.getBoundsInLocal());
			getVisualizerWindow().getVisualizerContextMenu().show(visualizerSettings, bounds.getMinX(), bounds.getMaxY());
		});
		
		// maximizeVisualizer
		maximizeVisualizer.disableProperty().bind(visualizerVisible.selectedProperty().not());
		maximizeVisualizer.setOnAction(e -> visualizerWindow.displayVisualizer());
		
		// visualizerVisible
		visualizerVisible.selectedProperty().addListener((observable , oldValue , newValue) -> visualizerVisible.setGraphic(newValue ? eye : eyeDisabled));
		
		// visualizerVisibleLabel
		visualizerVisibleLabel.visibleProperty().bind(visualizerVisible.selectedProperty().not());
		
		// visualizerMaximizedHBox
		visualizerMaximizedBox.visibleProperty().bind(visualizerWindow.getStage().showingProperty());
		
		// visualizerMinimize
		visualizerMinimize.setOnMouseReleased(m -> visualizerWindow.removeVisualizer());
		
		// visualizerRequestFocus
		visualizerRequestFocus.setOnMouseReleased(m -> visualizerWindow.getStage().requestFocus());
		
		// playerStatusLabel
		playerStatusLabel.visibleProperty().bind(visualizer.getAnimationService().runningProperty().not());
		
		//Equalizer
		equalizer = new XPlayerEqualizer(this);
		equalizerTab.setContent(equalizer);
		
		//Pad
		//xPlayerPad = new XPlayerPad(this);
		//padTab.setContent(xPlayerPad);
		
	}
	
	/**
	 * This method is making the disc of the player.
	 *
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param color
	 *            the color
	 * @param volume
	 *            the volume
	 * @param side
	 *            the side
	 */
	public void makeTheDisc(Color color , int volume , int minimumVolume , int maximumVolume , Side side) {
		
		// initialize
		disc = new DJDisc(136, 136, color, volume, maximumVolume);
		
		//smImageView
		smImageView.imageProperty().bind(disc.getImageView().imageProperty());
		smImageView.fitWidthProperty().bind(
				Bindings.when(smModeCenterStackPane.widthProperty().lessThan(smBorderPane.widthProperty())).then(smModeCenterStackPane.widthProperty().subtract(20)).otherwise(0));
		smImageView.fitHeightProperty().bind(Bindings.when(smModeCenterStackPane.heightProperty().lessThan(smBorderPane.heightProperty()))
				.then(smModeCenterStackPane.heightProperty().subtract(20)).otherwise(0));
		
		// Canvas Mouse Moving
		disc.getCanvas().setOnMouseMoved(m -> {
			// File is either corrupted or error or no File entered yet
			if (xPlayerModel.getDuration() == 0 || xPlayerModel.getDuration() == -1)
				disc.getCanvas().setCursor(noSeekCursor);
			// !discIsDragging
			else if (!discIsDragging)
				disc.getCanvas().setCursor(Cursor.OPEN_HAND);
		});
		
		// Canvas Mouse Released
		disc.getCanvas().setOnMouseReleased(m -> {
			
			// PrimaryMouseButton
			if (m.getButton() == MouseButton.PRIMARY) {
				
				// discIsDragging and MouseButton==Primary
				// and duration!=0 and duration!=-1
				if (discIsDragging && xPlayerModel.getDuration() != 0 && xPlayerModel.getDuration() != -1) {
					
					// Try to seek
					seek(xPlayerModel.getCurrentAngleTime() - xPlayerModel.getCurrentTime());
					
				}
				
				// SecondaryMouseButton
			} else if (m.getButton() == MouseButton.SECONDARY) {
				discIsDragging = false;
			}
		});
		
		// Canvas Mouse Dragging
		disc.getCanvas().setOnMouseDragged(m -> {
			
			// MouseButton==Primary || Secondary
			if (m.getButton() == MouseButton.PRIMARY || m.getButton() == MouseButton.SECONDARY)
				
				// RadialMenu!showing and duration!=0 and duration!=-1
				//if (!radialMenu.isHidden() && 
				if (xPlayerModel.getDuration() != 0 && xPlayerModel.getDuration() != -1) {
					
					//TotalTime and CurrentTime					
					int totalTime = xPlayerModel.getDuration() , currentTime = xPlayerModel.getCurrentAngleTime();
					
					// Set the cursor
					disc.getCanvas().setCursor(Cursor.CLOSED_HAND);
					
					// Try to do the dragging
					discIsDragging = true;
					xPlayerModel.setCurrentAngleTime(disc.getValue(xPlayerModel.getDuration()));
					disc.calculateAngleByMouse(m, currentTime, totalTime);
					
					//== RemainingTimeLabel
					remainingTimeLabel.setText(InfoTool.getTimeEdited(totalTime - currentTime)); // + "." + ( 9 - Integer.parseInt(millisecondsFormatted.replace(".", "")) ))
					
					//== ElapsedTimeLabel
					elapsedTimeLabel.setText(InfoTool.getTimeEdited(currentTime)); // + millisecondsFormatted + "")
					
				}
		});
		discBorderPane.setOnScroll(scroll -> setVolume((int) Math.ceil( ( smVolumeSlider.getValue() + ( scroll.getDeltaY() > 0 ? 2 : -2 ) ))));
		
		///smTimeSlider
		smTimeSlider.setOnMouseMoved(m -> {
			// File is either corrupted or error or no File entered yet
			if (xPlayerModel.getDuration() == 0 || xPlayerModel.getDuration() == -1) {
				smTimeSlider.setCursor(noSeekCursor);
				//smTimeSlider.setDisable(true);
				// !discIsDragging
			} else if (!discIsDragging) {
				smTimeSlider.setCursor(Cursor.OPEN_HAND);
				//smTimeSlider.setDisable(false);
			}
		});
		smTimeSlider.setOnMouseReleased(m -> {
			
			//Check if the slider is not allowed to move
			if (smTimeSlider.getCursor() == noSeekCursor)
				smTimeSlider.setValue(0);
			
			// PrimaryMouseButton
			if (m.getButton() == MouseButton.PRIMARY) {
				
				// discIsDragging and MouseButton==Primary
				// and duration!=0 and duration!=-1
				if (discIsDragging && xPlayerModel.getDuration() != 0 && xPlayerModel.getDuration() != -1) {
					
					// Try to seek
					seek(xPlayerModel.getCurrentAngleTime() - xPlayerModel.getCurrentTime());
					
				} else if (!discIsDragging) {
					
					//TotalTime and CurrentTime					
					int totalTime = xPlayerModel.getDuration() , currentTime = xPlayerModel.getCurrentAngleTime();
					
					// Keep the disc refreshed based on time slider value
					xPlayerModel.setCurrentAngleTime((int) smTimeSlider.getValue());
					disc.calculateAngleByMouse(m, currentTime, totalTime);
					
					// Try to seek
					seek(xPlayerModel.getCurrentAngleTime() - xPlayerModel.getCurrentTime());
				}
				
				discIsDragging = false;
				
				// SecondaryMouseButton
			} else if (m.getButton() == MouseButton.SECONDARY)
				discIsDragging = false;
			
		});
		smTimeSlider.setOnMouseDragged(m -> {
			// MouseButton==Primary || Secondary
			if (m.getButton() == MouseButton.PRIMARY || m.getButton() == MouseButton.SECONDARY)
				
				// RadialMenu!showing and duration!=0 and duration!=-1
				if (xPlayerModel.getDuration() != 0 && xPlayerModel.getDuration() != -1) {
					
					//TotalTime and CurrentTime					
					int totalTime = xPlayerModel.getDuration() , currentTime = xPlayerModel.getCurrentAngleTime();
					
					// Set the cursor
					smTimeSlider.setCursor(Cursor.CLOSED_HAND);
					
					// Try to do the dragging
					discIsDragging = true;
					xPlayerModel.setCurrentAngleTime((int) smTimeSlider.getValue());
					disc.calculateAngleByMouse(m, currentTime, totalTime);
					
					//smTimeSliderLabel
					smTimeSliderLabel.setText(InfoTool.getTimeEdited(currentTime) + "  / " + InfoTool.getTimeEdited(totalTime));
				}
		});
		
		//---VBOX------
		//		VBox vBox = new VBox();
		//		
		//volumeDisc
		volumeDisc = new DJFilter(30, 30, Color.MEDIUMSPRINGGREEN, volume, minimumVolume, maximumVolume, DJFilterCategory.VOLUME_FILTER);
		volumeDisc.addDJDiscListener(this);
		
		//smVolumeSlider
		smVolumeSlider.setMin(0);
		smVolumeSlider.setMax(maximumVolume - 1.00);
		smVolumeSlider.valueProperty().addListener(l -> {
			
			//Set the value to the volumeDisc
			volumeDisc.setValue(smVolumeSlider.getValue(), false);
			
			//Update the Volume
			controlVolume();
			
			//Update the Label
			smVolumeSliderLabel.setText((int) smVolumeSlider.getValue() + " %");
			
			//Change the disc value
			disc.setVolume((int) ( smVolumeSlider.getValue() * 100 ));
			
		});
		smVolumeSlider.setValue(volume);
		smVolumeSlider.setOnScroll(scroll -> setVolume((int) Math.ceil( ( smVolumeSlider.getValue() + ( scroll.getDeltaY() > 0 ? 2 : -2 ) ))));
		
		//Recalculate Volume Disc Size
		discBorderPane.boundsInLocalProperty().addListener((observable , oldValue , newValue) -> reCalculateDiscStackPane());
		
		//Add disc and volume disc to StackPane
		diskStackPane.getChildren().addAll(disc, volumeDisc);
	}
	
	/**
	 * Recalculates the Canvas size to the preferred size
	 */
	private void reCalculateCanvasSize() {
		//double size = Math.min(diskStackPane.getWidth(), diskStackPane.getHeight()) / 1.1;
		
		double size = Math.min(discBorderPane.getWidth(), discBorderPane.getHeight() - diskStackPane1.getHeight()) / 1.1;
		
		disc.resizeDisc(size, size);
		//radialMenu.getRadialMenuButton().setPrefSize(disc.getMinWidth(), disc.getMinHeight())
		//System.out.println("Redrawing canvas")
	}
	
	/**
	 * Makes the DJDisc fit correctly into it's StackPane
	 */
	public void reCalculateDiscStackPane() {
		
		//Call it for the DJDisc
		reCalculateCanvasSize();
		
		//System.out.println(disc.getPrefWidth())
		
		//Find the correct size for the VolumeDisc			
		double size;
		if (disc.getPrefWidth() < 80)
			size = disc.getPrefWidth() / 1.55;
		else if (disc.getPrefWidth() < 165)
			size = disc.getPrefWidth() / 1.25;
		else
			size = disc.getPrefWidth() / 1.15;
		
		volumeDisc.resizeDisc(size, size);
	}
	
	/**
	 * When the audio starts , fast configure it's settings
	 * 
	 * @param ignoreStartImmediately
	 */
	public void configureMediaSettings(boolean ignoreStartImmediately) {
		
		// Start immediately?
		if (!ignoreStartImmediately && !Main.settingsWindow.getxPlayersSettingsController().getStartImmediately().isSelected())
			pause();
		else {
			play();
			resume();
		}
		
		// Mute?
		xPlayer.setMute(muteButton.isSelected());
		//System.out.println("Mute is Selected? " + muteButton.isSelected())
		
		// Volume
		controlVolume();
		
		// Audio is MP3?
		if (!"mp3".equals(xPlayerModel.songExtensionProperty().get()))
			equalizer.setDisable(true);
		else {
			xPlayer.setEqualizer(xPlayerModel.getEqualizerArray(), 32);
			equalizer.setDisable(false);
		}
		
		// Sets Pan value. Line should be opened before calling this method.
		// Linear scale : -1.0 <--> +1.0
		xPlayer.setPan(equalizer.getPanFilter().getValueTransformed());
		//System.out.println("Eq Pan value :" + equalizer.getPanFilter().getValueTransformed());
		
		// Represents a control for the relative balance of a stereo signal
		// between two stereo speakers. The valid range of values is -1.0 (left
		// channel only) to 1.0 (right channel only). The default is 0.0
		// (centered).
		//xPlayer.setBalance((float) equalizer.getBalanceFilter().getValueTransformed());
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void opened(Object dataSource , Map<String,Object> map) {
		// some code here
	}
	
	float progress;
	
	@Override
	public void progress(int nEncodedBytes , long microSecondsPosition , byte[] pcmdata , Map<String,Object> properties) {
		//System.out.println("Entered....");
		
		//Check if DSP is allowed
		//if (Main.settingsWindow.getGeneralSettingsController().getHighGraphicsToggle().isSelected())
		visualizer.writeDSP(pcmdata);
		
		if (!isDiscBeingDragged()) {
			
			// previousTime = xPlayerUI.xPlayer.currentTime
			
			// .MP3 OR .WAV
			String extension = xPlayerModel.songExtensionProperty().get();
			if ("mp3".equals(extension) || "wav".equals(extension)) {
				
				// Calculate the progress until now
				progress = ( nEncodedBytes > 0 && xPlayer.getTotalBytes() > 0 ) ? ( nEncodedBytes * 1.0f / xPlayer.getTotalBytes() * 1.0f ) : -1.0f;
				// System.out.println(progress*100+"%")
				if (visualizerWindow.isVisible())
					Platform.runLater(() -> visualizerWindow.getProgressBar().setProgress(progress));
				
				// find the current time in seconds
				xPlayerModel.setCurrentTime((int) ( xPlayerModel.getDuration() * progress ));
				// System.out.println((double) xPlayerModel.getDuration() *
				// progress)
				
				// .WHATEVER MUSIC FILE*
			} else
				xPlayerModel.setCurrentTime((int) ( microSecondsPosition / 1000000 ));
			
			String millisecondsFormatted = InfoTool.millisecondsToTime(microSecondsPosition / 1000);
			// System.out.println(milliFormat)
			
			//Paint the Modes
			if (!xPlayer.isStopped()) {
				
				//TotalTime and CurrentTime
				int totalTime = xPlayerModel.getDuration() , currentTime = xPlayerModel.getCurrentTime();
				
				if (!modeToggle.isSelected()) { //Simple Mode for most of Users
					
					//Run on JavaFX Thread
					Platform.runLater(() -> {
						
						//Simple Mode
						smTimeSlider.setMin(0);
						smTimeSlider.setMax(totalTime);
						smTimeSlider.setValue(currentTime);
						
						//smTimeSliderLabel
						smTimeSliderLabel.setText(InfoTool.getTimeEdited(currentTime) + "." + ( 9 - Integer.parseInt(millisecondsFormatted.replace(".", "")) ) + "  / "
								+ InfoTool.getTimeEdited(totalTime));
					});
					
				} else { //Advanced DJ Disc Mode
					
					// Update the disc Angle
					disc.calculateAngleByValue(xPlayerModel.getCurrentTime(), xPlayerModel.getDuration(), false);
					
					// Update the disc time
					disc.updateTimeDirectly(xPlayerModel.getCurrentTime(), xPlayerModel.getDuration(), millisecondsFormatted);
					
					//Run on JavaFX Thread
					Platform.runLater(() -> {
						
						//== RemainingTimeLabel
						remainingTimeLabel.setText(InfoTool.getTimeEdited(totalTime - currentTime) + "." + ( 9 - Integer.parseInt(millisecondsFormatted.replace(".", "")) ));
						
						//== ElapsedTimeLabel
						elapsedTimeLabel.setText(InfoTool.getTimeEdited(currentTime) + millisecondsFormatted);
						
						//if (xPlayerController != null && xPlayerController.getDisc() != null)
						disc.repaint();
						
					});
				}
				
			}
			
			// if (!visualizer.isRunning())
			// Platform.runLater(this::resumeCode);
			
		}
		
		// System.out.println(xPlayer.currentTime)
	}
	
	@Override
	public void statusUpdated(StreamPlayerEvent streamPlayerEvent) {
		
		// Status.OPENED
		if (streamPlayerEvent.getPlayerStatus() == Status.OPENED && xPlayer.getSourceDataLine() != null) {
			
			visualizer.setupDSP(xPlayer.getSourceDataLine());
			visualizer.startDSP(xPlayer.getSourceDataLine());
			
			Platform.runLater(() -> {
				//Marquee Text
				mediaFileMarquee.setText(InfoTool.getFileName(xPlayerModel.songPathProperty().get()));
				
				//Notification
				if (Main.settingsWindow.getxPlayersSettingsController().getShowPlayerNotifications().isSelected()) {
					
					//Check if it has Album Image
					Image image = InfoTool.getAudioAlbumImage(xPlayerModel.songPathProperty().get(), 60, 60);
					
					//Show Notification
					ActionTool.showNotification("Player [ " + this.getKey() + " ] Opened", InfoTool.getFileName(xPlayerModel.songPathProperty().get()), Duration.seconds(4),
							NotificationType.SIMPLE, JavaFXTools.getImageView(image != null ? image : MediaInformation.MISSING_ARTWORK_IMAGE, 60, 60));
				}
			});
			
			// Status.RESUMED			
		} else if (streamPlayerEvent.getPlayerStatus() == Status.RESUMED) {
			
			Platform.runLater(() -> {
				playerStatusLabel.setText("Resuming");
				resumeCode();
				
				//Notification
				//ActionTool.showNotification("Player [ " + this.getKey() + " ] Resuming", InfoTool.getFileName(xPlayerModel.songPathProperty().get()), Duration.seconds(2),
				//		NotificationType.SIMPLE, InfoTool.getAudioAlbumImage(xPlayerModel.songPathProperty().get(), 60, 60));
			});
			
			// Status.PLAYING
		} else if (streamPlayerEvent.getPlayerStatus() == Status.PLAYING) {
			
			Platform.runLater(this::resumeCode);
			
			// Status.PAUSED
		} else if (streamPlayerEvent.getPlayerStatus() == Status.PAUSED) {
			
			Platform.runLater(() -> {
				playerStatusLabel.setText("Paused");
				pauseCode();
				
				//Notification
				//	ActionTool.showNotification("Player [ " + this.getKey() + " ] Paused", InfoTool.getFileName(xPlayerModel.songPathProperty().get()), Duration.seconds(2),
				//			NotificationType.SIMPLE, InfoTool.getAudioAlbumImage(xPlayerModel.songPathProperty().get(), 60, 60));
			});
			
			// Status.STOPPED
		} else if (streamPlayerEvent.getPlayerStatus() == Status.STOPPED) {
			
			visualizer.stopDSP();
			
			Platform.runLater(() -> {
				
				// SeekService running?
				if (seekService.isRunning()) {
					
					// oh yeah
					
				} else {
					
					// Set time to 0 to not have problems with SeekService
					xPlayerModel.setCurrentTime(0);
					
					// Change Marquee text
					//mediaFileMarquee.setText("Player is Stopped");
					playerStatusLabel.setText("Stopped");
					
					disc.calculateAngleByValue(0, 0, true);
					disc.repaint();
					
					// disk
					disc.stopRotation();
					disc.stopFade();
					
					// Visualizer
					visualizer.stopVisualizer();
					
					// Play Image
					// radialMenu.resumeOrPause.setGraphic(radialMenu.playImageView);
					
					//== RemainingTimeLabel
					remainingTimeLabel.setText("00:00");
					
					//== ElapsedTimeLabel
					elapsedTimeLabel.setText("00:00");
					
					// Notification
					//ActionTool.showNotification("Player " + this.getKey(), "Player[ " + this.getKey() + " ] has stopped...", Duration.millis(500), NotificationType.SIMPLE);
					
					//== Visualizer Window 
					visualizerWindow.getProgressBar().setProgress(0);
					
					//smTimeSlider
					smTimeSlider.setValue(0);
					
					//smTimeSliderLabel
					smTimeSliderLabel.setText(InfoTool.getTimeEdited(0) + "  / " + InfoTool.getTimeEdited(xPlayerModel.getDuration()));
				}
				
			});
			
			// Status.SEEKING
		} else if (streamPlayerEvent.getPlayerStatus() == Status.SEEKING) {
			
			Platform.runLater(() -> playerStatusLabel.setText("Seeking"));
			
			// Status.SEEKED
		} else if (streamPlayerEvent.getPlayerStatus() == Status.SEEKED) {
			//TODO i need to add code here
		}
	}
	
	//	@Override
	//	public void volumeChanged(int volume) {
	//		controlVolume();
	//	}
	
	/**
	 * Replay the current song
	 */
	public void replay() {
		
		if (xPlayerModel.songExtensionProperty().get() != null)
			playService.startPlayService(xPlayerModel.songPathProperty().get(), 0);
		else
			ActionTool.showNotification("No Previous File", "Drag and Drop or Add a File or URL on this player.", Duration.millis(1500), NotificationType.INFORMATION);
		
		// if (thisSong instanceof URL)
		// return playSong(((URL) thisSong).toString(), totalTime);
		// else if (thisSong instanceof File)
		// return playSong(((File) thisSong).getAbsolutePath(), totalTime);
		
	}
	
	/**
	 * Play the current song.
	 *
	 * @param absolutePath
	 *            The absolute path of the file
	 */
	public void playSong(String absolutePath) {
		
		playService.startPlayService(absolutePath, 0);
		
	}
	
	/**
	 * Play the current song.
	 *
	 * @param absolutePath
	 *            The absolute path of the file
	 * @param startingSecond
	 *            From which second to start the audio , this will not be exactly accurate
	 */
	public void playSong(String absolutePath , int startingSecond) {
		
		playService.startPlayService(absolutePath, startingSecond);
		
	}
	
	//---------------------------------------------------Player Actions------------------------------------------------------------------
	
	/**
	 * Tries to skip forward or backward
	 * 
	 * @param seconds
	 *            Seconds to seek
	 */
	public void seek(int seconds) {
		boolean ok = false;
		if (seconds == 0)
			return;
		
		//
		
		if (seconds < 0 && ( seconds + xPlayerModel.getCurrentTime() >= 0 )) { //negative seek
			
			System.out.println("Skipping backwards ...[" + seconds + "] seconds");
			
			ok = true;
		} else if (seconds > 0 && ( seconds + xPlayerModel.getCurrentTime() <= xPlayerModel.getDuration() )) { //positive seek
			
			System.out.println("Skipping forward ...[" + seconds + "] seconds");
			
			ok = true;
		}
		
		//Ok/?
		if (ok) {
			
			// Add or Remove
			xPlayerModel.setCurrentAngleTime(xPlayerModel.getCurrentTime() + seconds);
			
			//	    //Seek
			//	    System.out.println("Original: "
			//		    + (xPlayerModel.getCurrentAngleTime()) * (xPlayer.getTotalBytes() / xPlayerModel.getDuration())
			//		    + " With double:" + (long) (((float) xPlayerModel.getCurrentAngleTime())
			//			    * (xPlayer.getTotalBytes() / (float) xPlayerModel.getDuration())))
			
			//Start the Service
			seekService.startSeekService((long) ( ( (float) xPlayerModel.getCurrentAngleTime() ) * ( xPlayer.getTotalBytes() / (float) xPlayerModel.getDuration() ) ), false);
		}
		
	}
	
	/**
	 * This method is used to seek to a specific time of the audio
	 * 
	 * @param seconds
	 */
	public void seekTo(int seconds) {
		
		if (seconds < 0 || seconds >= xPlayerModel.getDuration())
			return;
		
		// Set
		xPlayerModel.setCurrentAngleTime(seconds);
		
		//Seek To
		seekService.startSeekService( ( xPlayerModel.getCurrentAngleTime() ) * ( xPlayer.getTotalBytes() / xPlayerModel.getDuration() ), true);
		
	}
	
	/**
	 * Set the mute of the Line. Note that mute status does not affect gain.
	 *
	 * @param mute
	 *            True to mute the audio of False to unmute it
	 */
	public void setMute(boolean value) {
		muteButton.setSelected(value);
		xPlayer.setMute(value);
		
	}
	
	/**
	 * Starts the player
	 */
	public void play() {
		try {
			xPlayer.play();
		} catch (StreamPlayerException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Resume the player.
	 */
	public void resume() {
		xPlayer.resume();
	}
	
	/**
	 * Pause the player.
	 */
	public void pause() {
		xPlayer.pause();
	}
	
	/**
	 * Stop the player.
	 */
	public void stop() {
		xPlayer.stop();
	}
	
	/**
	 * Reverse Play with Pause or the opposite.
	 */
	public void reversePlayAndPause() {
		if (xPlayer.isPlaying()) // playing?
			pause();
		else if (xPlayer.isPaused()) // paused?
			resume();
		else if (xPlayer.isStopped() || xPlayer.isUnknown())
			replay();
		
	}
	
	/**
	 * If the player is paused it resume it else if it is stopped it replays the Media
	 */
	public void playOrReplay() {
		if (xPlayer.isPaused()) // paused?
			resume();
		else if (xPlayer.isStopped() || xPlayer.isUnknown())
			replay();
	}
	
	/**
	 * @return the xPlayerWindow
	 */
	public XPlayerWindow getxPlayerWindow() {
		return xPlayerWindow;
	}
	
	/**
	 * @return the playerExtraSettings
	 */
	public XPlayerExtraSettings getPlayerExtraSettings() {
		return playerExtraSettings;
	}
	
	/**
	 * @return the xPlayerModel
	 */
	public XPlayerModel getxPlayerModel() {
		return xPlayerModel;
	}
	
	/**
	 * @return the xPlayer
	 */
	public XPlayer getxPlayer() {
		return xPlayer;
	}
	
	//	/**
	//	 * @return the radialMenu
	//	 */
	//	public XPlayerRadialMenu getRadialMenu() {
	//		return radialMenu;
	//	}
	
	/**
	 * @return the visualizerWindow
	 */
	public VisualizerWindowController getVisualizerWindow() {
		return visualizerWindow;
	}
	
	/**
	 * @return the visualizerStackController
	 */
	public VisualizerStackController getVisualizerStackController() {
		return visualizerStackController;
	}
	
	/**
	 * @return the visualizer
	 */
	public XPlayerVisualizer getVisualizer() {
		return visualizer;
	}
	
	/**
	 * @return the equalizer
	 */
	public XPlayerEqualizer getEqualizer() {
		return equalizer;
	}
	
	/**
	 * @return the disc
	 */
	public DJDisc getDisc() {
		return disc;
	}
	
	/**
	 * @return the xPlayerPlayList
	 */
	public XPlayerPlaylist getxPlayerPlayList() {
		return xPlayerPlayList;
	}
	
	/**
	 * @return the mediaTagImageButton
	 */
	public Button getMediaTagImageButton() {
		return mediaTagImageButton;
	}
	
	/**
	 * @return the backwardButton
	 */
	public Button getBackwardButton() {
		return backwardButton;
	}
	
	/**
	 * @return the forwardButton
	 */
	public Button getForwardButton() {
		return forwardButton;
	}
	
	/**
	 * @return the totalTimeLabel
	 */
	public Label getTotalTimeLabel() {
		return totalTimeLabel;
	}
	
	/**
	 * @param totalTimeLabel
	 *            the totalTimeLabel to set
	 */
	public void setTotalTimeLabel(Label totalTimeLabel) {
		this.totalTimeLabel = totalTimeLabel;
	}
	
	/**
	 * @return the playPauseButton
	 */
	public Button getPlayPauseButton() {
		return playPauseButton;
	}
	
	/**
	 * @return the emotionsButton
	 */
	public Button getEmotionsButton() {
		return emotionsButton;
	}
	
	/**
	 * @return the playService
	 */
	public XPlayerPlayService getPlayService() {
		return playService;
	}
	
	@Override
	public void valueChanged(double value) {
		//controlVolume();
		//volumeDiscLabel.setText(String.valueOf((int) value))
		smVolumeSlider.setValue(value);
		//disc.setVolume((int) ( value * 100 ));
	}
	
	/**
	 * @return the mediaFileMarquee
	 */
	public Marquee getMediaFileMarquee() {
		return mediaFileMarquee;
	}
	
	/**
	 * @return the diskStackPane
	 */
	public StackPane getDiskStackPane() {
		return diskStackPane;
	}
	
	/**
	 * @return the visualizationsDisabledLabel
	 */
	public Label getVisualizationsDisabledLabel() {
		return visualizationsDisabledLabel;
	}
	
	/**
	 * @return the smPlayPauseButton
	 */
	public Button getSmPlayPauseButton() {
		return smPlayPauseButton;
	}
	
	/**
	 * @return the modeToggle
	 */
	public JFXToggleButton getModeToggle() {
		return modeToggle;
		
	}
	
	/**
	 * @return the isExtended
	 */
	public boolean isExtended() {
		return isPlayerExtended;
	}
	
	/**
	 * @return the showVisualizer
	 */
	public JFXToggleButton getShowVisualizer() {
		return showVisualizer;
	}
	
}
