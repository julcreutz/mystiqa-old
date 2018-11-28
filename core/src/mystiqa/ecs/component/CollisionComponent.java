package mystiqa.ecs.component;

public class CollisionComponent implements EntityComponent  {
    public float x;
    public float y;
    public float z;

    public float width;
    public float height;
    public float depth;

    public boolean overlaps(float x, float y, float z, CollisionComponent _collision, float _x, float _y, float _z) {
        return x + this.x + width > _x + _collision.x && x + this.x < _x + _collision.x + _collision.width
                && y + this.y + height > _y + _collision.y && y + this.y < _y + _collision.y + _collision.height
                && z + this.z + depth > _z + _collision.z && z + this.z < _z + _collision.z + _collision.depth;
    }
}
