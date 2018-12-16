import processing.core.PImage;
import java.util.Optional;
import java.util.List;

public class SuperMiner extends Mobile {
    private PathingStrategy strat;
    public static final String QUAKE_KEY = "quake";
    private static final String SMOULDER_KEY = "burn";
    public SuperMiner(String id, Point position,
                 List<PImage> images,
                 int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        strat = new AStarPathingStrategy();
    }

    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler){
        if (this.getPosition().adjacent( target.getPosition()))
        {
            world.removeEntity( target);
            scheduler.unscheduleAllEvents( target);
            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents( occupant.get());
                }

                world.moveEntity( this, nextPos);
            }
            return false;
        }


    }
    protected Point nextPosition( WorldModel world,
                                  Point destPos)
    {
        List<Point> path = strat.computePath(this.getPosition(), destPos, pt->world.withinBounds(pt) && !world.isOccupied(pt),
                (pt1,pt2)->pt1.adjacent(pt2), PathingStrategy.CARDINAL_NEIGHBORS);
        if(path.size() == 0) {
            return getPosition();
        }

        else {
            return path.get(0);
        }
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> demonTarget = world.findNearest(
                super.getPosition(), Demon.class);
        long nextPeriod = super.getActionPeriod();

        if (demonTarget.isPresent())
        {
            Point tgtPos = demonTarget.get().getPosition();

            if (moveTo(  world, demonTarget.get(), scheduler))
            {

                Entity quake = Factory.createQuake(tgtPos,
                        imageStore.getImageList( QUAKE_KEY));

                world.addEntity( quake);
                nextPeriod += super.getActionPeriod();
                ((Active)quake).scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Factory.createAction( this,world, imageStore),
                nextPeriod);
    }

}
