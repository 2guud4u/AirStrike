public class Animation implements Action{
    private final AnimationEntity entity;
    private final int repeatCount;

    public Animation(
            AnimationEntity entity,
            int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(
            EventScheduler scheduler)
    {
        entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent((Entity) this.entity,
                    ((AnimationEntity)entity).createAnimationAction(
                            Math.max(this.repeatCount - 1,
                                    0)),
                    ((AnimationEntity)entity).getAnimationPeriod());
        }
    }


}
