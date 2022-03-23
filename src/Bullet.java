import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Bullet extends ActivityEntity{

    private String direction;

    public Bullet( String id, Point position, List<PImage> images,
                  String direction) {
        super(100, 100, id, position, images);
        this.direction = direction;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        Optional<Entity> target;
        Point nextPoint;

        if (direction == "Left")
        {
            nextPoint = new Point(getPosition().x-1, getPosition().y);
        }
        else if (direction == "Up")
        {
            nextPoint = new Point(getPosition().x, getPosition().y-1);
        }
        else if (direction == "Right")
        {
            nextPoint = new Point(getPosition().x+1, getPosition().y);
        }
        else
        {
            nextPoint = new Point(getPosition().x, getPosition().y+1);
        }

        if (!world.withinBounds(nextPoint))
        {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
        }
        else
        {
            target = world.getOccupant(nextPoint);
            if (target.isPresent())
            {
                if (target.get() instanceof Dude)
                {

                    Entity corpse = Factory.createCorpse(target.get().getId(), nextPoint,
                            imageStore.getImageList(Functions.CORPSE_KEY));
                    world.removeEntity(this);
                    scheduler.unscheduleAllEvents(this);
                    world.removeEntity(target.get());
                    scheduler.unscheduleAllEvents(target.get());

                    world.addEntity(corpse);
                }
                else
                {
                    world.removeEntity(this);
                    scheduler.unscheduleAllEvents(this);
                }

            }
            else{
                world.moveEntity(this, nextPoint);
            }
        }

        scheduler.scheduleEvent( this,
                createActivityAction( world, imageStore),
                this.getActionPeriod());
    }
}
