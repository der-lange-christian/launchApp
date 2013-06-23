package de.cutl.chris.project.launcher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Anwendung die nur eine Instanz hat. Bei zweitem Start wird erste Instanz wieder
 * in den Vordergrund geholt.
 * 
 * SWING HACKS Hack#84 - Costruct Single-Launch Application
 *
 * @author chris
 *
 */
public class LauncherApp extends Application {

    private static final int PORT = 38629;
    private static ServerSocket server;
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FXML TableView Example");
        Pane myPane = (Pane) FXMLLoader.load(getClass().getResource("/launcher.fxml"));
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();

        LauncherApp.primaryStage = primaryStage;
    }

    public static void main(String[] args) {
        try {
            server = new ServerSocket(PORT);
            Thread relauncher = new Thread(new RelanchWatcher());
            relauncher.setDaemon(true);
            relauncher.setName("Relauncher");
            relauncher.start();
            launch(args);
        } catch (IOException e) {
            System.out.println("already running!");
            relaunch(args);
        }
    }

    private static void relaunch(String[] args) {
        try {
            Socket sock = new Socket("localhost", PORT);

            OutputStreamWriter out = new OutputStreamWriter(sock.getOutputStream());

            for (int i = 0; i < args.length; ++i) {
                out.write(args[i] + "\n");
                System.out.println("wrote: " + args[i]);
            }

            sock.close();
        } catch (Exception e) {
            System.out.println("ex: " + e);
            e.printStackTrace();
        }
    }

    static class RelanchWatcher implements Runnable {

        @Override
        public void run() {
            System.out.println("waiting for a connection");
            while (true) {
                try {
                    Socket sock = server.accept();

                    InputStreamReader in = new InputStreamReader(sock.getInputStream());
                    StringBuffer sb = new StringBuffer();
                    char[] buf = new char[256];
                    while (true) {
                        int n = in.read(buf);
                        if (n < 0) {
                            break;
                        }
                        sb.append(buf, 0, n);
                    }
                    String[] results = sb.toString().split("\\n");
                    otherMain(results);
                } catch (IOException e) {
                    System.out.println("ex: " + e);
                    e.printStackTrace();
                }
            }
        }

        private void otherMain(String[] results) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    System.out.println("show again");
                    LauncherApp.primaryStage.hide();
                    LauncherApp.primaryStage.show();
                    LauncherApp.primaryStage.sizeToScene();
                    LauncherApp.primaryStage.centerOnScreen();
                    LauncherApp.primaryStage.toFront();
                }
            });
        }
    }
}
