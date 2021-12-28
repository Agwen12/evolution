package agh.ics.project.GUI;

import agh.ics.project.AbstractOrganism;
import agh.ics.project.Animal;
import agh.ics.project.GUI.App;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoxElement {


    private final VBox vbox;

    public BoxElement(AbstractOrganism organism, double maxEnergy) {
        Rectangle rectangle = new Rectangle(12, 12);
        double x = (double) (organism.energy) / maxEnergy  <= 1 ? (double) (organism.energy) / maxEnergy: 1;

        if(organism instanceof Animal) {
            int red = (int) (255 * (1 - x));
            int green = (int) (255 * x);
            rectangle.setFill(Color.rgb(red, green, 0));

            rectangle.setOnMouseClicked(action -> {
                //TODO sledzenie zwierzaka
                System.out.println(organism);
                System.out.println(organism.isDead());
                System.out.println(((Animal) organism).getLifeTime());
            });
        } else {
            //Tak, trawa jest niebieska
            rectangle.setFill(Color.rgb(0,0,255));
        }
        vbox = new VBox();
        if (!organism.isDead()) vbox.getChildren().addAll(rectangle); //hackish but why not
    }


    public VBox getVbox() {
        return vbox;
    }
}
