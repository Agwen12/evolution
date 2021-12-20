package agh.ics.project;

import java.util.Random;


public class Animal extends AbstractOrganism {
    private int energy;
    private Orientation orientation;
    private final AbstractMap map;


    //TODO todo ToDo
    public Animal(AbstractMap map, Vector2d position, int energy) {
        this.position = position;
        this.energy = energy;
        this.map = map;
        Random rand = new Random();
        this.orientation = Orientation.values()[rand.nextInt(8)]; // #losowa orientacja
    }
}
