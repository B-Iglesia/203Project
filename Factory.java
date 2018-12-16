import processing.core.PImage;
import java.util.List;
import java.util.Random;

public class Factory {


    public static final Random rand = new Random();
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    private static final String FIRE_BALL_ID = "nuke";
    private static final int FIRE_BALL_ACTION_PERIOD = 1100;
    private static final int FIRE_BALL_ANIMATION_PERIOD = 100;


    public static Entity createBlacksmith(String id, Point position,
                                          List<PImage> images)
    {
        return new Blacksmith( id, position, images);
    }

    public static Entity createMinerFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images)
    {
        return new MinerFull( id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public static Entity createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images)
    {
        return new Miner_Not_Full( id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }
    public static Entity createSuperMiner(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new SuperMiner( id, position,images,  actionPeriod, animationPeriod);
    }



    public static Entity createObstacle(String id, Point position,
                                        List<PImage> images)
    {
        return new Obstacle( id, position, images);
    }

    public static Entity createOre(String id, Point position, int actionPeriod,
                                   List<PImage> images)
    {
        return new Ore( id, position, images,
                actionPeriod);
    }

    public static Entity createOreBlob(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Ore_Blob( id, position, images, actionPeriod, animationPeriod);
    }

    public static Entity createQuake(Point position, List<PImage> images)
    {
        return new Quake( QUAKE_ID, position, images,
                 QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }
    public static Entity createFireBall(Point position, List<PImage> images)
    {
        return new FireBall( FIRE_BALL_ID, position, images,
                FIRE_BALL_ACTION_PERIOD, FIRE_BALL_ANIMATION_PERIOD);
    }

    public static Entity createNuke(String id, Point position,
                                    List<PImage> images,
                                    int actionPeriod, int animationPeriod )
    {
        return new Nuke(id, position,images, actionPeriod, animationPeriod,0);
    }

    public static Entity createVein(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new Vein( id, position, images,
                actionPeriod );
    }
    public static Action createAction(Active entity, WorldModel world,
                               ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }
    public static Action createAction(Animated entity, int repeatCount)
    {
        return new Animation(entity,  repeatCount);
    }

    public static Entity createSpawner(String id, Point position, int actionPeriod,
                                    List<PImage> images)
    {
        return new FiendSpawner( id, position, images,
                actionPeriod );
    }
    public static Entity createEgg(String id, Point position, int actionPeriod,
                                   List<PImage> images)
    {
        return new Egg( id, position, images,
                actionPeriod);
    }
    public static Entity createDemon(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Demon( id, position, images, actionPeriod, animationPeriod);
    }

}












