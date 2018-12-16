import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Nuke extends Mobile {

    public static final String FIRE_BALL_KEY = "fireball";
    public static final String SUPER_KEY = "superMiner";
    private static final int SUPER_PERIOD_SCALE = 2;
    private static final int SUPER_ANIMATION_MIN = 50;
    private static final int SUPER_ANIMATION_MAX = 100;
    private PathingStrategy strat;
    public Nuke(String id, Point position,
                List<PImage> images,
                int actionPeriod, int animationPeriod, int health ){
        super(id,position,images,actionPeriod,animationPeriod);
        strat = new AStarPathingStrategy();
    }
    protected boolean moveTo(WorldModel world,
                             Entity target, EventScheduler scheduler)
    {
        if (getPosition().adjacent( target.getPosition()))
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
    public void executeActivity( WorldModel world,
                                 ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> altTarget = world.findNearest(getPosition(),
                Demon.class);
        Optional<Entity> fullTarget = world.findNearest( getPosition(),
                Blacksmith.class);
        Optional<Entity> target = fullTarget;

        if(fullTarget.equals(Optional.empty())){
            target = altTarget;
        }
        if (target.isPresent() &&
                moveTo( world, target.get(), scheduler))
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
    static final Function<Point, Stream<Point>> BLAST_ZONE =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX(),point.getY()))
                            .add(new Point(point.getX(), point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY()))
                            .add(new Point(point.getX() + 1, point.getY()))
                            .add(new Point(point.getX() +1, point.getY()-1))
                            .add(new Point(point.getX()+1, point.getY()+1))
                            .add(new Point(point.getX()-1, point.getY()-1))
                            .add(new Point(point.getX()-1, point.getY()+1))
                            .add(new Point(point.getX() +2, point.getY()-1))
                            .add(new Point(point.getX()+2, point.getY()+1))
                            .add(new Point(point.getX()-2, point.getY()-1))
                            .add(new Point(point.getX()-2, point.getY()+1))
                            .add(new Point(point.getX(), point.getY() - 2))
                            .add(new Point(point.getX(), point.getY() + 2))
                            .add(new Point(point.getX() - 2, point.getY()))
                            .add(new Point(point.getX() + 2, point.getY()))
                            .add(new Point(point.getX() +2, point.getY()-2))
                            .add(new Point(point.getX()+2, point.getY()+2))
                            .add(new Point(point.getX()-2, point.getY()-2))
                            .add(new Point(point.getX()-2, point.getY()+2))
                            .add(new Point(point.getX() +3, point.getY()-2))
                            .add(new Point(point.getX()+3, point.getY()+2))
                            .add(new Point(point.getX()-3, point.getY()-2))
                            .add(new Point(point.getX()-3, point.getY()+2))
                            .add(new Point(point.getX(), point.getY() - 3))
                            .add(new Point(point.getX(), point.getY() + 3))
                            .add(new Point(point.getX() - 3, point.getY()))
                            .add(new Point(point.getX() + 3, point.getY()))
                            .add(new Point(point.getX() +3, point.getY()-3))
                            .add(new Point(point.getX()+3, point.getY()+3))
                            .add(new Point(point.getX()-3, point.getY()-3))
                            .add(new Point(point.getX()-3, point.getY()+3))
                            .build();
    private boolean transform( WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        //long nextPeriod = super.getActionPeriod();

        List<Point> zone= BLAST_ZONE.apply(getPosition()).filter(p->world.withinBounds(p)).collect(Collectors.toList());

        for(Point p : zone){
            if(world.isOccupied(p)){
                Entity e = world.getOccupancyCell(p);
                world.removeEntityAt(p);
                scheduler.unscheduleAllEvents(e);
            }
            Entity fireball = Factory.createFireBall(p,
                    imageStore.getImageList( FIRE_BALL_KEY));

            world.addEntity( fireball);
            ((Active)fireball).scheduleActions(scheduler, world, imageStore);
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

        }

        return true;
    }

}
