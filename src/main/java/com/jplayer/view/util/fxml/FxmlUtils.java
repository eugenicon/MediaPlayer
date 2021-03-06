package com.jplayer.view.util.fxml;

import com.jplayer.view.util.ReflectionUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.kairos.components.MaterialButton;
import org.kairos.core.*;
import sun.misc.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FxmlUtils {
    public static void setupScene(Activity activity) {
        Class<?> activityClass = activity.getClass();
        URL resourcePath = activityClass.getResource(getResourcePath(activityClass));
        try {
            FXMLLoader loader = new FXMLLoader(resourcePath);
            loader.setController(activity);
            FxmlUtils.setupScene(loader);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load resource " + resourcePath, e);
        }
    }

    public static void setupScene(FXMLLoader fxmlLoader) {
        Object controller = fxmlLoader.getController();
        Class<?> activityClass = controller.getClass();
        String resourcePath = getResourcePath(activityClass);
        URL resource = activityClass.getResource(resourcePath);
        try {
            loadFxmlContent(fxmlLoader, resource, controller);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load resource " + resourcePath, e);
        }
    }

    private static void loadFxmlContent(FXMLLoader fxmlLoader, URL resource, Object controller) throws IOException {
        InputStream inputStream = prepareFragmentXML(resource.openStream(), controller);
        if (inputStream != null) {
            Node root = fxmlLoader.load(inputStream);

            if (controller instanceof Activity) {
                Context context = ReflectionUtils.getObjectField(controller, "context");
                SimpleWindowManager window = ReflectionUtils.getObjectField(context, "window");
                ObservableList<Node> windowPane = ReflectionUtils.getObjectField(window, "windowPane");
                assert windowPane != null;
                windowPane.add(root);
            }

        } else {
            fxmlLoader.setLocation(resource);
            fxmlLoader.load();
        }
    }

    private static String getResourcePath(Class<?> activityClass) {
        SceneContent sceneContent = activityClass.getAnnotation(SceneContent.class);
        String value = sceneContent.value();
        String fxmlExtension = ".fxml";
        if (!value.endsWith(fxmlExtension)) {
            value += fxmlExtension;
        }
        return value;
    }

    private static InputStream prepareFragmentXML(InputStream inputStream, Object controller) throws IOException {
        StringBuilder sb = getPreparedContent(inputStream, controller);
        //System.out.println(sb);
        return new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static StringBuilder getPreparedContent(InputStream inputStream, Object controller) throws IOException {
        String content = new String(IOUtils.readFully(inputStream, Integer.MAX_VALUE, true));
        StringBuilder sb = new StringBuilder(content);

        prepareFragmentFXML(controller, content, sb);

        sb = prepareIncludedFXML(controller, content, sb);

        return sb;
    }

    private static StringBuilder prepareIncludedFXML(Object controller, String content, StringBuilder sb) throws IOException {
        if (content.contains("fx:include")) {
            String patternString = "\\<fx:include source=(\".*\\.fxml\").*";

            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(sb);

            String include = "";
            int start = 0, end = 0;
            while (matcher.find()) {
                include = matcher.group();
                start = matcher.start();
                end = matcher.end();
            }

            pattern = Pattern.compile("\".*\"");
            matcher = pattern.matcher(include);

            String fileName = "";
            while (matcher.find()) {
                fileName = matcher.group().replace("\"", "");
            }

            URL resource = controller.getClass().getResource(fileName);
            StringBuilder preparedContent = getPreparedContent(resource.openStream(), controller);

            pattern = Pattern.compile("\\<\\?.*\\?\\>");
            matcher = pattern.matcher(preparedContent);

            StringBuilder imports = new StringBuilder();
            int importsStart = -1, importsEnd = 0;
            while (matcher.find()) {
                imports.append(matcher.group());
                if (importsStart < 0)
                    importsStart = matcher.start();
                importsEnd = matcher.end();
            }

            preparedContent = preparedContent.replace(importsStart, importsEnd, "");
            sb = sb.replace(start, end, preparedContent.toString());
            sb.insert(0, imports);
        }
        return sb;
    }

    private static StringBuilder prepareFragmentFXML(Object controller, String content, StringBuilder sb) {
        String fragmentRoot = "<fx:root xmlns:fx=\"http://javafx.com/fxml\" xmlns=\"http://javafx.com/javafx/8\" type=\"org.kairos.core.Fragment\">";

        if (controller instanceof Fragment && !content.contains(fragmentRoot)) {
            String completeRoot = "<?import javafx.scene.layout.VBox?>\n" +
                    "<?import javafx.scene.layout.AnchorPane?>\n" + fragmentRoot +
                    "\n<VBox AnchorPane.topAnchor=\"0\" AnchorPane.rightAnchor=\"0\"\n" +
                    " AnchorPane.bottomAnchor=\"0\" AnchorPane.leftAnchor=\"0\">";
            String completeTail = "\n</VBox>\n</fx:root>";
            sb.insert(sb.lastIndexOf("?>") + 3, completeRoot);
            sb.append(completeTail);
        }
        return sb;
    }

    public static void startActivity(Stage stage, Class<? extends Activity> activityClass) {
        StackPane root = new StackPane(); // Create root pane
        stage.setScene(new Scene(root)); // Set the scene in the stage

        // this object represent the stack  of activities  in your application
        ActivityFactory factory = new ActivityFactory(stage);

        // set the material design style in your application
        stage.getScene().getStylesheets().add(MaterialButton.class.getResource("controls.css").toExternalForm());
        stage.getScene().getStylesheets().add(activityClass.getResource("styles.css").toExternalForm());

        factory.startActivity(activityClass); // start the activity
    }

    public static void addAppToTray(Stage stage) {

        try {
            if (!stage.getIcons().isEmpty()) {
                java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
                Image icon = stage.getIcons().get(0);
                java.awt.Image image = javafx.embed.swing.SwingFXUtils.fromFXImage(icon, null);
                java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

                trayIcon.addActionListener(e -> Platform.runLater(() -> showStage(stage)));
                trayIcon.setToolTip(stage.getTitle());

                tray.add(trayIcon);

                stage.setOnCloseRequest(event -> tray.remove(trayIcon));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addIcon(Stage stage, String... icons) {
        for (String path : icons) {
            stage.getIcons().add(new Image(path));
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