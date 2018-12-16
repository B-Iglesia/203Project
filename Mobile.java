import processing.core.PImage;

import java.util.List;

abstract class Mobile extends Animated{
    int health;
    public Mobile(String id, Point position,
                    List<PImage> images,
                    int actionPeriod, int animationPeriod){
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected abstract boolean moveTo(WorldModel world,
                                 Entity target, EventScheduler scheduler);

    protected abstract Point nextPosition(WorldModel world,
                              Point destPos);

    public void scheduleActions( EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent( this,
                Factory.createAction( this,world, imageStore),
                super.getActionPeriod());
        scheduler.scheduleEvent( this,
                Factory.createAction( this,0), this.getAnimationPeriod());

    }

}
