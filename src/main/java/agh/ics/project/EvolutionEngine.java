package agh.ics.project;

import javafx.scene.chart.LineChart;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EvolutionEngine implements Runnable, IEngineObserver {

    private final int moveCost;
    private final int eatingGain;
    private final int startEnergy;
    private int magicLeft = 3;
    private boolean magicReproduction;
    private int epoch = 0;
    private List<Animal> engineAnimal = new LinkedList<>();
    private final List<Grass> engineGrass = new LinkedList<>();
    private final AbstractMap map;
    private final Random rand = new Random();
    private final List<IEngineObserver> observers = new LinkedList<>();
    private final List<Integer> deathTime = new LinkedList<>();

    public EvolutionEngine(AbstractMap map, int numberOfAnimals, int startEnergy) {
        this.map = map;
        this.startEnergy = startEnergy;
        this.moveCost = 1;
        this.eatingGain = 3;
        for (int i = 0; i < numberOfAnimals; i++) {
            Animal a = new Animal(map, new Vector2d(rand.nextInt(map.width), rand.nextInt(map.height)), startEnergy);
            map.placeObject(a);
            engineAnimal.add(a);
        }
    }

    public EvolutionEngine(AbstractMap map, int numberOfAnimals, int startEnergy, boolean magicReproduction,
                           int eatingGain, int moveCost) {
        this.map = map;
        this.eatingGain = eatingGain;
        this.startEnergy = startEnergy;
        this.moveCost = moveCost;
        this.magicReproduction = magicReproduction;
        for (int i = 0; i < numberOfAnimals; i++) {
            Animal a = new Animal(map, new Vector2d(rand.nextInt(map.width), rand.nextInt(map.height)), startEnergy);
            map.placeObject(a);
            engineAnimal.add(a);
        }
    }


    private void moveAnimals() {
            this.engineAnimal.forEach(Animal::move);
    }

    private void removeDeadAnimals() {
        this.engineAnimal = engineAnimal.stream()
                .map(this::cutDead)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Animal cutDead(Animal animal) {
        if (animal.isDead()) {
            this.deathTime.add(animal.getLifeTime());
            this.map.removeObject(animal, animal.getPosition());
            return null;
        }
        return animal;
    }

    //TODO splitting grass when there's >1 biggest energies
    private void eatGrass() {
        for (Vector2d vector: map.mapElements.keySet()) {
            MapCell cell = map.getMapCellAt(vector);
            if (cell.hasGrass() && cell.getBiggestEnergy() != null) {
                Grass grass = cell.getGrass();
                engineGrass.remove(grass);
                map.removeObject(grass, vector);
                cell.getBiggestEnergy().eat(this.eatingGain);
            }
        }
    }

    private void makeBasicLove() {
        for (Vector2d vector: map.mapElements.keySet()) {
            MapCell cell = map.getMapCellAt(vector);
            if (cell.animalSize() >= 2) {
                Animal first = cell.getBiggestEnergy();
                Animal second = cell.getSecond();

                if (second != null && first != null && second.energy >= 50 / 2) {
                    //TODO maybe change it later, 50 should be @param starEnergy
                    first.fuck(this.magicReproduction);
                    second.fuck(this.magicReproduction);
                    Animal newAnimal = new Animal(map, first, second);
                    map.placeObject(newAnimal);
                    engineAnimal.add(newAnimal);
                }
            }
        }
    }

    private void makeMagicLove() {
        if (this.magicLeft > 0 && rand.nextInt(10) > 6) {
            List<List<Vector2d>> free2D = getFreePositions();
            List<Vector2d> freePositions = new LinkedList<>(free2D.get(0));
            freePositions.addAll(free2D.get(1));
            List<Animal> tempAnimals = new LinkedList<>();
            for (Animal animal: engineAnimal) {
                int index = rand.nextInt(freePositions.size());
                Vector2d randomFreePosition = freePositions.get(index);
                freePositions.remove(index);
                Animal newAnimal = new Animal(map, animal, randomFreePosition, startEnergy);
                tempAnimals.add(newAnimal);
                map.placeObject(newAnimal, randomFreePosition);
            }
            this.engineAnimal.addAll(tempAnimals);
            this.magicLeft -= 1;
        }
    }

    private void makeSweetLove() {
        if (magicReproduction) makeMagicLove();
        else makeBasicLove();
    }

    private void plantGrass() {
        List<List<Vector2d>> freePositions = getFreePositions();
        List<Vector2d> jungle = freePositions.get(0);
        List<Vector2d> savana = freePositions.get(1);
        if (jungle.size() > 0) {
            Vector2d jungleVec = jungle.get(rand.nextInt(jungle.size()));
            Grass grass = new Grass(map, jungleVec);
            map.placeObject(grass, jungleVec);
            this.engineGrass.add(grass);
        }
        if (savana.size() > 0) {
            Vector2d savanaVec = savana.get(rand.nextInt(savana.size()));
            Grass grass = new Grass(map, savanaVec);
            map.placeObject(grass, savanaVec);
            this.engineGrass.add(grass);
        }
    }

    // 0 -> in jungle; 1 -> not in jungle
    private List<List<Vector2d>> getFreePositions() {
        List<List<Vector2d>> positions = new ArrayList<>();
        positions.add(new LinkedList<>());
        positions.add(new LinkedList<>());
        for (int i = 0; i < map.height + 1; i++) {
            for (int j = 0; j < map.width + 1; j++) {
                Vector2d vec = new Vector2d(i, j);
                if ((vec.follows(map.lowerJungleCorner) && vec.precedes(map.upperJungleCorner)) &&
                        map.isPositionFree(vec)) {
                    positions.get(0).add(vec);
                } else {
                    positions.get(1).add(vec);
                }
            }
        }
        return positions;
    }

    @Override
    public void run() {
        while (true) {
            removeDeadAnimals();
            moveAnimals();
            eatGrass();
            makeSweetLove();
            plantGrass();
            makeMoves(this.map instanceof TorusMap, this.getStats());
            System.out.println(epoch);
            this.epoch += 1;
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void addEngineObserver(IEngineObserver observer) {
        observers.add(observer);
    }

    public int getAnimalCounter() {
        return engineAnimal.size();
    }

    public int getEpoch() {
        return epoch;
    }

    private int getGrassCounter() {
        return this.engineGrass.size();
    }

    private Map<String, Double> getStats() {
        double lifTimeSum = 0d;
        double energySum = 0d;
        double childrenSum = 0d;
        for(Animal animal: engineAnimal) {
            energySum += animal.energy;
            childrenSum += animal.getChildren();
        }
        for(Integer i: deathTime) {
            lifTimeSum += i;
        }

        Map<String, Double> stats = new HashMap<>();
        double animalNumber = getAnimalCounter();
        stats.put("lifeTime", deathTime.size() > 0 ? lifTimeSum / deathTime.size() : 0);
        stats.put("energy", animalNumber > 0 ? energySum / animalNumber : 0);
        stats.put("children", animalNumber > 0 ? childrenSum / animalNumber : 0);
        stats.put("animalNumber", animalNumber);
        stats.put("grassNumber", (double) getGrassCounter());
        stats.put("epoch", (double) getEpoch());
        return stats;
    }

    @Override
    public void makeMoves(boolean isTorus, Map<String, Double> stats) {
        observers.forEach(observer -> observer.makeMoves(isTorus, stats));
    }
}
