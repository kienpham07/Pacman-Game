import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {

    enum GhostState {
        NORMAL,
        SCARED,
        RESPAWNING
    }

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;
        Image normalImage;
        GhostState ghostState = GhostState.NORMAL;

        int startX;
        int startY;

        char direction = 'U'; // U = up, D = down, L = left, R = right
        int velocityX = 0;
        int velocityY = 0;

        Block(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
            this.normalImage = image;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();

            // check wall collisions
            this.x += this.velocityX;
            this.y += this.velocityY;

            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {
            if (this.direction == 'U') {
                this.velocityX = 0;
                this.velocityY = -tileSize / 4;
            }
            else if (this.direction == 'D') {
                this.velocityX = 0;
                this.velocityY = tileSize / 4;
            }
            else if (this.direction == 'L') {
                this.velocityX = -tileSize / 4;
                this.velocityY = 0;
            }
            else if (this.direction == 'R') {
                this.velocityX = tileSize / 4;
                this.velocityY = 0;
            }
        }

        void reset() {
            this.x = startX;
            this.y = startY;
        }
    }

    private int rowNum = 23;
    private int colNum = 19;
    private int tileSize = 32;
    private int boardWidth = colNum * tileSize;
    private int boardHeight = rowNum * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image pinkGhostImage;
    private Image orangeGhostImage;
    private Image redGhostImage;
    private Image redCherryImage;
    private Image scaredGhostImage;

    public Image pacmanUpImage;
    public Image pacmanDownImage;
    public Image pacmanLeftImage;
    public Image pacmanRightImage;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    //X = wall, O = skip, P = pac man, ' ' = food, '-' = empty, 'C' = cherry
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "-------------------",
            "-------------------",
            "XXXXXXXXXXXXXXXXXXX",
            "X   c    X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X  c X",
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
            "X    X   X   X  c X",
            "X XXXXXX X XXXXXX X",
            "X  c              X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this); // Listen for key press and process that event with functions below
        setFocusable(true); // Allow this panel to receive keyboard focus

        // load images
        wallImage = new ImageIcon(getClass().getResource("img/wall.png")).getImage();
        redCherryImage = new ImageIcon(getClass().getResource("img/cherry.png")).getImage();

        blueGhostImage = new ImageIcon(getClass().getResource("img/blueGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("img/pinkGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("img/orangeGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("img/redGhost.png")).getImage();
        scaredGhostImage = new ImageIcon(getClass().getResource("img/scaredGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("img/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("img/pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("img/pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("img/pacmanRight.png")).getImage();

        // load map
        loadMap();

        // give ghosts random directions to move when first load map
        for (Block ghost : ghosts) {
            ghost.updateDirection(directions[random.nextInt(4)]);
        }

        // Creates a timer that runs every 50 ms and calls this PacMan object's actionPerformed - 20fps (1000 / 50)
        gameLoop = new Timer(50, this); // This refers to PacMan object, which is the listener for the timer and ActionListner
        gameLoop.start(); // Starts the game loop timer
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
                    Block ghost = new Block(x, y, tileSize, tileSize, orangeGhostImage);
                    ghosts.add(ghost);
                }
                else if (tile == 'p') { // pink ghost
                    Block ghost = new Block(x, y, tileSize, tileSize, pinkGhostImage);
                    ghosts.add(ghost);
                }
                else if (tile == 'r') { // red ghost
                    Block ghost = new Block(x, y, tileSize, tileSize, redGhostImage);
                    ghosts.add(ghost);
                }
                else if (tile == 'P') { // pacman
                    pacman = new Block(x, y, tileSize, tileSize, pacmanRightImage);
                }
                else if (tile == ' ') { // food (4x4) inside a 32x32 pixel block
                    Block food = new Block(x+14, y+14, 4, 4, null);
                    foods.add(food);
                }
                else if (tile == 'c') {
                    Block cherry = new Block(x, y, tileSize, tileSize, redCherryImage);
                    foods.add(cherry);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw (Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block food : foods) {
            if (food.image == redCherryImage) {
                g.drawImage(food.image, food.x, food.y, food.width, food.height, null);
            }
            else {
                g.setColor(Color.WHITE);
                g.fillRect(food.x, food.y, food.width, food.height);
            }
        }

        // color
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), boardWidth / 2 - 5 * tileSize - 10, tileSize + 10); // Location of text
            g.drawString("Press R to reset", boardWidth / 2 + 1 * tileSize, tileSize + 10); // Location of text
        }
        else {
            g.drawString("Lives: " + String.valueOf(lives), boardWidth / 2 - 4 * tileSize, tileSize + 10);
            g.drawString("Score: " + String.valueOf(score), boardWidth / 2 + tileSize + 10, tileSize + 10);
        }

    }

    public void move() {

        // -------- Pacman movement ----------
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // check pacman collisions with wall
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        // Check the pacman if it's out of the border
        handleTunnelTeleport(pacman);

        // -------- Ghost movement ----------
        // check ghost collisions
        for (Block ghost : ghosts) {

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            for (Block wall : walls) {
                if (collision(ghost, wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    ghost.updateDirection(directions[random.nextInt(4)]);
                    break;
                }
            }

            // Check the ghost if it's out of the border
             handleTunnelTeleport(ghost);

            // Force ghosts to move up when they on the 9th row
            if (ghost.y == tileSize * 11 && ghost.direction != 'U' && ghost.direction != 'D') {
                ghost.updateDirection('U');
            }

            if (collision(pacman, ghost)) {
                if (ghost.ghostState == GhostState.SCARED) {
                    eatGhost(ghost);
                }
                else if (ghost.ghostState == GhostState.NORMAL) {
                    loseLifeAndResetRound();
                    return;
                }
            }
        }

        // -------- Food movement ----------
        for (Block food : foods) {
            if (collision(pacman, food)) {
                if (food.image == redCherryImage) {
                    score += 50;
                    scaredGhostScene();
                }
                else {
                    score += 10;
                }
                foods.remove(food);
                break;
            }
        }

        if (foods.isEmpty()) {
           loadMap();
           resetAllPosition();
        }
    }

    public boolean collision (Block a, Block b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y; // Special Formula
    }

    public void handleTunnelTeleport(Block a) { // Tunnel is on the 9th row
        if (a.x + a.width <= 0) {
            a.x = boardWidth;
        } else if (a.x > boardWidth) {a.x = 0;}
    }

    public void resetAllPosition() {
        if (scaredTimer != null) {
            scaredTimer.stop();
        }

        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;

        for (Block ghost : ghosts) {
            ghost.reset();
            ghost.ghostState = GhostState.NORMAL;
            ghost.image = ghost.normalImage;
            ghost.updateDirection(directions[random.nextInt(4)]);
        }
    }

    Timer scaredTimer;

    public void scaredGhostScene() {
        for (Block ghost : ghosts) {
            if (ghost.ghostState == GhostState.NORMAL) {
                ghost.ghostState = GhostState.SCARED;
                ghost.image = scaredGhostImage;
            }
        }

        if (scaredTimer != null && scaredTimer.isRunning()) {
            scaredTimer.stop();
        }

        scaredTimer = new Timer(10000, e -> {
            for (Block ghost : ghosts) {
                if (ghost.ghostState == GhostState.SCARED) {
                    ghost.ghostState = GhostState.NORMAL;
                    ghost.image = ghost.normalImage;
                }
            }
            repaint();
        });

        scaredTimer.setRepeats(false);
        scaredTimer.start();
        repaint();
    }

    private void eatGhost(Block ghost) {
        ghost.ghostState = GhostState.RESPAWNING;
        ghost.reset();
        ghost.image = ghost.normalImage;
        ghost.ghostState = GhostState.NORMAL;
        ghost.updateDirection(directions[random.nextInt(4)]);
    }

    private void loseLifeAndResetRound() {
        lives--;
        if (lives == 0) {
            gameOver = true;
            return;
        }
        resetAllPosition();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint(); // Redraws the game screen
        /*
           Timer starts
            → every 100 ms, actionPerformed runs
            → actionPerformed calls repaint()
            → repaint causes paintComponent()
            → paintComponent redraws the game
         */
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            loadMap();
            resetAllPosition();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }

        // System.out.println("Key released: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }

        if (pacman.direction == 'U') {
            pacman.image = pacmanUpImage;
        }
        else if (pacman.direction == 'D') {
            pacman.image = pacmanDownImage;
        }
        else if (pacman.direction == 'L') {
            pacman.image = pacmanLeftImage;
        }
        else if (pacman.direction == 'R') {
            pacman.image = pacmanRightImage;
        }
    }
}
