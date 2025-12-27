package javatemajoc;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InventoryPanel extends JPanel {

    private MainFrame mainFrame;
    private GameEngine engine;
    
    private JLabel nameLabel;
    private JLabel levelLabel;
    private JLabel hpLabel;
    private JLabel manaLabel; // LABEL NOU
    private JLabel statsLabel;
    private JLabel goldLabel;
    private JTextArea skillsArea;

    public InventoryPanel(MainFrame mainFrame, GameEngine engine) {
        this.mainFrame = mainFrame;
        this.engine = engine;

        setLayout(null);
        setFocusable(true); 

        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) { requestFocusInWindow(); }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });

        // Stanga (CHARACTER)
        nameLabel = createLabel("Hero Name", 150, 200, 400, 40, 32);
        add(nameLabel);

        levelLabel = createLabel("Lvl: 1  XP: 0/100", 150, 260, 400, 30, 22);
        levelLabel.setForeground(new Color(255, 215, 0)); 
        add(levelLabel);

        hpLabel = createLabel("HP: 100/100", 150, 300, 400, 30, 22);
        hpLabel.setForeground(Color.GREEN);
        add(hpLabel);
        
        // --- AFISARE MANA ---
        manaLabel = createLabel("MANA: 50/50", 150, 330, 400, 30, 22);
        manaLabel.setForeground(new Color(0, 191, 255)); // Deep Sky Blue
        add(manaLabel);
        // --------------------

        statsLabel = createLabel("ATK: 10  DEF: 5", 150, 370, 400, 30, 22);
        add(statsLabel);
        
        // Dreapta (INVENTORY / SKILLS)
        goldLabel = createLabel("Gold: 0", 750, 200, 400, 30, 24);
        goldLabel.setForeground(Color.ORANGE);
        add(goldLabel);
        
        JLabel skillsTitle = createLabel("--- SKILLS ---", 750, 250, 400, 30, 20);
        skillsTitle.setForeground(Color.CYAN);
        add(skillsTitle);

        skillsArea = new JTextArea();
        skillsArea.setBounds(750, 290, 400, 300);
        skillsArea.setOpaque(false); 
        skillsArea.setForeground(Color.WHITE);
        skillsArea.setFont(new Font("Monospaced", Font.BOLD, 18));
        skillsArea.setEditable(false);
        add(skillsArea);

        JLabel closeLabel = createLabel("[ Press 'I' or 'ESC' to Close ]", 0, 850, 1280, 30, 18);
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        closeLabel.setForeground(Color.LIGHT_GRAY);
        add(closeLabel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_I || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    mainFrame.switchToPanel(MainFrame.MAP_PANEL_KEY);
                }
            }
        });
    }

    public void updateInventoryDisplay() {
        Player p = engine.getPlayer();
        
        nameLabel.setText(p.getName());
        levelLabel.setText("Lvl: " + p.getLevel() + "  XP: " + p.getCurrentXP() + "/" + p.getXpToNextLevel());
        hpLabel.setText("HP: " + p.getCurrentHP() + "/" + p.getMaxHP());
        manaLabel.setText("MANA: " + p.getCurrentMana() + "/" + p.getMaxMana()); // Update Mana
        statsLabel.setText("ATK: " + p.getAttack() + "   DEF: " + p.getDefense());
        goldLabel.setText("GOLD: " + p.getGold());

        StringBuilder sb = new StringBuilder();
        if (p.getInventory().getSkills().isEmpty()) {
            sb.append("No skills learned.");
        } else {
            for (Skill s : p.getInventory().getSkills()) {
                // Afisam Costul in Mana
                sb.append("• ").append(s.getName())
                  .append("\n  Cost: ").append(s.getCost()).append(" Mana\n\n");
            }
        }
        skillsArea.setText(sb.toString());
        
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (ImageLoader.inventoryBgImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2d.drawImage(ImageLoader.inventoryBgImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(new Color(34, 34, 34));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        if (ImageLoader.playerSprite != null) {
            int scale = 6; 
            int imgW = ImageLoader.playerSprite.getWidth() * scale;
            int imgH = ImageLoader.playerSprite.getHeight() * scale;
            
            int x = 250 - (imgW / 2); 
            int y = 500;
            
            g.drawImage(ImageLoader.playerSprite, x, y, imgW, imgH, null);
        }
    }

    private JLabel createLabel(String text, int x, int y, int w, int h, int fontSize) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, h);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, fontSize));
        return label;
    }
}