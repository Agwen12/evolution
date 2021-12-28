package agh.ics.project;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMap implements IPositionObserver {
    protected int height;
    protected int width;
    protected float jungleRatio;
    protected Vector2d lowerJungleCorner;
    protected Vector2d upperJungleCorner;
    protected Map<Vector2d, MapCell> mapElements = new HashMap<>();

    /**
     * @param currPosition
     *          Animal current position
     * @param orientation
     *         Animal orientation
     *
     * @return position for animal on given position specific map
     */
    public abstract Vector2d moveAnimal(Vector2d currPosition, Orientation orientation);

    //TODO dont allow too big or too small jungle
    protected void placeJungle() {
        Vector2d center = new Vector2d(this.width / 2, this.height / 2);
        int jungleWidth = (int) (this.width * jungleRatio);
        int jungleHeight = (int) (this.height * jungleRatio);
        this.lowerJungleCorner = new Vector2d(center.x - jungleWidth / 2, center.y - jungleHeight / 2);
        this.upperJungleCorner = new Vector2d(center.x + jungleWidth / 2, center.y + jungleHeight / 2);
    }

    // adds element to a cell in map
    public void placeObject(AbstractOrganism organism) {
        if(this.mapElements.containsKey(organism.getPosition())) this.mapElements.get(organism.getPosition()).placeElement(organism);
        else this.mapElements.put(organism.getPosition(), new MapCell(organism));
    }
    protected void placeObject(AbstractOrganism organism, Vector2d position) {
        if(mapElements.containsKey(position)) mapElements.get(position).placeElement(organism);
        else mapElements.put(position, new MapCell(organism));
    }

    protected void removeObject(AbstractOrganism organism, Vector2d position) {
        mapElements.get(position).removeElement(organism);
    }

    public void removeObject(AbstractOrganism organism) {
        if(this.mapElements.containsKey(organism.getPosition())) this.mapElements.get(organism.position).removeElement(organism);
    }

    public MapCell getMapCellAt(Vector2d position) {
        return this.mapElements.get(position);
    }


    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        this.removeObject(animal, oldPosition);
        this.placeObject(animal, newPosition);
        System.out.println("d");
    }

    @Override
    public String toString() {
        return this.mapElements.values().toString();
    }
}
