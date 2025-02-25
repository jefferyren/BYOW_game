package core;

//import edu.princeton.cs.algs4.StdDraw;
//import tileengine.TERenderer;
import edu.princeton.cs.algs4.StdDraw;
//import net.sf.saxon.expr.Component;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

//import java.sql.Array;
import java.awt.*;
import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static edu.princeton.cs.algs4.StdDraw.*;

//import static edu.princeton.cs.algs4.StdDraw.setPenColor;
//import static edu.princeton.cs.algs4.StdDraw.text;

public class World {
    private static final String SAVE_FILE = "save.txt";
    public static final int WIDTH =  50;
    public static final int HEIGHT =  50;
    public static final int SIXTEEN = 16;
    public static final int TWOHUNDREDFIFTYFIVE = 255;
    public static final int TWENTYFIVE = 25;
    public static final int TWENTY = 20;
    public static final int TWELVE = 12;
    public static final int THIRTY = 30;
    public static final int FORTYNINE = 49;
    boolean checkQuit = true;
    TETile[][] world;
    long seed = 0;
    String tiletype = "";
    private Random random;
    private Movement m;
    private Movement m2;
    private int numflowersleft;
    private int p1numflowerscollected;
    private int p2numflowerscollected;
    private char menuoption;
    private boolean checkq = false;
    private final TERenderer ter = new TERenderer();
    public World() {
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        numflowersleft = 0;
        p1numflowerscollected = 0;
        p2numflowerscollected = 0;
    }

    public World(long seed) {
        world = new TETile[WIDTH][HEIGHT];
        this.seed = seed;
        random = new Random(seed);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        numflowersleft = 0;
        p1numflowerscollected = 0;
        p2numflowerscollected = 0;
    }

    public TETile[][] boardNoRender(String autograderinput) {
        char first = autograderinput.charAt(0);
        String tempseed = "";
        int charplacer = 1;
        if (first == 'N' || first == 'n') {
            for (int w = 1; w < autograderinput.length(); w++) {
                if (autograderinput.charAt(w) == 'S' || autograderinput.charAt(w) == 's') {
                    charplacer++;
                    break;
                }
                charplacer++;
                tempseed += autograderinput.charAt(w);
            }
            this.seed = Long.parseLong(tempseed);
            createBoard();
        } else if (first == 'L' || first == 'l') {
            loadBoard(SAVE_FILE);
        } else {
            return this.world;
        }
        while (charplacer < autograderinput.length()) {
            if (autograderinput.charAt(charplacer) == 'A' || autograderinput.charAt(charplacer) == 'a') {
                m.moveLeft();
                charplacer++;
            } else if (autograderinput.charAt(charplacer) == 'D' || autograderinput.charAt(charplacer) == 'd') {
                m.moveRight();
                charplacer++;
            } else if (autograderinput.charAt(charplacer) == 'W' || autograderinput.charAt(charplacer) == 'w') {
                m.moveUp();
                charplacer++;
            } else if (autograderinput.charAt(charplacer) == 'S' || autograderinput.charAt(charplacer) == 's') {
                m.moveDown();
                charplacer++;
            } else if (autograderinput.charAt(charplacer) == ':') {
                charplacer++;
                if (autograderinput.charAt(charplacer) == 'Q' || autograderinput.charAt(charplacer) == 'q') {
                    charplacer++;
                    saveBoard();
                }
            }
        }
        return this.world;

    }

    private Point pointRandomizer() {
        int xCord = randomFromRange(5, 5 * 9);
        int yCord = randomFromRange(5, 5 * 9);
        while (!world[xCord][yCord].equals(Tileset.FLOOR)) {
            xCord = randomFromRange(5, 5 * 9);
            yCord = randomFromRange(5, 5 * 9);
        }
        Point p = new Point(xCord, yCord);
        return p;
    }

