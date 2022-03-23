import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Dude_Not_Full extends Dude{
    // TODO if walk on fire tile turn into OnFire
    public Dude_Not_Full(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {

        super( resourceLimit,  resourceCount, actionPeriod, animationPeriod,  id, position, images);


    }

    public void executeActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !moveToNotFull( world,
                (Plants) target.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent( this,
                    createActivityAction( world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Entity miner = Factory.createDudeFull(this.getId(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.getResourceLimit(),
                    this.getImages());

            world.removeEntity( this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((ActivityEntity)miner).scheduleActions( scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean moveToNotFull(
            WorldModel world,
            Plants target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            this.setResourceCount(getResourceCount() + 1);
            (target).setHealth((target).getHealth() - 1);
            return true;
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
            return false;
        }
    }


}
