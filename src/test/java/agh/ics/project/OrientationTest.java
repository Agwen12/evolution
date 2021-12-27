package agh.ics.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrientationTest {

    @Test
    void rotateTest() {
        assertEquals(Orientation.NORTH, Orientation.NORTH_EAST.rotate(7));
        assertEquals(Orientation.NORTH, Orientation.EAST.rotate(6));
        assertEquals(Orientation.NORTH, Orientation.SOUTH_EAST.rotate(5));
        assertEquals(Orientation.NORTH, Orientation.SOUTH.rotate(4));
        assertEquals(Orientation.NORTH, Orientation.SOUTH_WEST.rotate(3));
        assertEquals(Orientation.NORTH, Orientation.WEST.rotate(2));
        assertEquals(Orientation.NORTH, Orientation.NORTH_WEST.rotate(1));
        assertEquals(Orientation.NORTH, Orientation.NORTH.rotate(0));
    }

}