package agh.ics.project;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class World {

    public static void main(String[] args) {
//        Random r = new Random();
//        System.err.println(r.nextInt(8));
//
//        AbstractMap map = new TorusMap(10, 10);
//        System.out.println(map.moveAnimal(new Vector2d(10, 10), Orientation.NORTH));
//
//        byte a = 1;
//        System.out.println(a);

//        Genotype t = new Genotype();
//        Genotype u = new Genotype();
//        System.out.println(t);
//        System.out.println(u);
//        double ratio = 150.d / 200.d;
//
//        List<Integer> p1 = t.getLeftGenes(ratio);
//        List<Integer> p2 = u.getRightGenes(ratio);
//        System.out.println(p1);
//        System.out.println(p2);
//
//        List<Integer> p3 = u.getLeftGenes(ratio);
//        List<Integer> p4 = t.getRightGenes(ratio);
//        System.out.println(p3);
//        System.out.println(p4);
        AbstractMap map = new TorusMap(40, 40, 0.2f);
        EvolutionEngine engine = new EvolutionEngine(map, 6, 10);
        engine.run();
    }
}
