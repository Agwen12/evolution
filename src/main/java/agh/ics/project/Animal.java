package agh.ics.project;

import java.util.Random;


public class Animal extends AbstractOrganism implements IPositionObserver {
    private final Random rand = new Random();
    public Orientation orientation;
    private final AbstractMap map;
    public Genotype genotype;

    public Animal(AbstractMap map, Vector2d position, int energy) {
        this.position = position;
        this.energy = energy;
        this.map = map;
        this.orientation = Orientation.values()[rand.nextInt(8)]; // #losowa orientacja
        this.genotype = new Genotype();
    }

    public Animal(AbstractMap map, Animal parent1, Animal parent2) {
        this.position = parent1.position;
        this.energy = (int) (parent1.energy / 4 + parent2.energy / 4);
        this.map = map;
        double energyRation = (parent1.energy * 1.d) / ((parent1.energy + parent2.energy) * 1.d);
        if (rand.nextBoolean()) {
            this.genotype = new Genotype(parent1.genotype.getLeftGenes(energyRation), parent2.genotype.getRightGenes(energyRation));
        } else
            this.genotype = new Genotype(parent1.genotype.getRightGenes(energyRation), parent2.genotype.getLeftGenes(energyRation));
        this.orientation = Orientation.values()[rand.nextInt(8)]; // #losowa orientacja
    }


    public void move() {
        int action = this.genotype.getAction();
        this.orientation = this.orientation.rotate(action);
        if (action == 0 || action == 4) {
            Vector2d newPosition = this.map.moveAnimal(this.position, this.orientation);
             this.positionChanged(this, this.position, newPosition);
             this.position = newPosition;
        }

        loosEnergy();

    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        this.map.positionChanged(this, oldPosition, newPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal)) return false;

        Animal animal = (Animal) o;
        if (energy != animal.energy) return false;
        if (orientation != animal.orientation) return false;
        if (!position.equals(animal.position)) return false;
        return genotype.equals(animal.genotype);
    }

    private void loosEnergy() {
        this.energy -= 1;
    }

    public int fuck() {
        this.energy = 3 * this.energy / 4;
        return this.energy / 4;
    }

    public void eat() {
        energy += 10; //TODO this should be a param
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + genotype.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return
                "{ " + orientation +
                ", " + position +
                ", " + energy + " }";
    }


}
