import processing.core.PImage;

import java.util.List;


public class Obstacle extends AnimationEntity{

    public Obstacle(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod)
    {
        super(animationPeriod, id,  position,  images);
    }

}


