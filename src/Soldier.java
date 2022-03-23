import processing.awt.PGraphicsJava2D;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Soldier extends Dude{
    private boolean alliance;
    public Soldier(String id, Point position, List<PImage> images, boolean alliance) {

        super(4, 0, 1000, 100, id, position, images);
        this.alliance = alliance;
    }
    public void executeActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {


            Optional<Entity> target =
                    world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Dude_Full.class, Dude_Not_Full.class, OnFire.class)));

        if (alliance == true){
             target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Soldier.class)));
        }

        moveToSoldier( world, (AnimationEntity) target.get(), scheduler, imageStore);

        scheduler.scheduleEvent( this,
                createActivityAction( world, imageStore),
                this.getActionPeriod());

    }

    public void transformSoldier_shoot(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        Entity miner = Factory.createSoldier_shoot(this.getId(),
                this.getPosition(),
                imageStore.getImageList("soldier_shoot"), alliance);

        world.removeEntity( this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((ActivityEntity)miner).scheduleActions( scheduler, world, imageStore);


    }

    public void moveToSoldier(
            WorldModel world,
            AnimationEntity target,
            EventScheduler scheduler, ImageStore imageStore)
    {
        if (getShootRadius().contains(target.getPosition())) {
            transformSoldier_shoot(world, scheduler, imageStore);
            scheduler.unscheduleAllEvents(this);
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
    public List<Point> getShootRadius()
    {
        List<Point> radius = new ArrayList<Point>();
        for (int x = getPosition().x-5; x < getPosition().x+5; x++)
        {
            for (int y = getPosition().y-5; y < getPosition().y+5; y++)
            {
                if (!new Point(x, y).equals(this.getPosition()) && (getPosition().x == x || getPosition().y == y)) {
                    radius.add(new Point(x, y));
                }
            }
        }

        return radius;
    }
}
