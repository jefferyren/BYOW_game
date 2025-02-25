package core;

import tileengine.TETile;
import tileengine.Tileset;


public class Movement {
    TETile[][] board;
    World w;
    TETile player;
    Point currPos;
    public Movement(World w, TETile p, Point cp) {
        this.w = w;
        this.board = w.world;
        this.player = p;
        this.currPos = cp;
        board[currPos.x][currPos.y] = this.player;
    }
    public void moveLeft() {
        int x = currPos.x;
        int y = currPos.y;

        if (board[x - 1][y].equals(Tileset.WALL)) {
            return;
        }

        if (board[x - 1][y].equals(Tileset.FLOWER)) {
            w.incrementFlower(this);
        }
        board[x][y] = Tileset.FLOOR;
        board[x - 1][y] = this.player;
        currPos.x--;
    }
    public void moveRight() {
        int x = currPos.x;
        int y = currPos.y;
        if (board[x + 1][y].equals(Tileset.WALL)) {
            return;
        }

        if (board[x + 1][y].equals(Tileset.FLOWER)) {
            w.incrementFlower(this);
        }
        board[x][y] = Tileset.FLOOR;
        board[x + 1][y] = this.player;
        currPos.x++;
    }
    public void moveUp() {
        int x = currPos.x;
        int y = currPos.y;

        if (board[x][y + 1].equals(Tileset.WALL)) {
            return;
        }

        if (board[x][y + 1].equals(Tileset.FLOWER)) {
            w.incrementFlower(this);
        }
        board[x][y] = Tileset.FLOOR;
        board[x][y + 1] = this.player;
        currPos.y++;
    }
    public void moveDown() {
        int x = currPos.x;
        int y = currPos.y;

        if (board[x][y - 1].equals(Tileset.WALL)) {
            return;
        }

        if (board[x][y - 1].equals(Tileset.FLOWER)) {
            w.incrementFlower(this);
        }
        board[x][y] = Tileset.FLOOR;
        board[x][y - 1] = this.player;
        currPos.y--;
    }
}
