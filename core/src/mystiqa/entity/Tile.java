package mystiqa.entity;

public class Tile extends Entity {
    public Tile() {
        hitbox.set(0, 0, 0, 16, 16, 16);
        attacking = false;
        defending = false;
        pushed = false;
        gravity = false;
    }
}
