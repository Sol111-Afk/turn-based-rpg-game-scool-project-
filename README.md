⚔️ Java RPG Engine - University Project (OOP)
This is a 2D Role-Playing Game (RPG) developed as a final project for the Object-Oriented Programming (OOP) course. The application demonstrates the practical application of software design patterns, state management, and graphical user interface (GUI) development using Java.

🎓 Academic Context
This project was designed to showcase fundamental OOP principles:

Encapsulation & Inheritance: Implementing a hierarchy where Player and Enemy inherit core attributes from an abstract GameEntity class.

Polymorphism: Overriding methods such as takeDamage and performAction to allow different behaviors for various entities.

Interface Implementation: Using the ISavable interface to standardize game state serialization for saving and loading progress.

Design Patterns: Implementation of the Singleton pattern in the GameEngine class to ensure a single point of control for game logic.

Resource Management: Handling external assets (sprites, tiles, and backgrounds) through a dedicated ImageLoader with transparency processing.

🚀 Key Features
Turn-Based Combat System: A robust battle engine featuring physical attacks, mana-based skills, and a "Flee" mechanic with success rates based on entity speed.

Thematic Enemies: Encounter classic RPG monsters like Goblins and Slimes, or the final "boss"—"The Bachelor's Thesis" (Lucrarea de Licență)—adding a humorous student-life twist to the gameplay.

Character Progression: Gain XP and Gold from victories to level up and manually distribute attribute points such as Strength, Vitality, Defense, or Intelligence.

Inventory & Skills: Manage a collection of spells like Fire Blast, Heal, and Rage, while tracking mana costs and player stats.

Data Persistence: Save and load functionality using Java Object Streams to preserve player progress across sessions.

🛠️ Technical Stack
Language: Java 8+

Graphics: Java Swing & AWT (Developed without external game libraries to demonstrate native GUI and Graphics2D handling).

Architecture: A decoupled architecture separating the business logic (GameEngine) from the graphical presentation layer (BattlePanel, InventoryPanel, StatsPanel).

🎮 How to Play
Exploration: Navigate the map to find treasure chests containing gold and experience.

Combat:

ATTACK: Standard physical damage based on Strength.

SKILL: Use powerful abilities by consuming Mana (e.g., healing or high-damage blasts).

FLEE: Attempt to escape the battle based on a speed check or random chance.

Leveling Up: Upon reaching an XP threshold, a menu appears allowing you to permanently boost your stats and fully restore your health and mana.

Hotkeys:

I or ESC: Open/Close the Student Inventory and Status screen.

Author: SOL

University: FILS (Faculty of Engineering in Foreign Languages)

Department: Computer Science in French

Course: Object-Oriented Programming
