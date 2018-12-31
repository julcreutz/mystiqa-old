package game.main.state.play.map;

public class Node {
    public int x;
    public int y;

    public Node parent;

    public float f;
    public float g;
    public float h;

    public Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;

        this.parent = parent;
    }

    public Node(int x, int y) {
        this(x, y, null);
    }

    public Node() {
        this(0, 0, null);
    }
}
