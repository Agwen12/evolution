package agh.ics.project;

import java.util.AbstractMap;

public class AbstractOrganism {

    protected Vector2d position;
    public int energy;

    public Vector2d getPosition() {
        return this.position;
    }

    public boolean isDead() {
        return this.energy < 1;
    }
}
