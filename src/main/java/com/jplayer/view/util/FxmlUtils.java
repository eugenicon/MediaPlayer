package com.jplayer.view.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.kairos.components.MaterialButton;
import org.kairos.core.Activity;
import org.kairos.core.ActivityFactory;
import org.kairos.core.Fragment;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FxmlUtils {
    public static void setupScene(Activity activity){
        try {
            Class<?> activityClass = activity.getClass();
            activity.setContentView(
                    activityClass.getResource(
                            getResourcePath(activityClass)));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void setupScene(FXMLLoader fxmlLoader){
        Fragment controller = fxmlLoader.getController();
        if (controller.getState() <= 1 ) {
            Class<?> activityClass = controller.getClass();
            String resourcePath = getResourcePath(activityClass);
            URL resource = activityClass.getResource(resourcePath);
            try {
                InputStream inputStream = prepareFragmentXML(resource.openStream());
                if (inputStream != null){
                    fxmlLoader.load(inputStream);
                }else {
                    fxmlLoader.setLocation(resource);
                    fxmlLoader.load();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getResourcePath(Class<?> activityClass) {
        SceneContent sceneContent = activityClass.getAnnotation(SceneContent.class);
        String value = sceneContent.value();
        String fxmlExtension = ".fxml";
        if (!value.endsWith(fxmlExtension)){
            value += fxmlExtension;
        }
        return value;
    }

    private static InputStream prepareFragmentXML(InputStream inputStream) throws IOException {
        String content = new String(IOUtils.readFully(inputStream, Integer.MAX_VALUE, true));
        String fragmentRoot = "<fx:root xmlns:fx=\"http://javafx.com/fxml\" xmlns=\"http://javafx.com/javafx/8\" type=\"org.kairos.core.Fragment\">";
        if (!content.contains(fragmentRoot)) {
            StringBuilder sb = new StringBuilder(
                    content.replaceAll("xmlns:fx=\"http://javafx.com/fxml\"", ""));
            sb.insert(sb.lastIndexOf("?>")+3, fragmentRoot);
            sb.append("</fx:root>");
            return new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
        }
        return null;
    }

    public static void startActivity(Stage stage, Class<? extends Activity> activityClass){
        StackPane root = new StackPane(); // Create root pane
        stage.setScene(new Scene(root)); // Set the scene in the stage

        // this object represent the stack  of activities  in your application
        ActivityFactory factory=new ActivityFactory(stage);

        // set the material design style in your application
        stage.getScene().getStylesheets().add(MaterialButton.class.getResource("controls.css").toExternalForm());
        //stage.getScene().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        factory.startActivity(activityClass); // start the activity
    }

    public static void addAppToTray(Stage stage) {

        try {
            if (stage.getIcons().isEmpty()){
                return;
            }

            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            Image icon = stage.getIcons().get(0);
            java.awt.Image image = javafx.embed.swing.SwingFXUtils.fromFXImage(icon, null);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

            trayIcon.addActionListener(e -> Platform.runLater(() -> showStage(stage)));
            trayIcon.setToolTip(stage.getTitle());

            tray.add(trayIcon);

            stage.setOnCloseRequest(event -> tray.remove(trayIcon));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void showStage(Stage stage) {
        if (stage != null) {
            stage.show();
            stage.setIconified(false);
            stage.toFront();
        }
    }

}
