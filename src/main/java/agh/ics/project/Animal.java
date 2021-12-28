package agh.ics.project;

import agh.ics.project.GUI.Trackable;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Animal extends AbstractOrganism implements IPositionObserver, Trackable {
    private final Random rand = new Random();
    public Orientation orientation;
    private final AbstractMap map;
    public final Genotype genotype;
    private int lifeTime = 0;
    private int children = 0;
    private Trackable tracker;
    public List<Animal> childrenList = new LinkedList<>();

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

    public Animal(AbstractMap map, Animal animal, Vector2d position, int energy) {
        this.map = map;
        this.position = position;
        this.energy = energy;
        this.orientation = animal.orientation;
        this.genotype = animal.genotype.clone();
    }


    public void move() {
        if(!this.isDead()) {
            this.lifeTime += 1;
            int action = this.genotype.getAction();
            this.orientation = this.orientation.rotate(action);
            if (action == 0 || action == 4) {
                Vector2d newPosition = this.map.moveAnimal(this.position, this.orientation);
                this.positionChanged(this, this.position, newPosition);
                this.position = newPosition;
            }
            loosEnergy();
            push(this);
        }
    }

    public int getLifeTime() {
        return this.lifeTime;
    }
    public int getChildren() {
        return this.children;
    }
    public Trackable getTracker() { return tracker; }
    public void setTracker(Trackable tracker) {
        this.tracker = tracker;
        this.childrenList.clear();
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
        if (!position.equals(animal.position)) return false;
        return genotype.equals(animal.genotype);
    }

    private void loosEnergy() {
        this.energy = this.energy -  1;
    }

    //TODO tak
    public int fuck(Animal child, boolean magic) {
        if (this.tracker != null) this.childrenList.add(child);
        this.energy = magic ? this.energy : 3 * this.energy / 4;
        this.children += 1;
        return this.energy / 4;
    }

    public void eat(int eatGain) {
        energy += eatGain;
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

    @Override
    public void push(Animal animal) {
        if(this.tracker != null) this.tracker.push(animal);
    }
}
