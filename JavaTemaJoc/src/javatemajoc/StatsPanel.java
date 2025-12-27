package javatemajoc;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {

    private GameEngine engine;
    
    private JLabel hpLabel;
    private JLabel manaLabel;
    private JLabel levelLabel;
    private JLabel goldLabel;
    private JLabel xpLabel;

    public StatsPanel(GameEngine engine) {
        this.engine = engine;

        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
        setBackground(new Color(50, 50, 50));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        JLabel titleLabel = new JLabel("Game Status: ");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        add(titleLabel);

        hpLabel = new JLabel("HP: 100/100");
        hpLabel.setForeground(Color.GREEN);
        hpLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        add(hpLabel);
        
        manaLabel = new JLabel("Mana: 50/50");
        manaLabel.setForeground(new Color(0, 191, 255));
        manaLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        add(manaLabel);

        levelLabel = new JLabel("Lvl: 1");
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(levelLabel);

        goldLabel = new JLabel("Gold: 0");
        goldLabel.setForeground(Color.ORANGE);
        goldLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(goldLabel);

        xpLabel = new JLabel("XP: 0/100");
        xpLabel.setForeground(Color.LIGHT_GRAY);
        xpLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(xpLabel);
    }

    public void updateStats() {
        if (engine.getPlayer() != null) {
            Player p = engine.getPlayer();
            
            hpLabel.setText("HP: " + p.getCurrentHP() + "/" + p.getMaxHP());
            manaLabel.setText("Mana: " + p.getCurrentMana() + "/" + p.getMaxMana());
            levelLabel.setText("Lvl: " + p.getLevel());
            goldLabel.setText("Gold: " + p.getGold());
            xpLabel.setText("XP: " + p.getCurrentXP() + "/" + p.getXpToNextLevel());
            
            repaint();
        }
    }
}