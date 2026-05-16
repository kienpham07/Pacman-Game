import java.awt.*;
import java.util.HashSet;
import javax.swing.*;

public class PacMan extends JPanel {

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;

        Block(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
            this.startX = x;
            this.startY = y;
        }
    }

    private int rowNum = 21;
    private int colNum = 19;
    private int tileSize = 32;
    private int boardWidth = colNum * tileSize;
    private int boardHeight = rowNum * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image pinkGhostImage;
    private Image orangeGhostImage;
    private Image redGhostImage;

    public Image pacmanUpImage;
    public Image pacmanDownImage;
    public Image pacmanLeftImage;
    public Image pacmanRightImage;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);

        // load images
        wallImage = new ImageIcon(getClass().getResource("img/wall.png")).getImage();

        blueGhostImage = new ImageIcon(getClass().getResource("img/blueGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("img/pinkGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("img/orangeGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("img/redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("img/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("img/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("img/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("img/pacmanRight.png")).getImage();

        // load map
        loadMap();
        System.out.println(walls.size());
        System.out.println(foods.size());
        System.out.println(ghosts.size());

    }

    public void loadMap() {

        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int r = 0; r < rowNum; r++) {
            for (int c = 0; c < colNum; c++) {
                String row = tileMap[r];
                char tile = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if (tile == 'X') {
                    Block wall = new Block(x, y, tileSize, tileSize, wallImage);
                    walls.add(wall);
                }
                else if (tile == 'b') { // blue ghost
                    Block ghost = new Block(x, y, tileSize, tileSize, blueGhostImage);
                    ghosts.add(ghost);
                }
                else if (tile == 'o') { // orange ghost
                    Block ghost = new Block(x, y, tileSize, tileSize, blueGhostImage);
                    ghosts.add(ghost);
                }
                else if (tile == 'p') { // pink ghost
                    Block ghost = new Block(x, y, tileSize, tileSize, pacmanUpImage);
                    ghosts.add(ghost);
                }
                else if (tile == 'r') { // red ghost
                    Block ghost = new Block(x, y, tileSize, tileSize, redGhostImage);
                    ghosts.add(ghost);
                }
                else if (tile == 'P') { // pacman
                    pacman = new Block(x, y, tileSize, tileSize, pacmanRightImage);
                }
                else if (tile == ' ') { // food (4x4)
                    Block food = new Block(x+14, y+14, 4, 4, null);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
