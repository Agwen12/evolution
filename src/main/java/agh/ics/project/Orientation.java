package agh.ics.project;

public enum Orientation {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public Orientation rotate(int degree) {
        return Orientation.values()[(this.ordinal() + degree) % 8];
    }

    public Vector2d toUnitVector() {
        switch (this) {

            case WEST -> {
                return new Vector2d(-1, 0);
            }
            case NORTH -> {
                return new Vector2d(0, 1);
            }
            case NORTH_EAST -> {
                return new Vector2d(1, 1);
            }
            case EAST -> {
                return new Vector2d(1, 0);
            }
            case SOUTH_EAST -> {
                return new Vector2d(1, -1);
            }
            case SOUTH -> {
                return new Vector2d(0, -1);
            }
            case SOUTH_WEST -> {
                return new Vector2d(-1, -1);
            }
            default -> {
                return new Vector2d(-1, 1);
            }
        }
    }
}
