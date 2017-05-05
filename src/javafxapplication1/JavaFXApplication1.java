/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;


import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author nikitabatra
 */
public class JavaFXApplication1 extends Application {
    
//    static FXMLDocumentController myControllerHandle;
    @Override
    public void start(Stage stage) throws Exception {
        
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        Pane p = fxmlLoader.load(getClass().getResource("foo.fxml").openStream());
//        FXMLDocumentController dc = (FXMLDocumentController) fxmlLoader.getController();

        //FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
//        Parent root = loader.load();
//        myControllerHandle = (FXMLDocumentController)loader.getController();

        
        //Pane p = fxmlLoader.load(getClass().getResource("FXMLDocument.fxml").openStream());
        //FXMLDocumentController fooController = (FXMLDocumentController) fxmlLoader.getController();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
