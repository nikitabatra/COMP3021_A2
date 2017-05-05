/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import World.World;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author nikitabatra
 */
public class SampleController implements Initializable {

    @FXML private ImageView hq1Flag;
    @FXML private ImageView hq2Flag;
    @FXML private Label displayTime;
    
    @FXML private Image image_hq1Flag = new Image("flag_blue.png");
    @FXML private Image image_hq2Flag = new Image("flag_red.png");
    
    @FXML
    private void handleStartGameA1(ActionEvent event) throws InterruptedException{ // When startgame is selected
        System.out.println("Game has been started!");
        displayTime.setText("Sup");
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("display_a1.fxml"));
        World w = new World();
        w.runGame();
        
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Reached Sample Controller! WHOOOOOOOOPSSSSSSS");
        hq1Flag.setImage(image_hq1Flag);
        hq2Flag.setImage(image_hq2Flag);
    }
}