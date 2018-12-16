import processing.core.PImage;
import java.util.List;
public abstract class Active extends Entity {
    private int actionPeriod;

    public Active(String id, Point position, List<PImage> images,
                  int actionPeriod){
        super(id,position,images);
        this.actionPeriod = actionPeriod;

    }
    public int getActionPeriod(){return this.actionPeriod;}
    protected abstract void executeActivity(WorldModel world,
                         ImageStore imageStore, EventScheduler scheduler);
    public void scheduleActions( EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent( this,
                Factory.createAction(this, world, imageStore),
                this.getActionPeriod());
    }
}
