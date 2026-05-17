# Pac-Man Game

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)

A classic Pac-Man-style desktop game built with Java Swing, featuring tile-based movement, ghost collisions, collectible food, power-up cherries, score tracking, lives, and keyboard-controlled gameplay.

## Features

- **Classic Pac-Man Gameplay**: Move through a maze, collect food, avoid ghosts, and clear the board
- **Keyboard Controls**: Use the arrow keys to control Pac-Man's direction
- **Tile-Based Map System**: Builds the maze from a structured character map
- **Ghost Movement**: Multiple ghosts move through the maze with randomized directions
- **Collision Detection**: Handles walls, food, cherries, ghosts, and tunnel movement
- **Scoring System**: Earn points by collecting regular food and cherries
- **Lives System**: Start with three lives and lose one when Pac-Man hits a normal ghost
- **Power-Up Mode**: Cherries trigger a temporary scared-ghost state
- **Tunnel Teleporting**: Move through side tunnels to wrap around the board
- **Game Reset**: Press `R` after game over to restart with a fresh score and lives

## Tech Stack

- **Language**: Java
- **GUI Framework**: Java Swing
- **Graphics**: Java AWT
- **Game Loop**: Swing `Timer`
- **Input Handling**: Java `KeyListener`
- **Assets**: PNG sprites for Pac-Man, ghosts, walls, and cherries
- **No build tools**: Compiles and runs directly with the JDK

## Installation

### Clone and Run Locally

1. Clone the repository:

   ```bash
   git clone https://github.com/kienpham07/Pacman-Game.git
   cd Pacman-Game
   ```

2. Compile the Java source files:

   ```bash
   javac src/App.java src/PacMan.java
   ```

3. Run the game:

   ```bash
   java -cp src App
   ```

## Usage

1. Launch the game using the run command above
2. Use the arrow keys to move Pac-Man:
   - **Up Arrow**: Move up
   - **Down Arrow**: Move down
   - **Left Arrow**: Move left
   - **Right Arrow**: Move right
3. Collect food pellets to increase your score
4. Collect cherries to activate scared-ghost mode
5. Avoid normal ghosts or you will lose a life
6. Clear all food to reload the map and continue playing
7. When the game is over, press `R` to restart

## Project Structure

```text
Pacman-Game/
├── README.md              # Project documentation
├── Project 11 - PacMan game.iml
└── src/
    ├── App.java           # Application entry point and game window setup
    ├── PacMan.java        # Main game logic, rendering, input, and collisions
    └── img/
        ├── blueGhost.png
        ├── cherry.png
        ├── cherry2.png
        ├── orangeGhost.png
        ├── pacmanDown.png
        ├── pacmanLeft.png
        ├── pacmanRight.png
        ├── pacmanUp.png
        ├── pinkGhost.png
        ├── powerFood.png
        ├── redGhost.png
        ├── scaredGhost.png
        └── wall.png
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Dev: Kien Pham (kienpham07)
