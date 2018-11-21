package mystiqa;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Voronoi {
    public Vector2[] points;

    public int width;
    public int height;

    public Voronoi(int size, int width, int height) {
        points = new Vector2[size];

        this.width = width;
        this.height = height;

        for (int i = 0; i < size; i++) {
            points[i] = new Vector2(MathUtils.random(width - 1), MathUtils.random(height - 1));
        }
    }

    public int getIndex(float x, float y) {
        int index = 0;

        for (int i = 0; i < points.length; i++) {
            if (getEuclideanDistance(points[i], new Vector2(x, y)) < getEuclideanDistance(points[index], new Vector2(x, y))) {
                index = i;
            }
        }

        return index;
    }

    public Vector2 getPoint(float x, float y) {
        return points[getIndex(x, y)];
    }

    private float getEuclideanDistance(Vector2 v1, Vector2 v2) {
        return new Vector2(v1).sub(new Vector2(v2)).len();
    }

    private float getManhattenDistance(Vector2 v1, Vector2 v2) {
        return Math.abs(v2.x - v1.x) + Math.abs(v2.y - v1.y);
    }
}
