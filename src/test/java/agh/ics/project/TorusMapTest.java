package agh.ics.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TorusMapTest {

    @Test
    void animalMoveTest() {
        TorusMap map = new TorusMap(10, 10, 0.2f);

        assertEquals(new Vector2d(10, 10), map.moveAnimal(new Vector2d(0, 0), Orientation.SOUTH_WEST));
        assertEquals(new Vector2d(0, 0), map.moveAnimal(new Vector2d(10, 10), Orientation.NORTH_EAST));
        assertEquals(new Vector2d(5, 10), map.moveAnimal(new Vector2d(5, 0), Orientation.SOUTH));
        assertEquals(new Vector2d(10, 6), map.moveAnimal(new Vector2d(0, 6), Orientation.WEST));
    }

}