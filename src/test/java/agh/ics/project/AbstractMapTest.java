package agh.ics.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractMapTest {


    @Test
    void removeDeadTest() {
        AbstractMap map = new TorusMap(10, 10, 0.2f);
        EvolutionEngine engine = new EvolutionEngine(map, 4, 0);
//        engine.
    }

}