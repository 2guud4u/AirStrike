import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// TODO spawn y - 1 above clicked point and move straight in x direction. should go thru everything bc it is flying
// TODO fix the 2 entities collide thing by figuring out hoe to delete pic from spot and remove and put back entity
// TODO last resort we have plane path to gaol instead of straight line
public class Plane extends ActivityEntity {
    private Point goal;
    private List<Entity> removed = new ArrayList<>();
    private boolean remove_check;
    private List<Point> removedPos = new ArrayList<>();
    public Plane(String id, Point position, List<PImage> images, Point goal) {

        super(100, 100, id, position, images);
        this.goal = goal;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Point nextPos = nextPositionPlane();

        if (removed.size() > 0 && removedPos.get(0) != getPosition()) {
            Entity spawned= removed.remove(0);
            spawned.setPosition(removedPos.remove(0));
            world.addEntity(spawned);
            if (spawned instanceof ActivityEntity) {
                ((ActivityEntity) spawned).scheduleActions(scheduler, world, imageStore);
            }
        }

        spawnBomb(world, imageStore, scheduler);

        Optional<Entity> front = world.getOccupant(nextPos);
        if (front.isPresent()) {
            removed.add(world.getOccupancyCell(nextPos));
            world.removeEntity(front.get());
            if (removed.size() > 0) {
                removedPos.add(nextPos);
            }
        }

            world.moveEntity(this, nextPos);
            // replace entity

            // remove plane
            if (nextPos.x == -1) {
                world.removeEntityAt(getPosition());
            }
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    this.getActionPeriod());

    }

    public Point nextPositionPlane() {
        return new Point(getPosition().x - 1, getPosition().y);
    }

    public void spawnBomb(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        if(Functions.adjacent(getPosition(), goal)){
            Entity bomb = Factory.createBomb("1", new Point(goal.x, goal.y), imageStore.getImageList("bomb"));
            Optional<Entity> bombloc = world.getOccupant(goal);
            if (bombloc.isPresent()){
                world.removeEntity(bombloc.get());
            }
            world.addEntity(bomb);
            ((ActivityEntity)bomb).scheduleActions( scheduler, world, imageStore);
        }
    }
}
//