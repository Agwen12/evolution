package agh.ics.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapCellTest {

    @Test
    void getBiggestEnergyTest() {
        AbstractMap map = new TorusMap(10, 10, 0.2f);
        Animal a1 = new Animal(map, new Vector2d(1,1), 10);
        Animal a2 = new Animal(map, new Vector2d(1,3), 11);
        Animal a3 = new Animal(map, new Vector2d(1,2), 12);
        Animal a4 = new Animal(map, new Vector2d(1,2), 12);
        Animal a5 = new Animal(map, new Vector2d(0, 0), 100);
        Grass g1 = new Grass(new Vector2d(1,1));
        MapCell mapCell = new MapCell();
        mapCell.placeElement(a1);
        mapCell.placeElement(a2);
        mapCell.placeElement(a3);
        mapCell.placeElement(a4);
        mapCell.placeElement(g1);
        assertEquals(a4, mapCell.getBiggestEnergy());
        mapCell.placeElement(a5);
        assertEquals(a5, mapCell.getBiggestEnergy());
    }

    @Test
    void getSecondTest() {
        AbstractMap map = new TorusMap(10, 10, 0.2f);
        Animal a1 = new Animal(map, new Vector2d(1,1), 10);
        Animal a2 = new Animal(map, new Vector2d(1,3), 11);
        Animal a3 = new Animal(map, new Vector2d(1,2), 12);
        Animal a4 = new Animal(map, new Vector2d(1,2), 12);
        Animal a5 = new Animal(map, new Vector2d(0, 0), 100);
        Grass g1 = new Grass(new Vector2d(1,1));
        MapCell mapCell = new MapCell();
        mapCell.placeElement(a1);
        mapCell.placeElement(a2);
        mapCell.placeElement(a3);
        mapCell.placeElement(a4);
        mapCell.placeElement(g1);
        assertEquals(a3, mapCell.getSecond());
        mapCell.placeElement(a5);
        assertEquals(a4, mapCell.getSecond());
    }

    @Test
    void removeElementTest() {
        AbstractMap map = new TorusMap(10, 10, 0.2f);
        Animal a1 = new Animal(map, new Vector2d(1,1), 10);
        Animal a2 = new Animal(map, new Vector2d(1,3), 11);
        Animal a3 = new Animal(map, new Vector2d(1,2), 12);
        Animal a4 = new Animal(map, new Vector2d(1,2), 12);
        Animal a5 = new Animal(map, new Vector2d(0, 0), 100);
        Grass g1 = new Grass(new Vector2d(1,1));
        map.placeObject(a1);
        map.placeObject(a2);
        map.placeObject(a3);
        map.placeObject(a4);
        map.placeObject(a5);
        map.placeObject(g1);

        map.removeObject(a5, new Vector2d(1,2));
        assertEquals(a4, map.getMapCellAt(new Vector2d(1, 2)).getBiggestEnergy());
        map.removeObject(a4);
        assertEquals(a3, map.getMapCellAt(new Vector2d(1, 2)).getBiggestEnergy());

//        assertEquals(a3, mapCell.getBiggestEnergy());
    }

}