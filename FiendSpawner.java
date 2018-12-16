import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FiendSpawner extends Active {


    public static final Random rand = new Random();
    public static final String EGG_KEY = "egg";
    public static final String EGG_ID_PREFIX = "egg -- ";
    public static final int EGG_CORRUPT_MIN = 25000;
    public static final int EGG_CORRUPT_MAX = 30000;

    public FiendSpawner(String id, Point position,
                List<PImage> images,
                int actionPeriod) {
        super(id,position,images,actionPeriod);
    }


    protected void executeActivity( WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround( this.getPosition());

        if (openPt.isPresent())
        {
            Entity egg = Factory.createEgg(EGG_ID_PREFIX + super.getId(),
                    openPt.get(), EGG_CORRUPT_MIN +
                            rand.nextInt(EGG_CORRUPT_MAX - EGG_CORRUPT_MIN),
                    imageStore.getImageList(EGG_KEY));
            world.addEntity( egg);
            ((Active)egg).scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Factory.createAction(this, world, imageStore),
                super.getActionPeriod());
    }
}
