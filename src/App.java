import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        int rowNum = 23;
        int colNum = 19;
        int tileSize = 32;
        int boardWidth = colNum * tileSize;
        int boardHeight = rowNum * tileSize;

        JFrame frame = new JFrame("Pac Man");
        /*frame.setVisible(true)*/;
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); // Center the window in the screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PacMan pacManGame = new PacMan();
        frame.add(pacManGame);
        frame.pack();
        pacManGame.requestFocus(); // Give keyboard focus to the PacMan game panel
        frame.setVisible(true);

    }
}