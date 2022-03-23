import processing.core.PImage;

import java.util.List;

public abstract class Plants extends ActivityEntity{
    private int health;
    private final int healthLimit;
    public Plants(int health,int healthLimit,int actionPeriod, int animationPeriod, String id, Point position, List<PImage> images) {
        super(actionPeriod, animationPeriod, id, position, images);
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public int getHealth() {
        return health;
    }
    public abstract boolean transformPlant(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore);

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealthLimit() {
        return healthLimit;
    }
}
