import processing.core.PImage;

import java.util.List;

public class Quake extends Animated{

    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(String id, Point position,
                          List<PImage> images,
                          int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity( WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents( this);
        world.removeEntity( this);
    }

}
