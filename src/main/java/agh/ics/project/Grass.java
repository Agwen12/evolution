package agh.ics.project;

public class Grass extends AbstractOrganism {

    public Grass(Vector2d position) {
        this.position = position;
        this.energy = 1;
    }

    @Override
    public String toString() {
        return "G{"+ position +
                '}';
    }
}
