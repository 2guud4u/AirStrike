import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends AnimationEntity{
     private final int actionPeriod;
     //private PathingStrategy strategy = new SingleStepPathingStrategy();
     private PathingStrategy strategy = new AStarPathingStrategy();
     public ActivityEntity(int actionPeriod, int animationPeriod, String id, Point position, List<PImage> images) {
          super(animationPeriod, id,position, images);
          this.actionPeriod = actionPeriod;
     }

     public int getActionPeriod() {
          return actionPeriod;
     }

     public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
     public Action createActivityAction(
             WorldModel world, ImageStore imageStore)
     {
          return new Activity( this, world, imageStore);
     }
     public void scheduleActions(
             EventScheduler scheduler,
             WorldModel world,
             ImageStore imageStore)
     {
          scheduler.scheduleEvent( this,
                  createActivityAction( world, imageStore),
                  this.getActionPeriod());
          super.scheduleActions( scheduler, world, imageStore);


     }

     public PathingStrategy getStrategy() {
          return strategy;
     }
}
