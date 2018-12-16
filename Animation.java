public class Animation implements Action{

    private Animated entity;

    private int repeatCount;

    public Animation( Animated entity, int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent( this.entity,
                    Factory.createAction(this.entity,
                            Math.max(this.repeatCount - 1, 0)),
                    this.entity.getAnimationPeriod());
        }
    }

}
