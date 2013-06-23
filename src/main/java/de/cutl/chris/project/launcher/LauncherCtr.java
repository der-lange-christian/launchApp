package de.cutl.chris.project.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class LauncherCtr implements Initializable {

    private Properties props;

    @FXML
    TextField input;
    
    @FXML
    Label info;

    @FXML
    ListView<String> commands;

    List<String> allCommands = new ArrayList<String>();

    @FXML
    public void exit(ActionEvent event) {
        exit();
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        input.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> o, String oldVal, String newVal) {
                ObservableList<String> items = filter(oldVal, newVal);
                commands.setItems(items);
            }
        });

        initKeyEvents();
        initInfoLabel();
        ObservableList<String> items = initList();

        allCommands.addAll(items);
        commands.setItems(items);
    }

    private void initInfoLabel() {
        info.setText("Hello");
        
        LauncherCtr.class.getPackage().getImplementationTitle();
        LauncherCtr.class.getPackage().getImplementationVendor();
        LauncherCtr.class.getPackage().getImplementationVersion();
        
        LauncherCtr.class.getPackage().getSpecificationTitle();
        LauncherCtr.class.getPackage().getSpecificationVendor();
        LauncherCtr.class.getPackage().getSpecificationVersion();
    }

    private void initKeyEvents() {
        input.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                String key = keyEvent.getCode().getName();
//                System.out.println("text:    " + keyEvent.getText());
//                System.out.println("keyCode: " + keyEvent.getCode().getName());

                switch (key) {
                case "Esc":
                    exit();
                    break;
                case "Up":
                    goUp();
                    break;
                case "Down":
                    goDown();
                    break;
                case "Enter":
                    enter();
                    break;
                case "Rigth":
                    break;
                case "Left":
                    break;
                default:
                    // ignore this key
                }
            }
        });

    }

    private void enter() {
        String selected = commands.getSelectionModel().getSelectedItem();
        execute(selected);
        exit();
    }
    
    
    protected void goDown() {
        int selectedCommand = commands.getSelectionModel().getSelectedIndex();
        ++selectedCommand;
        if (selectedCommand >= commands.getItems().size()) {
            // liste von ganz unten wieder von oben beginnen
            selectedCommand = 0;
        }
        commands.getSelectionModel().select(selectedCommand);
    }
    
    protected void goUp() {
        int selectedCommand = commands.getSelectionModel().getSelectedIndex();
        --selectedCommand;
        if (selectedCommand <= -1) {
            // Liste von ganz oben wieder von unten beginnen
            selectedCommand = commands.getItems().size() - 1;
        }
        commands.getSelectionModel().select(selectedCommand);
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

            if (isScriptAvailable(val)) {
                throw new RuntimeException("Script mit dem Name " + val + " existiert nicht.");
            }

            items.add(key);
        }

        Collections.sort(items);
        return items;
    }
    
    protected boolean isScriptAvailable(String scriptCmd) {
        String cmdPart = removeArguments(scriptCmd);
        return !(new File(cmdPart)).exists();
    }

    protected String removeArguments(String scriptCmd) {
        String[] parts = scriptCmd.split(" ");
        String cmd = parts[0];
        return cmd;
    }

    private ObservableList<String> filter(String oldVal, String newVal) {
        String cmdPart = newVal.toLowerCase();
        ObservableList<String> items = FXCollections.observableArrayList();
        if (oldVal.length() < newVal.length()) {
            for (String cmd : commands.getItems()) {
                if (cmd.toLowerCase().contains(cmdPart)) {
                    items.add(cmd);
                }
            }
        } else {
            for (String cmd : allCommands) {
                if (cmd.toLowerCase().contains(cmdPart)) {
                    items.add(cmd);
                }
            }
        }
        Collections.sort(items);
        return items;
    }

    @FXML
    protected void commandsMouseClicked(MouseEvent event) {
        System.out.println("source: " + event.getSource());
        System.out.println("target: " + event.getTarget());
        String selected = commands.getSelectionModel().getSelectedItem();
        //System.out.println("select: " + selected);
        //System.out.println("OK");
        execute(selected);
    }

    private void execute(String cmd) {
        String shellScript = props.getProperty(cmd);

        try {
            Runtime.getRuntime().exec(shellScript);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void exit() {
        Platform.exit();
    }
}
