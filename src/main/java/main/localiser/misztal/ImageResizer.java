package main.localiser.misztal;

/**
 * Created by Krzysztof on 14.02.2017.
 */

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImageResizer extends Application {
    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar=new MenuBar();
        Menu menuGame = new Menu("Game");
        MenuItem newGame = new MenuItem("New Game                F1");
        MenuItem exit = new MenuItem("Exit                            F2");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
            }
        });
        menuGame.getItems().addAll(newGame,new SeparatorMenuItem(),exit);
        menuBar.getMenus().addAll(menuGame);

        Image image = new Image("http://docs.oracle.com/javafx/"
                + "javafx/images/javafx-documentation.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        ImageViewPane viewPane = new ImageViewPane(imageView);

        VBox vbox=new VBox();
        StackPane root=new StackPane();
        root.getChildren().addAll(viewPane);

        vbox.getChildren().addAll(menuBar,root);
        VBox.setVgrow(root, Priority.ALWAYS);

        Scene scene= new Scene(vbox,200,200);
        primaryStage.setScene(scene);

        primaryStage.setMaxHeight(400);
        primaryStage.setMinHeight(200);
        primaryStage.setMaxWidth(500);
        primaryStage.setMinWidth(400);

        primaryStage.setTitle("Minesweeper");
        primaryStage.show();

    }
    public static void main(String[] args) { launch(); }
}





/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */


/**
 *
 * @author akouznet
 */
class ImageViewPane extends Region {

    private ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<ImageView>();

    public ObjectProperty<ImageView> imageViewProperty() {
        return imageViewProperty;
    }

    public ImageView getImageView() {
        return imageViewProperty.get();
    }

    public void setImageView(ImageView imageView) {
        this.imageViewProperty.set(imageView);
    }

    public ImageViewPane() {
        this(new ImageView());
    }

    @Override
    protected void layoutChildren() {
        ImageView imageView = imageViewProperty.get();
        if (imageView != null) {
            imageView.setFitWidth(getWidth());
            imageView.setFitHeight(getHeight());
            layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
        }
        super.layoutChildren();
    }

    public ImageViewPane(ImageView imageView) {
        imageViewProperty.addListener(new ChangeListener<ImageView>() {

            @Override
            public void changed(ObservableValue<? extends ImageView> arg0, ImageView oldIV, ImageView newIV) {
                if (oldIV != null) {
                    getChildren().remove(oldIV);
                }
                if (newIV != null) {
                    getChildren().add(newIV);
                }
            }
        });
        this.imageViewProperty.set(imageView);
    }
}