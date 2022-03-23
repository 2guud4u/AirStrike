import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends Entity{
    private final int animationPeriod;

    public AnimationEntity(int animationPeriod, String id, Point position, List<PImage> images) {
        super( id, position,  images);
        this.animationPeriod = animationPeriod;
    }

    public Action createAnimationAction( int repeatCount) {
        return new Animation(this, repeatCount);
    }
    public int getAnimationPeriod() {
        return this.animationPeriod;
    }
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent( this, this.createAnimationAction( 0), getAnimationPeriod());

    }
    public void nextImage() {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    } // use a setter

}
