package agh.ics.project;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMap {
    protected int height;
    protected int width;
    protected float jungleRatio;
    protected Vector2d lowerJungleCorner;
    protected Vector2d upperJungleCorner;
    protected Map<Vector2d, AbstractOrganism> mapElements = new HashMap<>();

    /**
     * @param currPosition
     *          Animal current position
     * @param orientation
     *         Animal orientation
     *
     * @return position for animal on given currPosition specific map
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
}
