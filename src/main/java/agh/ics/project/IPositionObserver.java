package agh.ics.project;

public interface IPositionObserver {
    void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition);
}
