package agh.ics.project.GUI;

import agh.ics.project.Animal;
import agh.ics.project.EvolutionEngine;
import agh.ics.project.IEngineObserver;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class Tracker implements Trackable {

    private final TextField descendantsField;
    private final TextField childrenField;
    private final TextField deathTimeField;
    private final EvolutionEngine engine;
    private final GridPane grid;
    private final Animal animal;

    public Tracker(EvolutionEngine engine, Animal animal) {
        this.animal = animal;
        this.engine = engine;
        this.grid = new GridPane();

        childrenField = new TextField();
        descendantsField = new TextField();
        deathTimeField = new TextField("Still ALIVE");

        childrenField.setEditable(false);
        descendantsField.setEditable(false);
        deathTimeField.setEditable(false);


        grid.add(new Label("Children: "), 0, 0);
        grid.add(childrenField, 1, 0);

        grid.add(new Label("Descendants: "), 0, 1);
        grid.add(descendantsField, 1, 1);

        grid.add(new Label("Time of death: "), 0, 2);
        grid.add(deathTimeField, 1, 2);
        grid.setVgap(15.d);
    }

    public void show() {
        Stage trackerStage = new Stage();
        trackerStage.setAlwaysOnTop(true);
        Scene scene = new Scene(grid);
        trackerStage.setScene(scene);
        trackerStage.show();
    }

    private int sumChildren() {
        int sum = 0;
        for (Animal animal: this.animal.childrenList) {
            sum += animal.getChildren();
        }
        return sum;
    }

    public void killTracker() {
        this.animal.setTracker(null);
    }

    @Override
    public void push() {
        System.out.println(animal);
        System.out.println("TRAKER TRACKER TRACKER");
        childrenField.setText(Integer.toString(this.animal.getChildren()));
        descendantsField.setText(Integer.toString(sumChildren() + this.animal.getChildren()));
        if (animal.isDead()) deathTimeField.setText(Integer.toString(engine.getEpoch())) ;
    }
}
