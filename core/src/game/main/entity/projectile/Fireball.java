package game.main.entity.projectile;

import game.SpriteSheet;

public class Fireball extends Projectile {
    public Fireball() {
        image = new SpriteSheet("fireball", 2, 1);
    }
}