    public void loadBoard(String filename) {
        String silly = FileUtils.readFile(filename);
        String[] s = silly.split("\n");
        String[] size = s[0].split(" ");
        p1numflowerscollected = Integer.parseInt(size[0]);
        p2numflowerscollected = Integer.parseInt(size[1]);

        TETile[][] board = new TETile[WIDTH][HEIGHT];
        int height = HEIGHT;
        int width = WIDTH;
        Point temp1point = new Point(0, 0);
        Point temp2point = new Point(0, 0);
        for (int r = 1; r < height + 1; r++) {
            for (int c = 0; c < width; c++) {
                if (s[r].charAt(c) == '0') {
                    board[c][height - r] = Tileset.NOTHING;
                } else if (s[r].charAt(c) == '1') {
                    board[c][height - r] = Tileset.FLOOR;
                } else if (s[r].charAt(c) == '2') {
                    board[c][height - r] = Tileset.WALL;
                } else if (s[r].charAt(c) == '3') {
                    board[c][height - r] = Tileset.FLOWER;
                    numflowersleft++;
                } else if (s[r].charAt(c) == '4') {
                    board[c][height - r] = Tileset.MOUNTAIN;
                    temp1point = new Point(c, height - r);
                } else if (s[r].charAt(c) == '5') {
                    board[c][height - r] = Tileset.TREE;
                    temp2point = new Point(c, height - r);
                }
            }
        }
        world = board;
        m = new Movement(this, Tileset.MOUNTAIN, temp1point);
        m2 = new Movement(this, Tileset.TREE, temp2point);
    }

    public void saveBoard() {
        String silly = p1numflowerscollected + " " + p2numflowerscollected + "\n";
        int height = HEIGHT;
        int width = WIDTH;
        for (int r = height - 1; r >= 0; r--) {
            for (int c = 0; c < width; c++) {
                if (world[c][r].equals(Tileset.NOTHING)) {
                    silly += "0";
                } else if  (world[c][r].equals(Tileset.FLOOR)) {
                    silly += "1";
                } else if  (world[c][r].equals(Tileset.WALL)) {
                    silly += "2";
                } else if  (world[c][r].equals(Tileset.FLOWER)) {
                    silly += "3";
                } else if  (world[c][r].equals(Tileset.MOUNTAIN)) {
                    silly += "4";
                } else if  (world[c][r].equals(Tileset.TREE)) {
                    silly += "5";
                }
            }
            silly += "\n";
        }
        FileUtils.writeFile(SAVE_FILE, silly);
    }

    private void updateBoard() {
        double mx = mouseX();
        double my = mouseY();
        if (mx == 5 * 9 + 5 || my == 5 * 9 + 5 || mx == 0 || my == 0) {
            return;
        } else if (world[(int) mx][(int) my].equals(Tileset.FLOOR)) {
            tiletype = "Floor";
        } else if (world[(int) mx][(int) my].equals(Tileset.NOTHING)) {
            tiletype = "Nothing";
        } else if (world[(int) mx][(int) my].equals(Tileset.WALL)) {
            tiletype = "Wall";
        } else if (world[(int) mx][(int) my].equals(Tileset.MOUNTAIN)) {
            tiletype = "Player 1";
        } else if (world[(int) mx][(int) my].equals(Tileset.FLOWER)) {
            tiletype = "Flower";
        } else if (world[(int) mx][(int) my].equals(Tileset.TREE)) {
            tiletype = "Player 2";
        }
        while (hasNextKeyTyped()) {
            char temp = nextKeyTyped();

            if (temp == 'a') {
                m.moveLeft();
            } else if (temp == 'j') {
                m2.moveLeft();
            } else if (temp == 'd') {
                m.moveRight();
            } else if (temp == 'l') {
                m2.moveRight();
            } else if (temp == 'w') {
                m.moveUp();
            } else if (temp == 'i') {
                m2.moveUp();
            } else if (temp == 's') {
                m.moveDown();
            } else if (temp == 'k') {
                m2.moveDown();
            }
            temp = Character.toLowerCase(temp);
            if (checkq) {
                if (temp == 'q') {
                    checkQuit = false;
                    saveBoard();
                }
                checkq = false;
            }
            if (temp == ':') {
                checkq = true;
            }
        }
    }


    public List<Hallway> connectRooms(List<Room> rooms) {
        List<Hallway> hallways = new ArrayList<>();

        int min = 0;
        int max = rooms.size() - 1;
        int randomRoom = randomFromRange(min, max);

        for (int i = 0; i < rooms.size() - 1; i++) {
            Room a = rooms.get(i);
            Room b = rooms.get(i + 1);

            hallways.add(planHallway(a, b));
        }
        return hallways;
    }
    public List<Room> generateRandomRooms() {
        List<Room> rooms = new ArrayList<>();
        final int min = 15;
        final int max = 20;

        int numRooms = randomFromRange(min, max);
        for (int i = 0; i < numRooms; i++) {
            rooms.add(generateOneRoom());
        }
        return cleanRoomList(rooms);
    }

