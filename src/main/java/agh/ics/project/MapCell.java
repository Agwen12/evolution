package agh.ics.project;

import java.util.*;

public class MapCell {
    private final SortedSet<AbstractOrganism> cellSet = new TreeSet<>(((o1, o2) -> {
        if(o1 instanceof Grass) return -1;
        if (o1.equals(o2)) return 0;
        if(o1.energy > o2.energy) return 1;
        else if (o1.energy < o2.energy) return -1;
        else {
            if (o1.position.x > o2.position.x) return 1;
            else if (o1.position.x < o2.position.x) return -1;
        }
        return 1;
    }));


    public MapCell(AbstractOrganism organism) {
        cellSet.add(organism);
    }

    public MapCell(){};


    public void placeElement(AbstractOrganism organism) {
        this.cellSet.add(organism);
    }

    public Animal getBiggestEnergy() {
        if (cellSet.isEmpty()) return null;
        AbstractOrganism first = this.cellSet.last();
        if (first instanceof Animal) return (Animal) first;
        return null;
    }

    public Animal getSecond() {

        if (cellSet.size() > 1) {
            AbstractOrganism[] array = cellSet.toArray(AbstractOrganism[]::new);
            if (array[array.length - 2] instanceof Animal) return (Animal) array[array.length - 2];
        }
        return null;
    }

    public List<AbstractOrganism> getContents() {
        return new ArrayList<>(this.cellSet);
    }

    public boolean hasGrass() {
        return !cellSet.isEmpty() && this.cellSet.first() instanceof Grass;
    }

    public Grass getGrass() {
        if (this.hasGrass()) return (Grass) this.cellSet.first();
        return null;
    }

    public boolean removeElement(AbstractOrganism organism) {
        return this.cellSet.remove(organism);
    }

    public boolean isEmpty() {
        return this.cellSet.isEmpty();
    }

    public int animalSize() {
        return this.hasGrass() ? cellSet.size() - 1: cellSet.size();
    }

    @Override
    public String toString() {
        return !cellSet.isEmpty() ? cellSet.toString(): "";
    }
}
