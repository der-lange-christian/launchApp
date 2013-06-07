package de.cutl.chris.project.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LauncherCtr implements Initializable {

    private Properties props;
    
    @FXML
    TextField input;
    
    @FXML
    ListView<String> commands;
    
    List<String> allCommands = new ArrayList<String>();
    
    @FXML
    public void exit(ActionEvent event) {
        Platform.exit();
    }


    public void initialize(URL arg0, ResourceBundle arg1) {
        input.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> o, String oldVal, String newVal) {
                ObservableList<String> items = filter(oldVal, newVal);
                commands.setItems(items);
            }
        });
        
        ObservableList<String> items = initList();
        
        allCommands.addAll(items);
        commands.setItems(items);
    }
    
    private ObservableList<String> initList() {
        props = new Properties();
        
        File propFile = new File("commands.props");
        if (!propFile.exists()) {
            System.out.println("can not find File: " + propFile.getAbsolutePath());
        }
        
        try {
            props.load(new FileInputStream(propFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ObservableList<String> items = FXCollections.observableArrayList();
        
        for (Entry<Object, Object> entry : props.entrySet()) {
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            
            if (!(new File(val)).exists()) {
                throw new RuntimeException("Script mit dem Name " + val + " existiert nicht.");
            }
            
            items.add(key);
        }

        return items;
    }


    private ObservableList<String> filter(String oldVal, String newVal) {
        String cmdPart = newVal.toLowerCase();
        ObservableList<String> items = FXCollections.observableArrayList();
        if (oldVal.length() < newVal.length()) {
            for (String cmd : commands.getItems()) {
                if (cmd.toLowerCase().startsWith(cmdPart)) {
                    items.add(cmd);
                }
            }
        } else {
            for (String cmd : allCommands) {
                if (cmd.toLowerCase().startsWith(cmdPart)) {
                    items.add(cmd);
                }
            }
        }
        return items;
    }
    
    
    
    @FXML 
    protected void commandsMouseClicked(MouseEvent event) {
        System.out.println("source: " + event.getSource());
        System.out.println("target: " + event.getTarget());
        String selected = commands.getSelectionModel().getSelectedItem();
        System.out.println("select: " + selected);
        System.out.println("OK");
        action(selected);
     }
    
    private void action(String cmd) {
        String shellScript = props.getProperty(cmd);
        
        try {
            Runtime.getRuntime().exec(shellScript);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
