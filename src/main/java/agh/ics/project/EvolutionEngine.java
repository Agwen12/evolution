package agh.ics.project;

import java.util.*;
import java.util.stream.Collectors;

public class EvolutionEngine implements Runnable {

    //TODO TRAWA LIST?
    private List<Animal> engineAnimal = new LinkedList<>();
    private final AbstractMap map;
    private final Random rand = new Random();

    public EvolutionEngine(AbstractMap map, int numberOfAnimals, int startEnergy) {
        this.map = map;
        for (int i = 0; i < numberOfAnimals; i++) {
            Animal a = new Animal(map, new Vector2d(rand.nextInt(map.width), rand.nextInt(map.height)), startEnergy);
            map.placeObject(a);
            engineAnimal.add(a);
        }

        //TODO remove shite from tests
        Animal a1 = new Animal(map, new Vector2d(1,1), 10);
        Animal a2 = new Animal(map, new Vector2d(1,1), 11);
        map.placeObject(a1);
        map.placeObject(a2);
        engineAnimal.add(a1);
        engineAnimal.add(a2);
    }

    private void moveAnimals() {
        //TODO only 10 epochs for now
        for (int i = 0; i < 10; i++) {
            this.engineAnimal.forEach(Animal::move);
            System.out.println(map.toString());
        }
    }

    private void removeDeadAnimals() {
        this.engineAnimal = engineAnimal.stream()
                .map(this::cutDead)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Animal cutDead(Animal animal) {
        if (!animal.isDead()) {
            return animal;
        } else {
            this.map.removeObject(animal, animal.getPosition());
            return null;
        }
    }

    //TODO splitting grass when there's >1 biggest energies
    private void eatGrass() {
        for (Vector2d vector: map.mapElements.keySet()) {
            MapCell cell = map.getMapCellAt(vector);
            if (cell.hasGrass() && cell.getBiggestEnergy() != null) {
                map.removeObject(cell.getGrass());
                cell.getBiggestEnergy().eat();
            }
        }
    }

    private void makeSweetLove() {
        for (Vector2d vector: map.mapElements.keySet())  {
            MapCell cell = map.getMapCellAt(vector);
            Animal first = cell.getBiggestEnergy();
            Animal second = cell.getSecond();

            if (second != null && first != null && second.energy >= 50 / 2) {
                //TODO maybe change it later, 50 should be @param starEnergy
                first.fuck();
                second.fuck();
                Animal newAnimal = new Animal(map, first, second);
                map.placeObject(newAnimal);
                engineAnimal.add(newAnimal);
            }
        }
    }

    //TODO placing new grass
    private void plantGrass() {
        int counter = 0;
        while (counter == 0) {
            Vector2d v = new Vector2d(rand.nextInt(map.upperJungleCorner.x - map.lowerJungleCorner.x) + map.lowerJungleCorner.x,
                    rand.nextInt(map.upperJungleCorner.y - map.lowerJungleCorner.y) + map.lowerJungleCorner.y);
            if (!map.getMapCellAt(v).hasGrass()) {
                map.placeObject(new Grass(map, v), v);
                counter = 1;
            }
        }
        while (counter == 1) {

        }
    }

    private Vector2d randomVectorNotInRec()

    @Override
    public void run() {

    }
}
