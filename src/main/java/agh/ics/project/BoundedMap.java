package agh.ics.project;

public class BoundedMap extends AbstractMap {


    @Override
    public Vector2d moveAnimal(Vector2d currPosition, Orientation orientation) {
        Vector2d temp = new Vector2d(currPosition.x, currPosition.y).add(orientation.toUnitVector());
        if (temp.x <= this.width && temp.x >= 0 && temp.y <= this.height && temp.y >= 0) {
            return temp;
        }
        return currPosition;
    }
}
