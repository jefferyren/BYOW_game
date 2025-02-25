package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.ArrayList;

public class Hallway {
    ArrayList<Point> hallway;
    public Hallway() {
        hallway = new ArrayList<>();
    }

    public void addPoint(int x, int y) {
        Point p = new Point(x, y);
        hallway.add(p);
    }

    public void placeHallway(World world, TETile wall, TETile floor) {
        TETile[][] tiles = world.world;
        for (Point p : hallway) {
            int x = p.x;
            int y = p.y;
            tiles[x][y] = Tileset.FLOOR;
        }
        for (Point p : hallway) {
            placeWalls(tiles, p);
        }
    }
    public void placeWalls(TETile[][] tiles, Point p) {
        if (tiles[p.x + 1][p.y].equals(Tileset.NOTHING)) {
            tiles[p.x + 1][p.y] = Tileset.WALL;
        }
        if (tiles[p.x - 1][p.y].equals(Tileset.NOTHING)) {
            tiles[p.x - 1][p.y] = Tileset.WALL;
        }
        if (tiles[p.x][p.y + 1].equals(Tileset.NOTHING)) {
            tiles[p.x][p.y + 1] = Tileset.WALL;
        }
        if (tiles[p.x][p.y - 1].equals(Tileset.NOTHING)) {
            tiles[p.x][p.y - 1] = Tileset.WALL;
        }
        // diagonals here
        if (tiles[p.x - 1][p.y - 1].equals(Tileset.NOTHING)) {
            tiles[p.x - 1][p.y - 1] = Tileset.WALL;
        }
        if (tiles[p.x + 1][p.y + 1].equals(Tileset.NOTHING)) {
            tiles[p.x + 1][p.y + 1] = Tileset.WALL;
        }
        if (tiles[p.x + 1][p.y - 1].equals(Tileset.NOTHING)) {
            tiles[p.x + 1][p.y - 1] = Tileset.WALL;
        }
        if (tiles[p.x - 1][p.y + 1].equals(Tileset.NOTHING)) {
            tiles[p.x - 1][p.y + 1] = Tileset.WALL;
        }

    }
}
