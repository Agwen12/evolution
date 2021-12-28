package agh.ics.project.GUI;

import agh.ics.project.AbstractOrganism;
import agh.ics.project.Animal;
import agh.ics.project.EvolutionEngine;
import agh.ics.project.GUI.App;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class BoxElement {


    private final VBox vbox;
    private final EvolutionEngine engine;
    public BoxElement(AbstractOrganism organism, double maxEnergy, EvolutionEngine engine) {
        Rectangle rectangle = new Rectangle(12, 12);
        this.engine = engine;
        double x = (double) (organism.energy) / engine.getStartEnergy()  <= 1 ? (double) (organism.energy) / engine.getStartEnergy(): 1;

        if(organism instanceof Animal) {
            int red = (int) (255 * (1 - x));
            int green = (int) (255 * x);
            rectangle.setFill(Color.rgb(red, green, 0));

            rectangle.setOnMouseClicked(action -> {
                //TODO sledzenie zwierzaka
                if (engine.isPaused()) {
                    showInfo((Animal) organism);
                }

            });
        } else {
            //Tak, trawa jest niebieska
            rectangle.setFill(Color.rgb(0,0,255));
        }
        vbox = new VBox();
        if (!organism.isDead()) vbox.getChildren().addAll(rectangle); //hackish but why not
    }

    private void showInfo(Animal animal) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Animal INFO");
        alert.getDialogPane().setMinSize(500, 150);
        alert.setHeaderText(animal.toString());
        alert.setContentText(animal.genotype.toString());

        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType trackButton = new ButtonType("Track");
        alert.getButtonTypes().setAll(trackButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (trackButton.equals(result.get())) {
            Tracker tracker = new Tracker(this.engine, animal);
            animal.setTracker(tracker);
            tracker.show();
        } else {

        }
    }

    public VBox getVbox() {
        return vbox;
    }
}
