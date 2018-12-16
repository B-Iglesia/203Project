import processing.core.PImage;

import java.util.List;


public class Egg extends Active {

    private static final String DEMON_KEY = "demon";
    private static final String DEMON_ID_SUFFIX = " -- yeet";
    private static final int DEMON_PERIOD_SCALE = 5;
    private static final int DEMON_ANIMATION_MIN = 50;
    private static final int DEMON_ANIMATION_MAX = 150;

    public Egg(String id, Point position,
               List<PImage> images,
               int actionPeriod) {
        super(id,position,images,actionPeriod);
    }
    protected void executeActivity( WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity( this);
        scheduler.unscheduleAllEvents( this);

        Entity demon = Factory.createDemon(this.getId() + this.DEMON_ID_SUFFIX,
                pos, this.getActionPeriod() / DEMON_PERIOD_SCALE,
                DEMON_ANIMATION_MIN +
                        Factory.rand.nextInt(DEMON_ANIMATION_MAX - DEMON_ANIMATION_MIN),
                imageStore.getImageList(DEMON_KEY));

        world.addEntity( demon);
        ((Active)demon).scheduleActions(scheduler, world, imageStore);
    }

}
