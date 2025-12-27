package javatemajoc;

import java.io.Serializable;

public class Skill implements Serializable {
    
    private String name;
    private double damageModifier;
    private int cost;

    public Skill(String name, double damageModifier, int cost) {
        this.name = name;
        this.damageModifier = damageModifier;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getDamageModifier() {
        return damageModifier;
    }

    public int getCost() {
        return cost;
    }
}