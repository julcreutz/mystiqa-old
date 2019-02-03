package game.main.positionable.entity.projectile;

import game.resource.SpriteSheet;

public class Fireball extends Projectile {
    public Fireball() {
        image = new SpriteSheet("fireball", 2, 1);
    }
}
