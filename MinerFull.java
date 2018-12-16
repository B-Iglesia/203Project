import processing.core.PImage;
import java.util.Optional;
import java.util.List;

public class MinerFull extends Mobile {
    private PathingStrategy strat;
    private int resourceLimit;
    private int resourceCount;

    public MinerFull(String id, Point position,
                          List<PImage> images, int resourceLimit, int resourceCount,
                          int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        strat = new AStarPathingStrategy();
    }
    protected boolean moveTo( WorldModel world,
                    Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            return true;
        }
        else
        {
            Point nextPos = nextPosition( world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
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


    public void transform( WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        Entity miner = Factory.createMinerNotFull(getId(), resourceLimit,
                getPosition(), getActionPeriod(), getAnimationPeriod(),
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents( this);

        world.addEntity(miner);
        ((Active)miner).scheduleActions( scheduler, world, imageStore);
    }
    public void executeActivity( WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest( getPosition(),
                Blacksmith.class);

        if (fullTarget.isPresent() &&
                moveTo( world, fullTarget.get(), scheduler))
        {
            transform( world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent( this,
                    Factory.createAction( this,world, imageStore),
                    getActionPeriod());
        }
    }

}
