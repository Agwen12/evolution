package agh.ics.project;

import java.util.Random;

public class World {

    public static void main(String[] args) {
        Random r = new Random();
        System.err.println(r.nextInt(8));

        AbstractMap map = new TorusMap(10, 10);
        System.out.println(map.moveAnimal(new Vector2d(10, 10), Orientation.NORTH));

        byte a = 1;
        System.out.println(a);
    }
}
