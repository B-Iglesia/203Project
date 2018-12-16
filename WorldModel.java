import processing.core.PImage;

import java.util.*;

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;
   private final int ORE_REACH = 1;

   public Set<Entity> getEntities(){
       return this.entities;
   }
   public int getNumRows(){
       return this.numRows;
   }
   public int getNumCols(){
       return this.numCols;
   }
   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }
    public void setBackgroundCell (Point pos,
                                         Background background)
    {
        this.background[pos.getY()][pos.getX()] = background;
    }
    public Background getBackgroundCell( Point pos)
    {
        return this.background[pos.getY()][pos.getX()];
    }
    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (this.withinBounds(pos))
        {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
        }
        else
        {
            return Optional.empty();
        }
    }

    public void setBackground( Point pos,
                                     Background background)
    {
        if (this.withinBounds( pos))
        {
            this.setBackgroundCell( pos, background);
        }
    }
    public void addEntity( Entity entity)
    {
        if (this.withinBounds(entity.getPosition()))
        {
            this.setOccupancyCell( entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }
    public void moveEntity( Entity entity, Point pos)
    {
        Point oldPos = entity.getPosition();
        if (this.withinBounds( pos) && !pos.equals(oldPos))
        {
            this.setOccupancyCell( oldPos, null);
            this.removeEntityAt( pos);
            this.setOccupancyCell( pos, entity);
            entity.setPosition(pos) ;
        }
    }
    public void removeEntity( Entity entity)
    {
        this.removeEntityAt(entity.getPosition());
    }

    public void removeEntityAt( Point pos)
    {
        if (this.withinBounds( pos)
                && this.getOccupancyCell( pos) != null)
        {
            Entity entity = this.getOccupancyCell( pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
            entity.setPosition(new Point(-1, -1)) ;
            this.entities.remove(entity);
            this.setOccupancyCell( pos, null);
        }
    }

    public boolean withinBounds( Point pos)
    {
        return pos.getY() >= 0 && pos.getY() < this.numRows &&
                pos.getX() >= 0 && pos.getX() < this.numCols;
    }
    public void setOccupancyCell( Point pos,
                                        Entity entity)
    {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }
    public Entity getOccupancyCell( Point pos)
    {
        return this.occupancy[pos.getY()][pos.getX()];
    }

    public boolean isOccupied( Point pos)
    {
        return this.withinBounds( pos) &&
                this.getOccupancyCell(pos) != null;
    }

    public Optional<Entity> getOccupant( Point pos)
    {
        if (this.isOccupied( pos))
        {
            return Optional.of(this.getOccupancyCell( pos));
        }
        else
        {
            return Optional.empty();
        }
    }
    public Optional<Point> findOpenAround( Point pos)
    {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++)
        {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++)
            {
                Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
                if (this.withinBounds( newPt) &&
                        !this.isOccupied( newPt))
                {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }
    public  Optional<Entity> findNearest( Point pos,
                                               Class<? extends Entity> entity1)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities)
        {
            if (entity1.isInstance(entity))
            {
                ofType.add(entity);
            }
        }

        return this.nearestEntity(ofType, pos);
    }

    public Optional<Entity> nearestEntity(List<Entity> entities,
                                                 Point pos)
    {
        if (entities.isEmpty())
        {
            return Optional.empty();
        }
        else
        {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.getPosition().distanceSquared( pos);

            for (Entity other : entities)
            {
                int otherDistance = other.getPosition().distanceSquared( pos);

                if (otherDistance < nearestDistance)
                {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }


    public  void tryAddEntity( Entity entity)
    {
        if (this.isOccupied( entity.getPosition()))
        {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity( entity);
    }


}
