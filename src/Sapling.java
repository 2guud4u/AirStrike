import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Sapling extends Plants{



    public Sapling(int health, int healthLimit, int actionPeriod, int animationPeriod, String id, Point position, List<PImage> images) {
        super(health, healthLimit, actionPeriod, animationPeriod, id, position, images);
    }



    public void executeActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.setHealth(getHealth() + 1);
        if (!transformPlant( world, scheduler, imageStore))
        {
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
            return this.transformSapling( world, scheduler, imageStore);
    }



    public boolean transformSapling(
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

                return true;

        }
        else if (this.getHealth() >= this.getHealthLimit())
        {
            Entity tree = Factory.createTree("tree_" + this.getId(),
                    this.getPosition(),
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    imageStore.getImageList( Functions.TREE_KEY));

            world.removeEntity( this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity( tree);
            ((ActivityEntity)tree).scheduleActions( scheduler, world, imageStore);

            return true;
        }

        return false;
    }






}
