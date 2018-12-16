import processing.core.PImage;

import java.util.List;


public class Ore extends Active {

    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    public Ore(String id, Point position,
                          List<PImage> images,
                          int actionPeriod) {
        super(id,position,images,actionPeriod);
    }
    protected void executeActivity( WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        //world.removeEntity( this);
        //scheduler.unscheduleAllEvents( this);

        Entity blob = Factory.createOreBlob(this.getId() + this.BLOB_ID_SUFFIX,
                pos, this.getActionPeriod() / this.BLOB_PERIOD_SCALE,
                this.BLOB_ANIMATION_MIN +
                        Factory.rand.nextInt(this.BLOB_ANIMATION_MAX - this.BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        //world.addEntity( blob);
        //((Active)blob).scheduleActions(scheduler, world, imageStore);
    }

}
