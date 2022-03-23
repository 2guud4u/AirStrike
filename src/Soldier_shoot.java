import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Soldier_shoot extends Dude{
    private boolean alliance;
    private int timer = 0;
    private boolean shot = true;
    public Soldier_shoot(  String id, Point position, List<PImage> images, boolean alliance) {
        super(4, 0, 100, 100, id, position, images);
        this.alliance = alliance;
    }



    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target =
                world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Dude_Full.class, Dude_Not_Full.class)));
        Optional<Entity> blocked =
                world.findNearest( this.getPosition(), new ArrayList<>(Arrays.asList(Fairy.class, Plants.class, Stump.class, House.class, Obstacle.class)));
        int directionx = getPosition().x - target.get().getPosition().x;
        int directiony = getPosition().y - target.get().getPosition().y;
        timer++;
        if (timer > 10) {
        if (!Functions.adjacent(blocked.get().getPosition(), getPosition()) && shot) {
            if (Math.abs(directionx) > Math.abs(directiony)) {
                if (directionx < 0) {
                   Entity bullet = Factory.createBullet("hi", new Point(getPosition().x + 1,
                            getPosition().y), imageStore.getImageList("bulletright"), "Right");
                    spawnBullet(target, world, scheduler, bullet, imageStore);

                } else {
                    Entity bullet = Factory.createBullet("hi", new Point(getPosition().x - 1,
                            getPosition().y), imageStore.getImageList("bulletleft"), "Left");
                    spawnBullet(target, world, scheduler, bullet, imageStore);
                }
            } else {
                if (directiony < 0) {
                    Entity bullet= Factory.createBullet("hi", new Point(getPosition().x,
                            getPosition().y + 1), imageStore.getImageList("bulletdown"), "Down");
                    spawnBullet(target, world, scheduler, bullet, imageStore);
                } else {
                    Entity bullet = Factory.createBullet("hi", new Point(getPosition().x,
                            getPosition().y - 1), imageStore.getImageList("bulletup"), "Up");
                    spawnBullet(target, world, scheduler, bullet, imageStore);

                }
            }
            shot = false;
        }
            Entity soldier = Factory.createSoldier("1", getPosition(),
                    imageStore.getImageList("soldier"), alliance);
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(soldier);
            ((ActivityEntity) soldier).scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent( this,
                createActivityAction( world, imageStore),
                this.getActionPeriod());
    }
    public void spawnBullet(Optional<Entity> target, WorldModel world, EventScheduler scheduler, Entity bullet, ImageStore imageStore){
        if(target.get().getPosition().equals(bullet.getPosition())){
            Entity corpse = Factory.createCorpse(target.get().getId(), bullet.getPosition(),
                    imageStore.getImageList(Functions.CORPSE_KEY));
            world.removeEntity(target.get());
            scheduler.unscheduleAllEvents(this);
            world.removeEntity(target.get());
            scheduler.unscheduleAllEvents(target.get());

            world.addEntity(corpse);
        }
        else{
            world.addEntity( bullet);
            ((ActivityEntity)bullet).scheduleActions( scheduler, world, imageStore);
        }
    }
}
