package com.sopra.banking.trafficlightapp;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.sopra.banking.trafficlightapp.model.TrafficLightConfigData;
import com.sopra.banking.trafficlightapp.view.TrafficLightOverviewController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {
	
	private Stage primaryStage;
	public ClassLoader classLoader;
    private AnchorPane rootLayout;
    private TrafficLightConfigData trafficLightConfigData;
    
    public Main() {
		classLoader = this.getClass().getClassLoader();

		Gson gson = new Gson();
		InputStream is = classLoader.getResourceAsStream("data.json");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		trafficLightConfigData = gson.fromJson(reader, TrafficLightConfigData.class);  
    }
    
    public TrafficLightConfigData getTrafficLightConfigData() {
    	return trafficLightConfigData;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Traffic Light Application");
        
        Platform.setImplicitExit(false);
        
        SwingUtilities.invokeLater(this::addTrayIcon);
        
        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
        	// Handle window close button
        	primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        		@Override
                public void handle(WindowEvent event) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
        	                Platform.exit();
                            System.exit(0);
                        }
                    });
                }
            });
        	
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader(classLoader.getResource("view/TrafficLightOverview.fxml"));
            rootLayout = (AnchorPane) loader.load();
            
            // Give the controller access to the main app.
            TrafficLightOverviewController controller = loader.getController();
            controller.setMain(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addTrayIcon() {
    	if (SystemTray.isSupported()) {
            try {
            	java.awt.Toolkit.getDefaultToolkit();
            	
	            SystemTray tray = SystemTray.getSystemTray();
	            Image image = ImageIO.read(classLoader.getResourceAsStream("img/icon.jpg"));
	            
	            MenuItem openItem = new MenuItem("Open");
	            MenuItem hideItem = new MenuItem("Hide");
	            java.awt.Font defaultFont = java.awt.Font.decode(null);
	            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
	            openItem.setFont(boldFont);
	            MenuItem exitItem = new MenuItem("Exit");
	            
	            TrayIcon trayIcon = new TrayIcon(image);
	            trayIcon.setImageAutoSize(true);
	            
	            openItem.addActionListener(event -> Platform.runLater(this::showPrimaryStage));
	            hideItem.addActionListener(event -> Platform.runLater(this::hidePrimaryStage));
	            exitItem.addActionListener(event -> {
	                tray.remove(trayIcon);
	                Platform.exit();
	                System.exit(0);
	            });
	
	            trayIcon.addActionListener(event -> Platform.runLater(this::showOrHidePrimaryStage));                    

	            PopupMenu popup = new PopupMenu();
	            popup.add(openItem);
	            popup.add(hideItem);
	            popup.addSeparator();
	            popup.add(exitItem);
	            trayIcon.setPopupMenu(popup);
	            
	            tray.add(trayIcon);
	            
            } catch (Exception e) {
            	System.err.println("Can't add to tray: " + e);
            }
            
    	} else {
        	System.err.println("Tray unavailable.");
    	}
    }
    
    private void showPrimaryStage() {
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }
    
    private void hidePrimaryStage() {
        if (primaryStage != null) {
        	primaryStage.hide();
        }
    }
    
    private void showOrHidePrimaryStage() {
        if (primaryStage != null && primaryStage.isShowing()) {
        	hidePrimaryStage();
        }
        else {
        	showPrimaryStage();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
