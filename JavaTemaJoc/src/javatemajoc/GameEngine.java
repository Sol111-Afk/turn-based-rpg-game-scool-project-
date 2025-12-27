package javatemajoc;

import java.io.*;
import java.util.Random;
import java.lang.Math;
import javax.swing.JOptionPane;

public class GameEngine implements ISavable {
    
    private Player player;
    private GameState currentState;
    private Enemy currentEnemy;      
    
    private static GameEngine instance;

    private BattlePanel battlePanelReference;
    private StatsPanel statsPanelReference;

    private GameEngine() {
        this.currentState = GameState.MAP;
        // Jucatorul incepe putin mai puternic
        this.player = new Player("Hero", 100, 20, 5, 20); 
        
        Skill fireblast = new Skill("Fire Blast", 2.5, 20); 
        Skill heal = new Skill("Heal", 0.0, 25);      
        Skill rage = new Skill("rage",20,20);
        
        player.getInventory().addSkill(rage);
        player.getInventory().addSkill(fireblast);
        player.getInventory().addSkill(heal);   
        
        player.setGold(50); 
    }
    
    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }
    
    public void setBattlePanelReference(BattlePanel panel) {
        this.battlePanelReference = panel;
    }

    public void setStatsPanelReference(StatsPanel panel) {
        this.statsPanelReference = panel;
    }
    
    private void updateGuiStats() {
        if (statsPanelReference != null) {
            statsPanelReference.updateStats();
        }
    }

    public void checkEncounter() {
        if (currentState == GameState.MAP) {
            Random random = new Random();
            
            // 15% sansa de lupta
            if (random.nextInt(100) < 50) { 
                
                int enemyType = random.nextInt(3); // 0, 1 sau 2
                Enemy newEnemy;
                
                if (enemyType == 0) {
                    // GOBLIN
                    newEnemy = new Enemy("Goblin", 40, 10, 3, 8, 40, 15);
                
                } else if (enemyType == 1) {
                    // PINK SLIME
                    newEnemy = new Enemy("Pink Slime", 70, 15, 5, 10, 80, 35);
                
                } else {
                    // BOSS: LUCRAREA DE LICENTA
                    newEnemy = new Enemy("Lucrarea de Licență", 200, 25, 10, 5, 300, 150);
                }

                startBattle(newEnemy);
            }
        }
    }
    
    public void openChest() {
        int xpReward = 100 + new Random().nextInt(101); 
        int goldReward = 100 + new Random().nextInt(151); 
        
        player.gainLoot(xpReward, goldReward);
        
        JOptionPane.showMessageDialog(null, 
            "You found a Treasure Chest!\nReceived: " + xpReward + " XP, " + goldReward + " Gold");
            
        if (player.canLevelUp()) {
            showLevelUpMenu();
        }
        
        updateGuiStats();
    }
    
    public void startBattle(Enemy enemy) {
        this.currentEnemy = enemy;
        this.currentState = GameState.BATTLE;
        
        if (battlePanelReference != null) {
            battlePanelReference.updateBattleDisplay();
        }
    }

    public void performPlayerTurn(ActionType action, Skill skill) {
        if (currentState != GameState.BATTLE || !player.isAlive()) return;

        String battleMessage = "";
        boolean turnEnded = true; 

        switch (action) {
            case ATTACK:
                int rawDamage = player.getAttack();
                int finalDamage = Math.max(1, rawDamage - currentEnemy.getDefense());
                currentEnemy.takeDamage(finalDamage);
                battleMessage = player.getName() + " attacks for " + finalDamage + " damage!";
                break;

            case USE_SKILL:
                if (skill != null) {
                    if (player.getCurrentMana() >= skill.getCost()) {
                        
                        player.setCurrentMana(player.getCurrentMana() - skill.getCost());
                        
                        if (skill.getName().equalsIgnoreCase("Heal")) {
                            int healAmount = 30 + (player.getLevel() * 8);
                            int oldHP = player.getCurrentHP();
                            player.setCurrentHP(player.getCurrentHP() + healAmount);
                            battleMessage = "Heal used! +" + (player.getCurrentHP() - oldHP) + " HP (-" + skill.getCost() + " Mana)";
                        } else {
                            int skillRawDamage = (int) (player.getAttack() * skill.getDamageModifier());
                            int skillFinalDamage = Math.max(1, skillRawDamage - currentEnemy.getDefense());
                            currentEnemy.takeDamage(skillFinalDamage);
                            battleMessage = skill.getName() + "! " + skillFinalDamage + " dmg (-" + skill.getCost() + " Mana)";
                        }
                    } else {
                        battleMessage = "Not enough Mana!";
                        turnEnded = false;
                    }
                }
                break;

            case FLEE:
                if (player.getSpeed() > currentEnemy.getSpeed() || new Random().nextInt(100) < 40) { 
                    String successMsg = "You managed to escape safely!";
                    if (battlePanelReference != null) {
                        battlePanelReference.showFleeSuccess(successMsg);
                    }
                    return; 
                } else {
                    battleMessage = "Failed to escape!";
                }
                break;
        }

        if (!turnEnded) return;

        updateGuiStats();

        if (battlePanelReference != null) {
            battlePanelReference.updateStatsBars();
            
            if (currentEnemy.isAlive()) {
                battlePanelReference.animatePlayerText(battleMessage);
            } else {
                endBattle(true);
            }
        }
    }

    // --- AICI AM ADAUGAT REPLICILE HAIOASE ---
    public void performEnemyTurn() {
        if (currentState != GameState.BATTLE || !currentEnemy.isAlive()) return;
        
        String enemyName = currentEnemy.getName();
        String attackFlavorText = "";
        Random rand = new Random();
        
        // 1. Calculam Damage-ul (Inamicul ataca inapoi)
        int rawDamage = currentEnemy.getAttack();
        int finalDamage = Math.max(1, rawDamage - player.getDefense());
        player.takeDamage(finalDamage);
        
        // 2. Alegem textul in functie de inamic
        if (enemyName.contains("Licență")) {
            // Replicile pentru Boss
            String[] bossMoves = {
                "<< te loveste cu o restanta din anu 1 >>",
                "<< fatala tre sa repeti materia >>",
                "<< profu' ti-a respins lucrarea >>",
                "<< eroare de compilare fatala >>",
                "<< ai uitat sa salvezi proiectul >>"
            };
            attackFlavorText = bossMoves[rand.nextInt(bossMoves.length)];
            
        } else if (enemyName.contains("Slime")) {
            // Replicile pentru Pink Slime (Girly)
            String[] slimeMoves = {
                "<< ataca cu dragostea fatala >>",
                "<< floarea de nu ma uita >>",
                "<< pupic lipicios >>",
                "<< te imbratiseaza cu sclipici >>",
                "<< atac cu inimioare >>"
            };
            attackFlavorText = slimeMoves[rand.nextInt(slimeMoves.length)];
            
        } else {
            // Replicile pentru Goblin (Standard)
            String[] goblinMoves = {
                "<< te loveste cu bata >>",
                "<< arunca cu piatra >>",
                "<< te musca de picior >>"
            };
            attackFlavorText = goblinMoves[rand.nextInt(goblinMoves.length)];
        }
        
        updateGuiStats();
        
        // Construim mesajul final: TEXT + DAMAGE
        String finalMessage = attackFlavorText + "\n( Ai pierdut " + finalDamage + " HP! )";
        
        if (battlePanelReference != null) {
            battlePanelReference.updateStatsBars();
            battlePanelReference.animateEnemyText(finalMessage);
        }

        if (!player.isAlive()) {
            endBattle(false);
        }
    }
    // -----------------------------------------

    public void endBattle(boolean victory) {
        if (victory) {
            player.gainLoot(currentEnemy.getXpDrop(), currentEnemy.getGoldDrop());
            
            int manaRegen = 10 + new Random().nextInt(11); 
            player.restoreMana(manaRegen);
            
            if (battlePanelReference != null) {
                battlePanelReference.showVictoryScreen(currentEnemy.getXpDrop(), currentEnemy.getGoldDrop());
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "GAME OVER! Ai fost invins de " + currentEnemy.getName() + ".");
            System.exit(0);
        }
    }
    
    public void finalizeBattleEnd() {
        this.currentState = GameState.MAP;
        this.currentEnemy = null;
        updateGuiStats();
        
        if (player.canLevelUp()) {
            showLevelUpMenu();
        }
    }
    
    private void showLevelUpMenu() {
        String[] options = {"Strength (+5 ATK)", "Vitality (+25 HP)", "Defense (+4 DEF)", "Intelligence (+20 Mana)"};
        
        int choice = JOptionPane.showOptionDialog(null, 
                "LEVEL UP! You reached Level " + (player.getLevel() + 1) + "!\nChoose a bonus attribute:", 
                "Level Up Bonus", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, 
                options, 
                options[0]);

        if (choice == 0) player.applyLevelUp("STRENGTH");
        else if (choice == 1) player.applyLevelUp("VITALITY");
        else if (choice == 2) player.applyLevelUp("DEFENSE");
        else if (choice == 3) player.applyLevelUp("INTELLIGENCE");
        else player.applyLevelUp("STRENGTH"); 

        updateGuiStats();
        JOptionPane.showMessageDialog(null, "Stats increased! HP & Mana fully restored.");
    }

    @Override
    public void save(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this.player); 
            oos.writeObject(this.currentState);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Player load(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            this.player = (Player) ois.readObject();
            this.currentState = (GameState) ois.readObject();
            updateGuiStats();
            return this.player;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Player getPlayer() { return player; }
    public GameState getCurrentState() { return currentState; }
    public Enemy getCurrentEnemy() { return currentEnemy; }
    public void setCurrentState(GameState state) { this.currentState = state; }
}