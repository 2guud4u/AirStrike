import processing.core.PImage;

import java.util.List;

public abstract class Dude extends ActivityEntity{

    private final int resourceLimit;
    private int resourceCount;

    public Dude(int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod, String id, Point position, List<PImage> images) {
        super(actionPeriod, animationPeriod, id, position, images);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;

    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    public Point nextPositionDude(
            WorldModel world, Point destPos) {
        List<Point> points = getStrategy().computePath(getPosition(), destPos, s -> !(world.isOccupied(s)) || world.withinBounds(s) && (world.getOccupancyCell(s) instanceof Stump),
                (p1, p2) -> Functions.adjacent(p1,p2), PathingStrategy.CARDINAL_NEIGHBORS );
        if (points.size() == 0)
        {
            return getPosition();
        }
        return points.get(0);
    }
//    public Point nextPositionDude(
//            WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos) instanceof Stump) {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
//    }

    public void transformBlownUp(WorldModel world,
                                 EventScheduler scheduler,
                                 ImageStore imageStore) {
        Entity onfire = Factory.createOnFire(this.getId(), this.getPosition(),
                1000, 125, 4, imageStore.getImageList("onfire"));

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(onfire);
        ((ActivityEntity)onfire).scheduleActions( scheduler, world, imageStore);
    }

}
