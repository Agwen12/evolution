package agh.ics.project;

public class Grass extends AbstractOrganism {
    private final AbstractMap map;

    public Grass(AbstractMap map, Vector2d position) {
        this.map = map;
        this.position = position;
        this.energy = 1;
    }
}
