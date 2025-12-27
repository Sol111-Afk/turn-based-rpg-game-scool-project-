package javatemajoc;

public class Enemy extends GameEntity {

    private int xpDrop;
    private int goldDrop;
    private int level; 

    public Enemy(String name, int maxHP, int attack, int defense, int speed, int xpDrop, int goldDrop) {
        super(name, maxHP, attack, defense, speed);
        this.xpDrop = xpDrop;
        this.goldDrop = goldDrop;
        this.level = 1; 
    }


    public int getLevel() {
        return level;
    }
   

    public int getXpDrop() {
        return xpDrop;
    }

    public int getGoldDrop() {
        return goldDrop;
    }

    @Override
    public void takeDamage(int amount) {
        this.currentHP -= amount;
        if (this.currentHP < 0) {
            this.currentHP = 0;
        }
    }

    @Override
    public void performAction(GameEntity target) {
       
    }
}