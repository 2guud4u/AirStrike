import processing.core.PImage;

import java.util.List;

public class Factory {
    public static Entity createHouse(
            String id, Point position, List<PImage> images)
    {
        return new House( id, position, images);
    }

    public static Entity createObstacle(
            String id, Point position, int animationPeriod, List<PImage> images)
    {
        return new Obstacle( id, position, images,
                animationPeriod);
    }

    public static Entity createTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new Tree( health, Functions.SAPLING_HEALTH_LIMIT, actionPeriod,animationPeriod,id, position, images);
    }

    public static Entity createStump(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Stump(id, position, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static Entity createSapling(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Sapling( 0, Functions.SAPLING_HEALTH_LIMIT,
                Functions.SAPLING_ACTION_ANIMATION_PERIOD,Functions.SAPLING_ACTION_ANIMATION_PERIOD,id, position, images);
    }
    public static Entity createFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Fairy(id, position,  images,actionPeriod,animationPeriod
                 );
    }

    // need resource count, though it always starts at 0
    public static Entity createDudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        return new Dude_Not_Full( id, position, images, resourceLimit, 0,
                actionPeriod, animationPeriod);
    }

    // don't technically need resource count ... full
    public static Entity createDudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new Dude_Full( id, position, images, resourceLimit, 0,
                actionPeriod, animationPeriod);
    }
    public static Entity createOnFire(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        return new OnFire( id, position, images,
                actionPeriod, animationPeriod);
    }
    public static Entity createPlane(
            String id,
            Point position,
            List<PImage> images, Point goal)
    {
        return new Plane( id, position, images, goal);
    }
    public static Entity createBomb(
            String id,
            Point position,
            List<PImage> images)
    {
        return new bomb( id, position, images);
    }
    public static Entity createSoldier(
            String id,
            Point position,
            List<PImage> images, boolean alliance)
    {
        System.out.println("soldweir made");
        return new Soldier( id, position, images
                , alliance);
    }
    public static Entity createSoldier_shoot(
            String id,
            Point position,
            List<PImage> images, boolean alliance)
    {
        return new Soldier_shoot(id,
                position, images, alliance);
    }
    public static  Entity createBullet(
            String id, Point position, List<PImage> images,
            String direction){
        System.out.println("bullet made");
        return new Bullet( id, position, images, direction);
    }

    public static Entity createCorpse(String id,
                                      Point position,
                                      List<PImage> images)
    {
        return new Corpse(id, position, images);
    }

}
