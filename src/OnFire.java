import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
//TODO entity runs to nearest Obstacle and turn back into miner
public class OnFire extends Dude {
    public OnFire(String id, Point position, List<PImage> images, int actionPeriod,
                  int animationPeriod) {
        super(4, 0, actionPeriod, animationPeriod, id, position, images);
    }
    public void executeActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Obstacle.class)));

        moveToOnFire( world, (AnimationEntity) target.get(), scheduler, imageStore);

            scheduler.scheduleEvent( this,
                    createActivityAction( world, imageStore),
                    this.getActionPeriod());

    }

    public void transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
            Entity miner = Factory.createDudeFull(this.getId(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.getResourceLimit(),
                    imageStore.getImageList("dude"));

            world.removeEntity( this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((ActivityEntity)miner).scheduleActions( scheduler, world, imageStore);


    }

    public void moveToOnFire(
            WorldModel world,
            AnimationEntity target,
            EventScheduler scheduler, ImageStore imageStore)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            transformNotFull(world, scheduler, imageStore);
        }
        else {
            Point nextPos = this.nextPositionDude(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity( this, nextPos);
            }
        }
    }

}
