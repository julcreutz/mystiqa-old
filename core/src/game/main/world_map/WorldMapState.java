package game.main.world_map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.loader.TileLoader;
import game.main.Game;
import game.main.GameState;
import game.main.site.SiteData;
import game.main.world_map.entity.WorldMapEntity;
import game.main.world_map.entity.WorldMapPlayer;
import game.main.world_map.site.Site;
import game.main.world_map.biome.Biome;
import game.noise.Noise;
import game.noise.NoiseParameters;

public class WorldMapState extends GameState {
    public static final float CAM_SPEED = 5f;

    public WorldMapGenerator generator;

    public Biome[][] biomes;
    public Site[][] sites;

    public Array<WorldMapEntity> entities;

    public WorldMapPlayer player;

    public int lastX;
    public int lastY;

    public int nextX;
    public int nextY;

    public int cursorX;
    public int cursorY;

    public float moveTime;

    @Override
    public void create() {
        super.create();

        generator = new WorldMapGenerator(this);
        generator.generate();
    }

    @Override
    public void update(Game g) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            generator.generate();
        }

        if (nextX != lastX || nextY != lastY) {
            if (moveTime == 0) {
                moveTime = 1;
            }

            float speed = Game.delta() * (2f / biomes[lastX][lastY].type.traversalCost);

            moveTime -= speed;
            player.time += speed * .5f;

            player.dir = MathUtils.round((MathUtils.atan2(nextY - lastY, nextX - lastX) * MathUtils.radDeg + 360f) / 90f) % 4;

            if (moveTime < 0) {
                lastX = nextX;
                lastY = nextY;
                moveTime = 0;
            }

            player.x = MathUtils.lerp(lastX, nextX, 1 - moveTime) * 8f;
            player.y = MathUtils.lerp(lastY, nextY, 1 - moveTime) * 8f;
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                SiteData site = new SiteData();

                for (int x = 0; x < site.tiles.length; x++) {
                    for (int y = site.tiles[0].length - 1; y >= 0; y--) {
                        site.placeTile(TileLoader.load("Grass"), x, y, 0);

                        if (MathUtils.randomBoolean(.05f)) {
                            site.placeTile(TileLoader.load("TreeBottom"), x, y, 0);

                            int z = 1;
                            for (int i = 0; i < MathUtils.random(0, 2); i++) {
                                site.placeTile(TileLoader.load("TreeMiddle"), x, y + z, 1);
                                z++;
                            }

                            site.placeTile(TileLoader.load("TreeTop"), x, y + z, 1);
                        }
                    }
                }

                g.SITE.data = site;
                g.nextState = g.SITE;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                nextX--;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                nextX++;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                nextY--;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                nextY++;
            }
        }

        cam.position.x = MathUtils.clamp(MathUtils.lerp(cam.position.x, player.x + 4, Game.delta() * CAM_SPEED), Game.WIDTH * .5f, (biomes.length - 1) * 8 - Game.WIDTH * .5f);
        cam.position.y = MathUtils.clamp(MathUtils.lerp(cam.position.y, player.y + 4, Game.delta() * CAM_SPEED), Game.HEIGHT * .5f, (biomes[0].length - 1) * 8 - Game.HEIGHT * .5f);

        for (int x = 0; x < biomes.length; x++) {
            for (int y = 0; y < biomes[0].length; y++) {
                Biome tile = biomes[x][y];

                if (tile != null) {
                    tile.update(this);
                }
            }
        }

        for (int x = 0; x < sites.length; x++) {
            for (int y = 0; y < sites[0].length; y++) {
                Site site = sites[x][y];

                if (site != null) {
                    site.update(this);
                }
            }
        }

        for (WorldMapEntity e : entities) {
            e.update(this);
        }

        super.update(g);
    }

    @Override
    public void renderToBuffer() {
        int x0 = (int) MathUtils.clamp((cam.position.x - 72) / 8, 0, biomes.length - 1);
        int x1 = (int) MathUtils.clamp((cam.position.x + 72) / 8 + 1, 0, biomes.length - 1);

        int y0 = (int) MathUtils.clamp((cam.position.y - 36) / 8, 0, biomes[0].length - 1);
        int y1 = (int) MathUtils.clamp((cam.position.y + 36) / 8 + 1, 0, biomes[0].length - 1);

        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                Biome tile = biomes[x][y];

                if (tile != null) {
                    tile.render(batch);
                }
            }
        }

        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                Site site = sites[x][y];

                if (site != null) {
                    site.render(batch);
                }
            }
        }

        /*
        TextureRegion[][] pathSheet = SheetLoader.load("Path");

        if (path != null && path.size > 1) {
            for (int i = 0; i < path.size; i++) {
                WorldMapNode node = path.noiseAt(i);
                batch.draw(pathSheet[0][0], node.x * 8, node.y * 8);

                if (i > 0) {
                    WorldMapNode _node = path.noiseAt(i - 1);
                    batch.draw(pathSheet[1][0], node.x * 8, node.y * 8, 4, 4, 8, 8, 1, 1, new Vector2(_node.x, _node.y).sub(node.x, node.y).angle());
                }

                if (i < path.size - 1) {
                    WorldMapNode _node = path.noiseAt(i + 1);
                    batch.draw(pathSheet[1][0], node.x * 8, node.y * 8, 4, 4, 8, 8, 1, 1, new Vector2(_node.x, _node.y).sub(node.x, node.y).angle());
                }

                if (i == path.size - 1) {
                    WorldMapNode _node = path.noiseAt(i - 1);
                    batch.draw(pathSheet[2][0], node.x * 8, node.y * 8, 4, 4, 8, 8, 1, 1, new Vector2(node.x, node.y).sub(_node.x, _node.y).angle());
                }
            }
        }
        */

        for (WorldMapEntity e : entities) {
            e.render(batch);
        }

        /*
        if (!moving && ((cursorX != MathUtils.floor(player.x / 8f) || cursorY != MathUtils.floor(player.y / 8f)) || sites[MathUtils.floor(player.x / 8f)][MathUtils.floor(player.y / 8f)] != null)) {
            TextureRegion[][] cursorSheet = SheetLoader.load("Cursor");
            TextureRegion cursor = cursorSheet[MathUtils.floor(Game.time * CURSOR_ANIMATION_SPEED) % cursorSheet.length][0];

            batch.draw(cursor, cursorX * 8 - 8, cursorY * 8 + 8, 4, 4, 8, 8, 1, 1, 0);
            batch.draw(cursor, cursorX * 8 + 8, cursorY * 8 + 8, 4, 4, 8, 8, 1, 1, 270);
            batch.draw(cursor, cursorX * 8 + 8, cursorY * 8 - 8, 4, 4, 8, 8, 1, 1, 180);
            batch.draw(cursor, cursorX * 8 - 8, cursorY * 8 - 8, 4, 4, 8, 8, 1, 1, 90);
        }
        */

        Site site = sites[cursorX][cursorY];

        if (site != null) {
            Game.write(batch, "Hateno Village", site.x * 8f + 4, site.y * 8f + 16, true);
        }
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

                        if (xx >= 0 && xx < biomes.length && yy >= 0 && yy < biomes[0].length && biomes[xx][yy] != null && biomes[xx][yy].type.traversable) {
                            WorldMapNode node = new WorldMapNode(xx, yy);

                            // Ignore if node is in closed list
                            if (inList(closedList, node)) {
                                continue;
                            }

                            // Traversal cost of new node + old one
                            float g = curr.g + biomes[xx][yy].type.traversalCost;

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
