import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Tree extends Plants {

    public Tree(int health, int healthLimit, int actionPeriod, int animationPeriod, String id, Point position, List<PImage> images) {
        super(health, healthLimit, actionPeriod, animationPeriod, id, position, images);
    }




    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!transformPlant( world, scheduler, imageStore)) {

            scheduler.scheduleEvent( this,
                    createActivityAction( world, imageStore),
                    this.getActionPeriod());
        }
    }

    public boolean transformPlant(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
            return this.transformTree( world, scheduler, imageStore);
    }

    public boolean transformTree(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getHealth() <= 0) {
            Entity stump = Factory.createStump(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

        }

        return false;
    }


}
