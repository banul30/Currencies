package zad1;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class BrowserPane extends JFXPanel {

    public BrowserPane( String url) {

        Platform.runLater(new Runnable() {
            @Override public void run() {

                WebView view = new WebView();
                WebEngine engine = view.getEngine();
                engine.load(url);
                setScene(new Scene(view));
            }
        });
    }
}
