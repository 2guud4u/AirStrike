import processing.core.PImage;

import java.util.List;

public abstract class Entity {
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex = 0;

    public Entity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
    }


    public PImage getCurrentImage() {
        return this.images.get((this.imageIndex));
    }

    public String getId(){
        return this.id;
    }
    public Point getPosition(){
        return this.position;
    }
    public List<PImage> getImages(){
        return this.images;
    }

    public int getImageIndex(){
        return this.imageIndex;
    }
    public void setPosition(Point position){
        this.position = position;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }


}
