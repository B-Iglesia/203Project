import processing.core.PImage;
import java.util.List;

public abstract class Animated extends Active{

    private int animationPeriod;


    public Animated(String id, Point position,
                    List<PImage> images,
                    int actionPeriod, int animationPeriod ){
        super(id,position,images,actionPeriod);
        this.animationPeriod = animationPeriod;

    }
    public int getAnimationPeriod(){return this.animationPeriod;}
    public void nextImage()
    {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }
    public void scheduleActions( EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent( this,
                Factory.createAction( this,world, imageStore),
                super.getActionPeriod());
        scheduler.scheduleEvent( this,
                Factory.createAction( this,0), this.getAnimationPeriod());

    }
}
