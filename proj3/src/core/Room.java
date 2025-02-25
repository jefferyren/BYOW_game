package core;

import tileengine.TETile;
import tileengine.Tileset;

// java.util.Dictionary;
import java.util.HashSet;

public class Room {
    int x; //x-coordinate of bottom left corner
    int y; //y-coordinate of bottom left corner
    int width;
    int height;
    Point center;
    HashSet<Point> border;
    private HashSet<Point> collisionborder;

    public Room(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        center = new Point(x + w / 2, y + h / 2);
        width = w;
        height = h;
        border = new HashSet<>();
        collisionborder = new HashSet<>();
        fillborderlists();
    }

    private void fillborderlists() {
        for (int i = x; i < x + width; i++) {
            border.add(new Point(i, y)); // Bottom wall
            collisionborder.add(new Point(i, y));
            border.add(new Point(i, y + height - 1)); // Top wall
            collisionborder.add(new Point(i, y + height - 1));
        }
        for (int s = 1; s < 4; s++) {
            collisionborder.add(new Point(x - s, y));
            collisionborder.add(new Point(x + s, y));
            collisionborder.add(new Point(x + s, y + height - 1));
            collisionborder.add(new Point(x - s, y + height - 1));
        }
        for (int j = y; j < y + height; j++) {
            border.add(new Point(x, j)); // Left wall
            collisionborder.add(new Point(x, j));
            border.add(new Point(x + width - 1, j)); // Right wall
            collisionborder.add(new Point(x + width - 1, j));
        }
        for (int k = 1; k < 4; k++) {
            collisionborder.add(new Point(x, y + k));
            collisionborder.add(new Point(x, y - k));
            collisionborder.add(new Point(x + width - 1, y + k));
            collisionborder.add(new Point(x + width - 1, y - k));
        }
    }

    public void placeRoom(World world, TETile wall, TETile floor) {
        TETile[][] tiles = world.world; //if i change tiles will it change world.world too? I want it to
        //placing the floor of the room
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                tiles[i][j] = floor;
            }
        }

        //placing the walls
        for (int i = x; i < x + width; i++) {
            tiles[i][y] = wall; // Bottom wall
            tiles[i][y + height - 1] = wall; // Top wall
        }
        for (int j = y; j < y + height; j++) {
            tiles[x][j] = wall; // Left wall
            tiles[x + width - 1][j] = wall; // Right wall
        }
    }


    public boolean doRoomsCollide(Room otherroom) {
        for (Point i : this.border) {
            for (Point j : otherroom.border) {
                if (i.compareTo(j) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void putDoors(TETile[][] tiles) {
        for (Point p : border) {
            int numwalls = 0;
            if (tiles[p.x][p.y].equals(Tileset.FLOOR)) {
                if ((tiles[p.x + 1][p.y].equals(Tileset.WALL))) {
                    numwalls++;
                }
                if ((tiles[p.x][p.y + 1].equals(Tileset.WALL))) {
                    numwalls++;
                }
                if ((tiles[p.x][p.y - 1].equals(Tileset.WALL))) {
                    numwalls++;
                }
                if ((tiles[p.x - 1][p.y].equals(Tileset.WALL))) {
                    numwalls++;
                }
            }
            if (numwalls >= 2) {
                tiles[p.x][p.y] = Tileset.UNLOCKED_DOOR;
            }
        }
    }



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getCenter() {
        return center;
    }
}
