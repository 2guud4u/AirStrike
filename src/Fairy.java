import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Fairy extends ActivityEntity{

    public Fairy(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(actionPeriod, animationPeriod,  id,  position, images);

    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        Optional<Entity> fairyTarget =
                world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Revivable.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();
            int move = moveToFairy( world, fairyTarget.get(), scheduler);

            if (move > 0) {
                if (move == 1)
                {
                    Entity sapling = Factory.createSapling("sapling_" + this.getId(), tgtPos,
                            imageStore.getImageList( Functions.SAPLING_KEY));

                    world.addEntity(sapling);
                    ((ActivityEntity)sapling).scheduleActions( scheduler, world, imageStore);
                }
                else {
                    Entity dude_Not_Full = Factory.createDudeNotFull("Dude_Not_Full_" , tgtPos
                            , 1000, 100,4 , imageStore.getImageList( Functions.DUDE_KEY));

                    world.addEntity(dude_Not_Full);
                    ((ActivityEntity)dude_Not_Full).scheduleActions( scheduler, world, imageStore);
                }
            }
        }

        scheduler.scheduleEvent( this,
                createActivityAction( world, imageStore),
                this.getActionPeriod());
    }


    public Point nextPositionFairy(
            WorldModel world, Point destPos) {

        List<Point> points = getStrategy().computePath(getPosition(), destPos, s -> !(world.isOccupied(s)),
                (p1, p2) -> Functions.adjacent(p1,p2), PathingStrategy.CARDINAL_NEIGHBORS );
        if (points.size() == 0)
        {
            return getPosition();
        }
        return points.get(0);
    }

    public int moveToFairy(

            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            if (target.getClass() == Stump.class) {
                return 1;
            }
            else {
                return 2;
            }
        }
        else {
            Point nextPos = this.nextPositionFairy( world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity( this, nextPos);
            }
            return 0;
        }
    }



}
