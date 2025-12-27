package javatemajoc;
import java.io.Serializable;
import java.lang.Math;

public abstract class GameEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    protected String name;
    protected int maxHP;
    protected int currentHP;
    protected int attack;
    protected int defense;
    protected int speed;
    
    public GameEntity(String name, int maxHP, int attack, int defense, int speed) {
        this.name = name;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }
    
    public abstract void takeDamage(int rawDamage);

    public abstract void performAction(GameEntity target);
    
    public boolean isAlive() {
        return this.currentHP > 0;
    }
    
    protected int calculateDamage(GameEntity target) {
        int damageDealt = Math.max(1, this.attack - target.getDefense());
        return damageDealt;
    }
    
    public String getName() { return name; }
    public int getCurrentHP() { return currentHP; }
    public int getMaxHP() { return maxHP; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getSpeed() { return speed; }
    
    public void setCurrentHP(int currentHP) { 
        this.currentHP = Math.min(currentHP, this.maxHP); 
        this.currentHP = Math.max(0, this.currentHP);
    }
}