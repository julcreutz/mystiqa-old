package game.main.world_map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.loader.SheetLoader;
import game.main.Game;
import game.main.GameState;
import game.main.world_map.entity.WorldMapEntity;
import game.main.world_map.entity.WorldMapPlayer;
import game.main.world_map.tile.WorldMapTile;

public class WorldMap extends GameState {
    public static final float CAM_SPEED = 5f;
    public static final float CURSOR_ANIMATION_SPEED = 2.5f;

    public WorldMapTile[][] tiles;
    public Array<WorldMapEntity> entities;

    public WorldMapPlayer player;

    public int cursorX;
    public int cursorY;

    public Array<WorldMapNode> path;

    public WorldMapNode lastNode;
    public WorldMapNode nextNode;

    public boolean moving;
    public float moveTime;

    @Override
    public void create() {
        super.create();

        WorldMapGenerator.generate(this);
    }

    @Override
    public void update(Game g) {
        super.update(g);

        float camX = 0;
        float camY = 0;

        if (!moving) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                cursorX--;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                cursorX++;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                cursorY--;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                cursorY++;
            }

            path = findPath(MathUtils.floor(player.x / 8f), MathUtils.floor(player.y / 8f), cursorX, cursorY);

            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                moving = true;

                lastNode = path.get(0);
                nextNode = path.get(0);
            }

            camX = cursorX * 8 + 4;
            camY = cursorY * 8 + 4;
        } else {

            if (moveTime > 0) {
                player.x = MathUtils.lerp(lastNode.x * 8f, nextNode.x * 8f, 1 - moveTime);
                player.y = MathUtils.lerp(lastNode.y * 8f, nextNode.y * 8f, 1 - moveTime);

                WorldMapTile tile = tiles[lastNode.x][lastNode.y];
                float delta = Game.getDelta() / (tile != null ? tile.type.traversalCost : 1);

                moveTime -= delta * 2f;

                player.time += delta;
                player.dir = MathUtils.round((new Vector2(nextNode.x, nextNode.y).sub(lastNode.x, lastNode.y).angle() + 360) / 90f) % 4;
            } else {
                path.removeIndex(0);

                if (path.size > 0) {
                    lastNode = nextNode;
                    nextNode = path.get(0);

                    moveTime = 1;
                } else {
                    moving = false;

                    player.x = MathUtils.round(player.x / 8f) * 8;
                    player.y = MathUtils.round(player.y / 8f) * 8;
                }
            }

            camX = player.x + 4;
            camY = player.y + 4;
        }

        cam.position.x = MathUtils.clamp(MathUtils.lerp(cam.position.x, camX, Game.getDelta() * CAM_SPEED), Game.WIDTH * .5f, tiles.length * 8 - Game.WIDTH * .5f);
        cam.position.y = MathUtils.clamp(MathUtils.lerp(cam.position.y, camY, Game.getDelta() * CAM_SPEED), Game.HEIGHT * .5f, tiles[0].length * 8 - Game.HEIGHT * .5f);

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                WorldMapTile tile = tiles[x][y];

                if (tile != null) {
                    tile.update(this);
                }
            }
        }

        for (WorldMapEntity e : entities) {
            e.update(this);
        }
    }

    @Override
    public void renderToBuffer() {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                WorldMapTile tile = tiles[x][y];

                if (tile != null) {
                    tile.render(batch);
                }
            }
        }

        TextureRegion[][] pathSheet = SheetLoader.load("Path");

        if (path != null && path.size > 1) {
            for (int i = 0; i < path.size; i++) {
                WorldMapNode node = path.get(i);
                batch.draw(pathSheet[0][0], node.x * 8, node.y * 8);

                if (i > 0) {
                    WorldMapNode _node = path.get(i - 1);
                    batch.draw(pathSheet[1][0], node.x * 8, node.y * 8, 4, 4, 8, 8, 1, 1, new Vector2(_node.x, _node.y).sub(node.x, node.y).angle());
                }

                if (i < path.size - 1) {
                    WorldMapNode _node = path.get(i + 1);
                    batch.draw(pathSheet[1][0], node.x * 8, node.y * 8, 4, 4, 8, 8, 1, 1, new Vector2(_node.x, _node.y).sub(node.x, node.y).angle());
                }

                if (i == path.size - 1) {
                    WorldMapNode _node = path.get(i - 1);
                    batch.draw(pathSheet[2][0], node.x * 8, node.y * 8, 4, 4, 8, 8, 1, 1, new Vector2(node.x, node.y).sub(_node.x, _node.y).angle());
                }
            }
        }

        for (WorldMapEntity e : entities) {
            e.render(batch);
        }

        TextureRegion[][] cursorSheet = SheetLoader.load("Cursor");
        TextureRegion cursor = cursorSheet[MathUtils.floor(Game.time * CURSOR_ANIMATION_SPEED) % cursorSheet.length][0];

        batch.draw(cursor, cursorX * 8 - 8, cursorY * 8 + 8, 4, 4, 8, 8, 1, 1, 0);
        batch.draw(cursor, cursorX * 8 + 8, cursorY * 8 + 8, 4, 4, 8, 8, 1, 1, 270);
        batch.draw(cursor, cursorX * 8 + 8, cursorY * 8 - 8, 4, 4, 8, 8, 1, 1, 180);
        batch.draw(cursor, cursorX * 8 - 8, cursorY * 8 - 8, 4, 4, 8, 8, 1, 1, 90);
    }

    public Array<WorldMapNode> findPath(int x1, int y1, int x2, int y2) {
        Array<WorldMapNode> openList = new Array<WorldMapNode>();
        Array<WorldMapNode> closedList = new Array<WorldMapNode>();

        openList.add(new WorldMapNode(x1, y1));

        do {
            WorldMapNode curr = null;

            // Find node with lowest f
            for (WorldMapNode node : openList) {
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
                Array<WorldMapNode> path = new Array<WorldMapNode>();

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

                        if (xx >= 0 && xx < tiles.length && yy >= 0 && yy < tiles[0].length && tiles[xx][yy] != null) {
                            WorldMapNode node = new WorldMapNode(xx, yy);

                            // Ignore if node is in closed list
                            if (inList(closedList, node)) {
                                continue;
                            }

                            // Traversal cost of new node + old one
                            float g = curr.g + tiles[xx][yy].type.traversalCost;

                            // Ignore if new path is slower
                            if (inList(openList, node) && g >= getNode(openList, xx, yy).g) {
                                continue;
                            }

                            node.parent = curr;
                            node.g = g;

                            float f = g + (int) new Vector2(xx, yy).sub(x2, y2).len();

                            // Update f or add to open list if not already in it
                            if (inList(openList, node)) {
                                getNode(openList, xx, yy).f = f;
                            } else {
                                node.f = f;
                                openList.add(node);
                            }
                        }
                    }
                }
            }
        } while (openList.size > 0);

        return null;
    }

    public WorldMapNode getNode(Array<WorldMapNode> list, int x, int y) {
        for (WorldMapNode node : list) {
            if (node.x == x && node.y == y) {
                return node;
            }
        }

        return null;
    }

    public boolean inList(Array<WorldMapNode> list, WorldMapNode node) {
        for (WorldMapNode _node : list) {
            if (node.x == _node.x && node.y == _node.y) {
                return true;
            }
        }

        return false;
    }
}
