import processing.core.PImage;
import java.util.Random;
import java.util.List;

public class FireBall extends Animated{

    private static final String SPAWNER_KEY = "fiend";
    private static final int SPAWNER_ACTION_PERIOD =  8832;
    private static final Random rand = new Random();
    private static final String SMOULDER_KEY = "burn";
    public FireBall(String id, Point position,
                 List<PImage> images,
                 int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
    }

protected void executeActivity( WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler)
{
    Point pos = this.getPosition();  // store current position before removing

    world.removeEntity( this);
    scheduler.unscheduleAllEvents( this);
    int transVal = transNum();
    if(transVal>=80) {
        Entity fiend = Factory.createSpawner(SPAWNER_KEY,
                pos, SPAWNER_ACTION_PERIOD,
                imageStore.getImageList(SPAWNER_KEY));

        world.addEntity(fiend);
        ((Active) fiend).scheduleActions(scheduler, world, imageStore);
    }
    else{

        Background smoulder = new Background(SMOULDER_KEY,imageStore.getImageList(SMOULDER_KEY));
        if(world.withinBounds(pos)){
            world.setBackgroundCell(pos,smoulder);
        }
    }
}

    public int transNum(){
        return rand.nextInt(100)+1;
    }
}
