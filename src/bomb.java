import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// TODO spawn when plane get in x position of target point.
//  After last animation change terrain. if dude is on terrain turn into on fire.
//  Should remove blown up entities other than dude or obstacles

public class bomb extends ActivityEntity{
    private int fuse = 0;
    public bomb(String id, Point position, List<PImage> images) {
        super(100,125, id, position, images);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        fuse++;
        if(fuse == 4) {

            //corners
            System.out.print(getBlastRadius());
            for(int i=0; i<9; i++) {
                destruction(world, imageStore, scheduler);
            }
            world.setBackground( new Point(getPosition().x-1, getPosition().y+1), new Background("i", imageStore.getImageList( "bombed_around")));
            world.setBackground( new Point(getPosition().x-1, getPosition().y-1), new Background("i", imageStore.getImageList( "bombed_around")));
            world.setBackground( new Point(getPosition().x+1, getPosition().y+1), new Background("i", imageStore.getImageList( "bombed_around")));
            world.setBackground( new Point(getPosition().x+1, getPosition().y-1), new Background("i", imageStore.getImageList( "bombed_around")));
            //right/left
            world.setBackground( new Point(getPosition().x+1, getPosition().y), new Background("i", imageStore.getImageList( "bombed_around")));
            world.setBackground( new Point(getPosition().x-1, getPosition().y), new Background("i", imageStore.getImageList( "bombed_around")));
            //top/bottom
            world.setBackground( new Point(getPosition().x, getPosition().y-1), new Background("i", imageStore.getImageList( "bombed_around")));
            world.setBackground( new Point(getPosition().x, getPosition().y+1), new Background("i", imageStore.getImageList( "bombed_around")));
            //center
            world.setBackground( new Point(getPosition().x, getPosition().y), new Background("i", imageStore.getImageList( "bombed")));

            world.removeEntityAt(getPosition());


        }
        scheduler.scheduleEvent( this,
                createActivityAction( world, imageStore),
                this.getActionPeriod());
    }
    public void destruction(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        // spawn dude if case is dude
        // maybe use a switch case

        List<Entity> blastZone = new ArrayList<Entity>();
        List<Point> radius = getBlastRadius();
        for (Point p : radius)
        {
            if (world.isOccupied(p))
                blastZone.add(world.getOccupant(p).get());
        }
        for (Entity e : blastZone)
        {
            if (e instanceof Dude)
            {
                ((Dude) e).transformBlownUp(world, scheduler, imageStore);
            }
            else if (!(e instanceof Obstacle))
            {
                world.removeEntityAt(e.getPosition());
                scheduler.unscheduleAllEvents(e);
            }
        }
//            Optional<Entity> near = world.findNearest(this.getPosition(),
//                    new ArrayList<>(Arrays.asList(Tree.class, Dude.class, Fairy.class)));
//            if (near.isPresent()) {
//                if (getBlastRadius().contains(near.get().getPosition()) ) {
//                    Entity onfire = Factory.createOnFire("1", near.get().getPosition(), 1000, 125, 4, imageStore.getImageList("onfire"));
//                    world.removeEntity(near.get());
//                    world.addEntity(onfire);
//                    ((ActivityEntity) onfire).scheduleActions(scheduler, world, imageStore);
//
//                }

        }


    public List<Point> getBlastRadius()
    {
        List<Point> radius = new ArrayList<Point>();
        for (int x = getPosition().x-1; x < getPosition().x+2; x++)
        {
            for (int y = getPosition().y-1; y < getPosition().y+2; y++)
            {
                if (!new Point(x, y).equals(this.getPosition())) {
                    radius.add(new Point(x, y));
                }
            }
        }
        return radius;
    }
}

