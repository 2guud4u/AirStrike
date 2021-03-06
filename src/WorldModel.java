import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel
{
    private final int numRows;
    private final int numCols;
    private final Background background[][];
    private final Entity occupancy[][];
    private final Set<Entity> entities;

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }
    public void addEntity(Entity entity) {
        if (withinBounds( entity.getPosition())) {
            setOccupancyCell( entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }
    public void addEntityAtPos(Entity entity, Point point) {
        entity.setPosition(point);
        if (withinBounds( entity.getPosition())) {
            setOccupancyCell( entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public void tryAddEntity( Entity entity) {
        if (isOccupied( entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0
                && pos.x < this.numCols;
    }

    public boolean isOccupied( Point pos) {
        return withinBounds(pos) && getOccupancyCell( pos) != null;
    }

    public Optional<Entity> findNearest(
             Point pos, List<Class> kinds)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind: kinds)
        {
            for (Entity entity : this.entities) {
                if (kind.isInstance(entity)) {
                    ofType.add(entity);
                }
            }
        }

        return pos.nearestEntity(ofType);
    }

    public void moveEntity( Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds( pos) && !pos.equals(oldPos)) {
            if(!(entity instanceof Plane) || !(entity instanceof Bullet)) {
                setOccupancyCell(oldPos, null);
                removeEntityAt(pos);
                setOccupancyCell(pos, entity);
                entity.setPosition(pos);
            }
            else{ // TODO move entity infront and replace it after it flys
                setOccupancyCell(oldPos, null);
                setOccupancyCell(pos, entity);
                entity.setPosition(pos);
            }
        }
    }

    public void removeEntity(Entity entity) {
        removeEntityAt( entity.getPosition());
    }

    public void removeByEntity(Optional<Entity> entity){

}

    public void removeEntityAt( Point pos) {
        if (this.withinBounds( pos) && this.getOccupancyCell( pos) != null) {
            Entity entity = this.getOccupancyCell( pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell( pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(
             Point pos)
    {
        if (this.withinBounds( pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }

    public void setBackground(
             Point pos, Background background)
    {
        if (this.withinBounds( pos)) {
            setBackgroundCell( pos, background);
        }
    }

    public Optional<Entity> getOccupant( Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(getOccupancyCell( pos));
        }
        else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell( Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public  void setOccupancyCell(
             Point pos, Entity entity)
    {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public Background getBackgroundCell( Point pos) {
        return this.background[pos.y][pos.x];
    }

    public void setBackgroundCell(
             Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

}
