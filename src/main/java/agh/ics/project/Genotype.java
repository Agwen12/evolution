package agh.ics.project;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class Genotype {

    final private List<Integer> genes;

    public Genotype() {
        Random rand = new Random();
        this.genes = rand.ints(32,0,8).boxed().collect(toList());
        Collections.sort(this.genes);
    }

    public Genotype(List<Integer> left, List<Integer> right) {
        this.genes = new ArrayList<>(left);
        this.genes.addAll(right);
        Collections.sort(this.genes);
    }

    public Genotype(List<Integer> list) {
        this.genes = new ArrayList<>(list);
        Collections.sort(this.genes);
    }

    public List<Integer> getLeftGenes(double percent) {
        return this.genes.subList(0, (int) (percent * 32));
    }

    public List<Integer> getRightGenes(double percent) {
        return this.genes.subList((int) (percent * 32), 32);
    }

    public Integer getAction(){
        return this.genes.get(new Random().nextInt(32));
    }

    public Genotype clone() {
        return new Genotype(this.genes);
    }


    @Override
    public String toString() {
        return genes.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genotype)) return false;
        Genotype genotype = (Genotype) o;
        return genes != null ? genes.equals(genotype.genes) : genotype.genes == null;
    }

    @Override
    public int hashCode() {
        return genes != null ? genes.hashCode() : 0;
    }
}
