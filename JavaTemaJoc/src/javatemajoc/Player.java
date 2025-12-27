package javatemajoc;

import java.lang.Math;

public class Player extends GameEntity {

    private int experience;
    private int gold;
    private int level;
    private int xpToNextLevel;
    
    // --- SISTEM NOU DE MANA ---
    private int currentMana;
    private int maxMana;
    // --------------------------
    
    private Inventory inventory; 

    // Variabile animatie
    private int mapX, mapY;       
    private int pixelX, pixelY;   
    private boolean moving = false; 
    private int targetPixelX, targetPixelY; 
    private int speed = 4;        

    public Player(String name, int maxHP, int attack, int defense, int speedStat) {
        super(name, maxHP, attack, defense, speedStat); 
        
        this.experience = 0;
        this.gold = 0;
        this.level = 1;
        this.xpToNextLevel = 100;
        
        // --- INITIALIZARE MANA ---
        this.maxMana = 50;         // Incepem cu 50 Mana
        this.currentMana = 50;
        // -------------------------
        
        this.inventory = new Inventory();
        
        this.mapX = 5;
        this.mapY = 5;
        this.pixelX = mapX * 0; 
        this.pixelY = mapY * 0;
    }

    public void gainLoot(int xp, int gold) {
        this.experience += xp;
        this.gold += gold;
    }

    public boolean canLevelUp() {
        return this.experience >= this.xpToNextLevel;
    }

    public void applyLevelUp(String bonusType) {
        this.level++;
        this.experience -= this.xpToNextLevel; 
        this.xpToNextLevel = (int) (this.xpToNextLevel * 1.5); 

        // Crestere automata mica la stats
        this.maxHP += 5;
        this.maxMana += 5; // Primesti si putina mana automat
        this.attack += 1;
        this.defense += 1;
        
        // BONUSURI LA ALEGERE
        switch (bonusType) {
            case "STRENGTH":
                this.attack += 4; 
                break;
            case "VITALITY":
                this.maxHP += 15; 
                break;
            case "DEFENSE":
                this.defense += 3; 
                break;
            case "INTELLIGENCE": // --- OPTIUNE NOUA ---
                this.maxMana += 15; 
                this.currentMana = this.maxMana; // Refill la level up
                break;
            case "WEALTH":
                this.gold += 100; 
                break;
        }

        // Vindecare completa la nivel nou
        this.currentHP = this.maxHP;
        this.currentMana = this.maxMana;
    }

    // --- METODE PENTRU MANA ---
    public int getCurrentMana() { return currentMana; }
    public int getMaxMana() { return maxMana; }
    
    public void setCurrentMana(int mana) {
        this.currentMana = mana;
        if (this.currentMana > this.maxMana) this.currentMana = this.maxMana;
        if (this.currentMana < 0) this.currentMana = 0;
    }
    
    public void restoreMana(int amount) {
        this.currentMana += amount;
        if (this.currentMana > this.maxMana) this.currentMana = this.maxMana;
    }
    // --------------------------

    public void update() {
        if (moving) {
            if (pixelX < targetPixelX) { pixelX += speed; if (pixelX > targetPixelX) pixelX = targetPixelX; } 
            else if (pixelX > targetPixelX) { pixelX -= speed; if (pixelX < targetPixelX) pixelX = targetPixelX; }
            if (pixelY < targetPixelY) { pixelY += speed; if (pixelY > targetPixelY) pixelY = targetPixelY; } 
            else if (pixelY > targetPixelY) { pixelY -= speed; if (pixelY < targetPixelY) pixelY = targetPixelY; }
            if (pixelX == targetPixelX && pixelY == targetPixelY) { moving = false; }
        }
    }

    public void setTargetPosition(int newMapX, int newMapY, int tileSize) {
        this.mapX = newMapX;
        this.mapY = newMapY;
        this.targetPixelX = mapX * tileSize;
        this.targetPixelY = mapY * tileSize;
        this.moving = true;
    }
    
    public void snapToGrid(int tileSize) {
        this.pixelX = mapX * tileSize;
        this.pixelY = mapY * tileSize;
        this.targetPixelX = pixelX;
        this.targetPixelY = pixelY;
        this.moving = false;
    }

    public boolean isMoving() { return moving; }
    public int getPixelX() { return pixelX; }
    public int getPixelY() { return pixelY; }
    public int getX() { return mapX; }
    public int getY() { return mapY; }
    public int getCurrentXP() { return experience; }

    @Override
    public void takeDamage(int finalDamage) {
        this.currentHP -= finalDamage;
        if (this.currentHP < 0) this.currentHP = 0;
    }

    @Override
    public void performAction(GameEntity target) {}

    public int getExperience() { return experience; }
    public int getGold() { return gold; }
    public int getLevel() { return level; }
    public int getXpToNextLevel() { return xpToNextLevel; }
    public Inventory getInventory() { return inventory; }
    public void setExperience(int experience) { this.experience = experience; }
    public void setGold(int gold) { this.gold = Math.max(0, gold); }
    public void setLevel(int level) { this.level = level; }
    public void setXpToNextLevel(int xpToNextLevel) { this.xpToNextLevel = xpToNextLevel; }
}