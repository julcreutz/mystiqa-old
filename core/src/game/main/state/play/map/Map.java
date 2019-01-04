package game.main.state.play.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.Play;
import game.main.state.play.map.entity.Entity;
import game.main.state.play.map.entity.EntityManager;
import game.main.state.play.map.tile.Tile;
import game.main.state.play.map.tile.TileManager;

public abstract class Map implements Serializable {
    public static final float CAM_SPEED = 1.5f;

    public static final int X_VIEW = 1000;
    public static final int Y_VIEW = 800;

    public boolean generated;

    public TileManager tiles;

    public EntityManager entities;
    public Entity player;

    public int x0;
    public int x1;
    public int y0;
    public int y1;

    public float camX;
    public float camY;

    public float toCamX;
    public float toCamY;

    public float camPosX;
    public float camPosY;

    public float camTime;

    public Array<Teleport> teleports;

    public Map() {
        tiles = new TileManager(this);
        entities = new EntityManager(this);

        teleports = new Array<Teleport>();
    }

    public void update(Play play) {
        toCamX = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        toCamY = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);

        if (camX != toCamX || camY != toCamY) {
            if (camTime == 0) {
                camTime = 1;
            }

            camTime -= Game.getDelta() * CAM_SPEED;

            float p = MathUtils.clamp(1 - camTime, 0, 1);

            camPosX = MathUtils.lerp(camX, toCamX, p);
            camPosY = MathUtils.lerp(camY, toCamY, p);

            if (camTime < 0) {
                camX = toCamX;
                camY = toCamY;

                camTime = 0;
            }
        }

        x0 = MathUtils.clamp(MathUtils.floor(play.cam.position.x / 8f) - X_VIEW, 0, tiles.getWidth());
        x1 = MathUtils.clamp(x0 + X_VIEW * 2, 0, tiles.getWidth());
        y0 = MathUtils.clamp(MathUtils.floor(play.cam.position.y / 8f) - Y_VIEW, 0, tiles.getHeight());
        y1 = MathUtils.clamp(y0 + Y_VIEW * 2, 0, tiles.getHeight());

        tiles.update(x0, x1, y0, y1);
        entities.update();

        if (player.onTeleport) {
            boolean onTeleport = false;

            for (Teleport t : teleports) {
                if (t.rect.overlaps(player.hitbox.rect)) {
                    onTeleport = true;
                    break;
                }
            }

            if (!onTeleport) {
                player.onTeleport = false;
            }
        } else {
            for (Teleport t : teleports) {
                if (t.rect.overlaps(player.hitbox.rect)) {
                    player.onTeleport = true;

                    if (!t.destination.generated) {
                        t.destination.generate();
                    }

                    play.nextMap = t.destination;

                    player.x = t.destinationX;
                    player.y = t.destinationY;

                    play.nextMap.entities.addEntity(player);
                    play.nextMap.player = player;

                    entities.entities.removeValue(player, true);
                    player = null;

                    break;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.setShader(null);

        tiles.render(batch, x0, x1, y0, y1, 0, 1);

        batch.setShader(null);

        entities.render(batch);

        batch.setShader(null);

        tiles.render(batch, x0, x1, y0, y1, 1, tiles.getDepth());

        batch.draw(Game.SPRITE_SHEETS.load("GuiLayer").sheet[0][0], camPosX - Game.WIDTH * .5f, camPosY + Game.HEIGHT * .5f - 8, Game.WIDTH, 8);
    }

    public void positionCamera() {
        camX = toCamX = camPosX = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        camY = toCamY = camPosY = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);
    }

    public boolean isCamMoving() {
        return camX != toCamX || camY != toCamY;
    }

    public boolean isVisible(float x, float y) {
        return x >= x0 * 8 && x < x1 * 8 && y >= y0 * 8 && y < y1 * 8;
    }

    public boolean isVisible(Entity e) {
        return isVisible(e.x, e.y);
    }

    public void generate() {
        generated = true;
    }

    public void connect(Map destination, float fromX, float fromY, float toX, float toY) {
        teleports.add(new Teleport(destination, toX, toY, fromX, fromY, 8, 8));
        destination.teleports.add(new Teleport(this, fromX, fromY, toX, toY, 8, 8));
    }

    public Array<Node> findPath(int x1, int y1, int x2, int y2) {
        Array<Node> openList = new Array<Node>();
        Array<Node> closedList = new Array<Node>();

        openList.add(new Node(x1, y1));

        do {
            Node curr = null;

            // Find node with lowest f
            for (Node node : openList) {
                if (curr == null || node.f < curr.f) {
                    curr = node;
                }
            }

            if (curr == null) {
                return null;
            }

            // Remove from open list to prevent infinite loop
            openList.removeValue(curr, true);

            // Return path when node with lowest f is goal node
            if (curr.x == x2 && curr.y == y2) {
                Array<Node> path = new Array<Node>();

                // Recursively build path
                while (curr != null) {
                    path.add(curr);
                    curr = curr.parent;
                }

                // Reverse to have start at index 0
                path.reverse();

                return path;
            }

            // Add node to closed list to prevent infinite loop
            closedList.add(curr);

            // Check neighbour nodes
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if ((x != 0 || y != 0) && x * y == 0) {
                        int xx = curr.x + x;
                        int yy = curr.y + y;

                        if (tiles.inBounds(xx, yy)) {
                            Tile t = tiles.at(xx, yy, 0);

                            if (xx >= 0 && xx < tiles.getWidth() && yy >= 0 && yy < tiles.getHeight() && t != null && !t.solid) {
                                Node node = new Node(xx, yy);

                                // Ignore if node is in closed list
                                if (inList(closedList, node)) {
                                    continue;
                                }

                                // Traversal cost of new node + old one
                                float g = curr.g + 1;

                                // Ignore if new path is slower
                                if (inList(openList, node) && g >= nodeAt(openList, xx, yy).g) {
                                    continue;
                                }

                                node.parent = curr;
                                node.g = g;

                                float f = g + (int) new Vector2(xx, yy).sub(x2, y2).len();

                                // Update f or add to open list if not already in it
                                if (inList(openList, node)) {
                                    nodeAt(openList, xx, yy).f = f;
                                } else {
                                    node.f = f;
                                    openList.add(node);
                                }
                            }
                        }
                    }
                }
            }
        } while (openList.size > 0);

        return null;
    }

    public Node nodeAt(Array<Node> list, int x, int y) {
        for (Node node : list) {
            if (node.x == x && node.y == y) {
                return node;
            }
        }

        return null;
    }

    public boolean inList(Array<Node> list, Node node) {
        for (Node _node : list) {
            if (node.x == _node.x && node.y == _node.y) {
                return true;
            }
        }

        return false;
    }

    public abstract void placePlayer();
}
