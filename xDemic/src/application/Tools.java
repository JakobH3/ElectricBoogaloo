package application;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Tools extends VBox {
	private MainView mainView;
	
	public Tools(MainView mainView) {
		this.mainView=mainView;
		
		// create menu bar
		MenuBar menu = new MenuBar();
			Menu file = new Menu("File");
				Menu add = new Menu("New");
					MenuItem malware = new MenuItem("Malware");
					malware.setOnAction(this::handleMalware);
					MenuItem device = new MenuItem("Device");
					device.setOnAction(this::handleDevice);
				add.getItems().addAll(malware, device);
				
				Menu open = new Menu("Open");
					MenuItem ex = new MenuItem("Example 1");
					ex.setOnAction(this::handleLoad);
				open.getItems().addAll(ex);
				
				MenuItem save = new MenuItem("Save");
				save.setOnAction(this::handleSave);
					
				MenuItem reset = new MenuItem("Reset");
				reset.setOnAction(this::handleReset);
			file.getItems().addAll(add, open, save, reset);
			
			Menu help = new Menu("Help");
				MenuItem helpDevices = new MenuItem("Devices");
				helpDevices.setOnAction(this::handleHelp);
			help.getItems().addAll(helpDevices);
		menu.getMenus().addAll(file, help);
		
		// create tool bar
		ToolBar tools = new ToolBar();
        
        Button start = new Button();
        start.setOnAction(this::handleStart);
        start.setGraphic(makeIcon(new Image("file:play.png")));
		
		Button stop = new Button();
		stop.setOnAction(this::handleStop);
		stop.setGraphic(makeIcon(new Image("file:pause.png")));
		
		Button config = new Button();
		config.setOnAction(this::handleConfig);
		config.setGraphic(makeIcon(new Image("file:config.png")));
		
		tools.getItems().addAll(start, stop, config);
		
		getChildren().addAll(menu, tools);
	}
	
	private void handleMalware(ActionEvent actionEvent) {
        if(mainView.editing()) mainView.getMalware().add();
        else System.out.println("# Simulation in progress!");
    }
	
	private void handleDevice(ActionEvent actionEvent) {
		if(mainView.editing()) mainView.getEnvironment().add();
		else System.out.println("# Simulation in progress!");
    }
	
	private void handleSave(ActionEvent actionEvent) {
		if(mainView.editing()) {
			// TODO save mainView - maybe able to use file i/o?
			System.out.println("Saved!");
		} else {
			System.out.println("# Simulation in progress!");
		}
	}
	
	private void handleLoad(ActionEvent actionEvent) {
		if(mainView.editing()) {
			mainView.getSimulation().loadExample();
			mainView.draw();
		} else {
			System.out.println("# Simulation in progress!");
		}
	}
	
	private void handleReset(ActionEvent actionEvent) {
		if(mainView.editing()) {
			mainView.getSimulation().clear();
			mainView.getSimulator().setFrameRate(60);
			mainView.getSimulator().reset();
			mainView.draw();
		} else {
			System.out.println("# Simulation in progress!");
		}
	}
	
	private void handleHelp(ActionEvent actionEvent) {
		VBox helpList = new VBox();
		helpList.setSpacing(10);
		helpList.getChildren().add(new Text("Shift drag to move devices"));
		helpList.getChildren().add(new Text("Ctrl drag to connect devices"));
		
		Scene helpScene = new Scene(helpList, 400, 400);
		
		Stage helpStage = new Stage();
		helpStage.setTitle("xDemic Help");
		helpStage.setScene(helpScene);
		helpStage.show();
	}
	
	private void handleStart(ActionEvent actionEvent) {
		if(mainView.editing()) {
			if(!(mainView.getSimulation().getDeviceList().isEmpty()) && !(mainView.getSimulation().getMalwareList().isEmpty()) && !(mainView.getSimulation().getConnectionList().isEmpty())) {
				mainView.getSimulator().start();
			} else {
				System.out.println("# Simulation compoenent missing! Try creating devices, malware, or connections.");
			}
		} else {
			System.out.println("# Simulation in progress!");
		}
    }
	
	private void handleStop(ActionEvent actionEvent) {
		if(!mainView.editing()) mainView.getSimulator().stop();
		else System.out.println("# Simulation not in progress!");
    }
	
	private void handleConfig(ActionEvent actionEvent) {
		if(mainView.editing()) {
			// TODO create simulation configuration window
			
			VBox options = new VBox();
			options.getStyleClass().add("options");
			
			Text ddLabel = new Text("Device to Device Mobility Factor");
			Slider dd = new Slider(0, 100, 10);
			dd.setShowTickLabels(true);
			dd.setShowTickMarks(true);
			
			Text dnLabel = new Text("Device to Node Mobility Factor");
			Slider dn = new Slider(0, 100, 10);
			dn.setShowTickLabels(true);
			dn.setShowTickMarks(true);
			
			Text nnLabel = new Text("Node to Node Mobility Factor");
			Slider nn = new Slider(0, 100, 10);
			nn.setShowTickLabels(true);
			nn.setShowTickMarks(true);
			
			Text fpsLabel = new Text("Frames per second");
			Slider fps = new Slider(1, 100, 10);
			fps.setShowTickLabels(true);
			fps.setShowTickMarks(true);
			fps.setSnapToTicks(true);
			
			Button apply = new Button("Apply");
			apply.setOnAction((e) -> {
				mainView.getSimulation().setDdMobility(dd.getValue());
				mainView.getSimulation().setDnMobility(dn.getValue());
				mainView.getSimulation().setNnMobility(nn.getValue());
				mainView.getSimulator().setFrameRate((int) fps.getValue());
				mainView.getSimulator().reset();
				mainView.setCenter(mainView.getOutput());
				mainView.draw();
			});
		
			Button cancel = new Button("Cancel");
			cancel.setOnAction((e) -> {
				mainView.setCenter(mainView.getOutput());
				mainView.draw();
			});
			
			options.getChildren().addAll(ddLabel, dd, dnLabel, dn, nnLabel, nn, fpsLabel, fps, apply, cancel);
			mainView.setCenter(options);
		} else {
			System.out.println("# Simulation in progress!");
		}
	}
	
	private ImageView makeIcon(Image image) {
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(9);
		imageView.setFitHeight(10);
		return imageView;
	}
}