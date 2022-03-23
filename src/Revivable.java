import processing.core.PImage;

import java.util.List;

public abstract class Revivable extends Entity{

    public Revivable(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