    public List<Room> cleanRoomList(List<Room> rooms) {
        List<Room> returnroomlist = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            boolean roomcollision = false;
            for (int j = i + 1; j < rooms.size(); j++) {
                roomcollision = rooms.get(i).doRoomsCollide(rooms.get(j));
                if (roomcollision) {
                    break;
                }
            }
            if (roomcollision) {
                continue;
            }
            returnroomlist.add(rooms.get(i));
        }
        return returnroomlist;
    }

    public Room generateOneRoom() {
        final int min = 5;
        final int max = 12;

        int width = randomFromRange(min, max);
        int height = randomFromRange(min, max);

        int x = randomFromRange(1, WIDTH - width);
        int y = randomFromRange(1, HEIGHT - height);

        return new Room(x, y, width, height);
    }

    public Hallway planHallway(Room a, Room b) {
        Hallway h = new Hallway();
        Point start = a.center;
        Point end = b.center;

        if (start.x < end.x) {
            for (int i = start.x; i <= end.x; i++) {
                h.addPoint(i, start.y);
            }
            if (start.y < end.y) {
                for (int i = start.y; i <= end.y; i++) {
                    h.addPoint(end.x, i);
                }
            } else {
                for (int i = start.y; i >= end.y; i--) {
                    h.addPoint(end.x, i);
                }
            }
        } else {
            for (int i = start.x; i >= end.x; i--) {
                h.addPoint(i, start.y);
            }
            if (start.y < end.y) {
                for (int i = start.y; i <= end.y; i++) {
                    h.addPoint(end.x, i);
                }
            } else {
                for (int i = start.y; i >= end.y; i--) {
                    h.addPoint(end.x, i);
                }
            }
        }
        return h;
    }

    public int randomFromRange(int min, int max) {
        int range = max - min;
        return (int) (random.nextDouble() * range) + min;
    }

    //    private void startUpScreen() {
    //        TERenderer ter = new TERenderer();
    //        ter.initialize(WIDTH, HEIGHT);
    //        ter.renderFrame(this.world);
    //        StdDraw.setPenColor(255, 255, 255);
    //        StdDraw.text(25, 25, String.valueOf(7836));
    //    }

    private void placeFlowers() {
        int numCoins = randomFromRange(WIDTH, 2 * WIDTH);
        for (int coin = 0; coin < numCoins; coin++) {
            int xcoor = randomFromRange(0, HEIGHT);
            int ycoor = randomFromRange(0, HEIGHT);
            if (world[xcoor][ycoor].equals(Tileset.FLOOR)) {
                world[xcoor][ycoor] = Tileset.FLOWER;
                numflowersleft++;
            }
        }
        m = new Movement(this, Tileset.MOUNTAIN, pointRandomizer());
        m2 = new Movement(this, Tileset.TREE, pointRandomizer());

        //StdDraw.show();
    }

    private void initializePen() {
        StdDraw.setCanvasSize(WIDTH * SIXTEEN, HEIGHT * SIXTEEN);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        //StdDraw.show();
    }
    public void runTitleScreen() {
        initializePen();
        StdDraw.setPenColor(TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE);
        StdDraw.text(TWENTYFIVE, TWENTYFIVE, "Welcome to CS61B: The Game");
        StdDraw.text(TWENTYFIVE, TWENTY, "Press N to start new game");
        StdDraw.text(TWENTYFIVE, SIXTEEN - 1, "Press L to load previous game");
        StdDraw.text(TWENTYFIVE, 5 * 2, "Press Q to quit");
        StdDraw.show();
    }

    public void runEndScreen() {
        initializePen();
        StdDraw.setPenColor(TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE);
        if (numflowersleft != 0) {
            StdDraw.text(TWENTYFIVE, TWENTYFIVE, "Game Saved.");
            StdDraw.show();
            return;
        }
        StdDraw.text(TWENTYFIVE, TWENTYFIVE, "Game Over.");
        if (p1numflowerscollected > p2numflowerscollected) {
            StdDraw.text(TWENTYFIVE, TWENTY, "Player 1 wins!");
            StdDraw.text(TWENTYFIVE, SIXTEEN - 1, "Player 1 points: " + p1numflowerscollected);
            StdDraw.text(TWENTYFIVE, TWELVE, "Player 2 points: " + p2numflowerscollected);
        } else if (p1numflowerscollected < p2numflowerscollected) {
            StdDraw.text(TWENTYFIVE, TWENTY, "Player 2 wins!");
            StdDraw.text(TWENTYFIVE, SIXTEEN - 1, "Player 1 points: " + p1numflowerscollected);
            StdDraw.text(TWENTYFIVE, TWELVE, "Player 2 points: " + p2numflowerscollected);
        } else {
            StdDraw.text(TWENTYFIVE, TWENTY, "It's a tie!");
            StdDraw.text(TWENTYFIVE, SIXTEEN - 1, "Player 1 points: " + p1numflowerscollected);
            StdDraw.text(TWENTYFIVE, TWELVE, "Player 2 points: " + p2numflowerscollected);
        }
        StdDraw.show();
    }

    public boolean optionmenu() {
        if (StdDraw.hasNextKeyTyped()) {
            char s = StdDraw.nextKeyTyped();
            if (s == 'n' || s == 'N') {
                menuoption = 'n';
            } else if (s == 'l' || s == 'L') {
                menuoption = 'l';
            } else if (s == 'q' || s == 'Q') {
                menuoption = 'q';
            }
            return false;
        }
        return true;
    }

    public void createBoard() {
        random = new Random(this.seed);
        List<Room> rooms = this.generateRandomRooms();
        for (Room r : rooms) {
            r.placeRoom(this, Tileset.WALL, Tileset.FLOOR);
            this.world[r.center.x][r.center.y] = Tileset.MOUNTAIN;
        }

        List<Hallway> hallways = this.connectRooms(rooms);
        for (Hallway h : hallways) {
            h.placeHallway(this, Tileset.WALL, Tileset.FLOOR);
        }
        placeFlowers();
    }

    private void renderBoard() {
        ter.drawTiles(world);
        renderFlowers();
        StdDraw.show();
    }

    private void renderFlowers() {
        StdDraw.setPenColor(TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE);
        StdDraw.text(TWENTYFIVE + 5, FORTYNINE, "Player 1: " + p1numflowerscollected
                + "          Player 2: " + p2numflowerscollected);
        StdDraw.text(5 * 2, FORTYNINE, "Current tile: " + tiletype);
    }
    private void runSeedScreen() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(WHITE);
        StdDraw.text(TWENTYFIVE, TWENTYFIVE, "Seed: " + this.seed);
        StdDraw.show();
    }
    public boolean enterSeed() {
        if (StdDraw.hasNextKeyTyped()) {
            char s = StdDraw.nextKeyTyped();
            if (s != 's' && s != 'S') {
                this.seed = this.seed * 5 * 2 + Integer.parseInt(String.valueOf(s));
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean checkQuit() {
        if (hasNextKeyTyped()) {
            char temp = Character.toLowerCase(nextKeyTyped());
            if (checkq) {
                if (temp == 'q') {
                    return true;
                }
                checkq = false;
            }
            if (temp == ':') {
                checkq = true;
            }
        }
        return false;
    }

    public void saveWorld() {

    }
    public void runGame() {
        runTitleScreen();
        boolean x = optionmenu();
        while (x) {
            x = optionmenu();
        }
        if (menuoption == 'n') {
            boolean y = enterSeed();
            runSeedScreen();
            long seedholder = 0;
            while (y) {
                y = enterSeed();
                if (seedholder < this.seed) {
                    runSeedScreen();
                    seedholder = this.seed;
                }
            }
            createBoard();
        } else if (menuoption == 'q') {
            initializePen();
            StdDraw.setPenColor(TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE);
            StdDraw.text(TWENTYFIVE, TWENTYFIVE, "Game terminated");
            StdDraw.show();
            return;
        } else if (menuoption == 'l') {
            loadBoard(SAVE_FILE);
        }

        while (numflowersleft != 0 && this.checkQuit) {
            updateBoard();
            renderBoard();
        }
        runEndScreen();
    }

    public void incrementFlower(Movement p) {
        if (p.equals(m)) {
            p1numflowerscollected++;
        } else {
            p2numflowerscollected++;
        }
        numflowersleft--;
    }

    public static void main(String[] args) {
        World w = new World();
        w.runGame();
    }
}
