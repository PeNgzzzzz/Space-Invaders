# Space-Invaders

## Introduction
This project is a Kotlin/JavaFX implementation of the classic Space Invaders game. The game features multiple levels, interactive controls, and various gameplay elements to keep the player engaged.

## Features

### Starting the Game
- **Title Screen**: A visually appealing title screen with the game's title, your name, and student number.
- **Instructions**: Clear and concise instructions on how to play and quit the game.
- **Start/Quit Options**: Interactive buttons or key bindings to start or quit the game.

### Gameplay Elements
- **Game Screen**: Consists of at least 10 columns and 5 rows of aliens, a player-controlled ship, score indicator, level indicator, and remaining ships indicator.
- **Aliens**: Move in a coordinated manner, descending and changing direction at screen edges.
- **Player Ship**: Controlled by the player, capable of moving left and right along the bottom of the screen.
- **Score Indicator**: Real-time score tracking.
- **Level Indicator**: Displays the current level.
- **Remaining Ships Indicator**: Shows the number of ships the player has left.

### The Aliens
- **Group Movement**: Aliens move as a group, initially towards the right and then descending and reversing direction at the screen edge.
- **Missile Firing**: Random chance of firing a missile at the player.
- **Speed Increase**: Speed increases as more aliens are destroyed.

### Game Progression
- **Multiple Levels**: At least three levels with increasing difficulty.
- **Ship Respawn**: If a player's ship is destroyed and extra ships remain, a new ship is spawned.
- **Win/Lose Messages**: Appropriate messages displayed upon game completion, with options to restart or quit.

### Controls
- **Movement**: 'A' and 'D' keys to move the ship left and right.
- **Firing**: SPACE key to fire missiles.

### Audio-Visual Elements
- **Graphics**: High-quality images for all game elements.
- **Sound Effects**: Engaging audio feedback for key game events like alien movement, missile firing, and alien destruction.

## Technical Requirements
- **Kotlin Version**: 1.8 or later
- **Java JDK**: 17
- **Java FX**: 18
- **Build Tool**: Gradle

## How to Run
1. Download the code.
2. Open it in any IDE you prefer.
3. Build the project using Gradle.
4. Run it!

## Testing

The application has been tested in a sandbox environment to ensure that file operations are safe and do not affect important system files. The test directory structure is included in the project.
