import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background
{
    private String id;
    private List<PImage> images;
    private int imageIndex;

    public int getImageIndex() {
        return imageIndex;
    }

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }
    public PImage getCurrentImage() {
        return this.images.get(
                    this.imageIndex);

    }
    public List<PImage> getImages(){
        return this.images;
    }
}
