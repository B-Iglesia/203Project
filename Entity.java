import processing.core.PImage;
import java.util.List;
public abstract class Entity
{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Entity(final String id, Point position, final List<PImage> images){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }
    public String getId(){return this.id;}
    public Point getPosition(){
        return this.position;
    }
    public void setPosition(Point p){
        this.position = p;
    }
    public PImage getCurrentImage() {return images.get(this.imageIndex);}
    public List<PImage> getImages(){return this.images;}
    public int getImageIndex(){return this.imageIndex;}
    public void setImageIndex(int val){imageIndex = val;}
}
