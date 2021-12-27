package agh.ics.project;

import java.util.*;

public class MapCell {
    private final SortedSet<AbstractOrganism> cellSet = new TreeSet<>(((o1, o2) -> {
        if (o1.equals(o2)) return 0;
        if(o1 instanceof Grass) return -1;
        if(o1.energy > o2.energy) return 1;
        else if (o1.energy < o2.energy) return -1;
        else {
            if (o1.position.x > o2.position.x) return 1;
            else if (o1.position.x < o2.position.x) return -1;
        }
        return 1;
    }));
    private Vector2d cellPosition;

    public MapCell(AbstractOrganism organism) {
        cellSet.add(organism);
        cellPosition = organism.position;
    }

    public MapCell(){};


    public void placeElement(AbstractOrganism organism) {
        this.cellSet.add(organism);
    }

    public Animal getBiggestEnergy() {
        AbstractOrganism first = this.cellSet.last();
        if (first instanceof Animal) return (Animal) first;
        return null;
    }

    public Animal getSecond() {
        AbstractOrganism biggest = getBiggestEnergy();
        this.cellSet.remove(biggest);
        AbstractOrganism sc = this.cellSet.last();
        this.cellSet.add(biggest);
        if (sc instanceof Animal) return (Animal) sc;
        else return null;
    }

    public List<AbstractOrganism> getContents() {
        return new ArrayList<>(this.cellSet);
    }

    public boolean hasGrass() {
        return this.cellSet.first() instanceof Grass;
    }

    public AbstractOrganism getGrass() {
        return this.cellSet.first();
    }

    public boolean removeElement(AbstractOrganism organism) {
        return this.cellSet.remove(organism);
    }

    @Override
    public String toString() {
        return !cellSet.isEmpty() ? cellSet.toString(): "";
    }
}
