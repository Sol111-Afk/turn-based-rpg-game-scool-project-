package javatemajoc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Serializable {

    private List<Skill> skills; 

    public Inventory() {
        this.skills = new ArrayList<>();
    }
    
    public void addSkill(Skill skill) {
        this.skills.add(skill);
    }
    
    public boolean removeSkill(Skill skill) {
        return this.skills.remove(skill);
    }
    
    public List<Skill> getSkills() {
        return skills;
    }
    
    public Skill getSkillByName(String name) {
        for (Skill skill : skills) {
            if (skill.getName().equalsIgnoreCase(name)) {
                return skill;
            }
        }
        return null;
    }
}