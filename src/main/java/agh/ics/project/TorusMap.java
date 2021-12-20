package agh.ics.project;

public class TorusMap extends AbstractMap {


    public TorusMap(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public Vector2d moveAnimal(Vector2d currPosition, Orientation orientation) {
        return new Vector2d(currPosition.x, currPosition.y).addModulo(orientation.toUnitVector(), this.width, this.height);
    }
}
