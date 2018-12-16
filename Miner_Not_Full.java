import processing.core.PImage;
import java.util.Optional;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Miner_Not_Full extends Mobile{

    private PathingStrategy strat;
    private int resourceCount;
    private int resourceLimit;

    public Miner_Not_Full(String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
    {
        super(id,position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
        strat = new AStarPathingStrategy();
    }
    protected boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler)
    {
        if (getPosition().adjacent( target.getPosition()))
        {
            resourceCount += 1;
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
    protected Point nextPosition(WorldModel world,
                                          Point destPos)
    {
        List<Point> path = strat.computePath(this.getPosition(), destPos, pt->world.withinBounds(pt) && !world.isOccupied(pt),
                (pt1,pt2)->pt1.adjacent(pt2), PathingStrategy.ALL_NEIGHBORS);
        if(path.size() == 0) {
            return getPosition();
        }
        return path.get(0);
    }

    private boolean transform( WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit)
        {
            Entity miner = Factory.createMinerFull(getId(), resourceLimit,
                    getPosition(), getActionPeriod(), getAnimationPeriod(),
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((Active)miner).scheduleActions( scheduler, world, imageStore);

            return true;
        }

        return false;
    }
    public void executeActivity(
            WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest( getPosition(),
                Ore.class);

        if (!notFullTarget.isPresent() ||
                !moveTo(world, notFullTarget.get(), scheduler) ||
                !transform( world, scheduler, imageStore))
        {
            scheduler.scheduleEvent( this,
                    Factory.createAction(this, world, imageStore),
                    getActionPeriod());
        }
    }
}
