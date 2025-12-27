package javatemajoc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class BattlePanel extends JPanel {

    private GameEngine engine;
    private MainFrame mainFrame;

    private JLabel playerStatsLabel;
    private JLabel enemyStatsLabel;
    private JProgressBar playerHPBar;
    private JProgressBar playerManaBar;
    private JProgressBar enemyHPBar;
    
    private JTextArea battleLog;
    private JPanel actionsPanel;

    private Timer textAnimationTimer;
    private String textToAnimate = "";
    private int charIndex = 0;

    public BattlePanel(GameEngine engine, MainFrame mainFrame) {
        this.engine = engine;
        this.mainFrame = mainFrame;
        engine.setBattlePanelReference(this);

        setLayout(null);
        setBackground(new Color(20, 20, 20));

        // --- BARE DE VIATA ---
        playerHPBar = new JProgressBar(0, 100);
        playerHPBar.setBounds(50, 50, 300, 30);
        playerHPBar.setForeground(Color.GREEN);
        playerHPBar.setStringPainted(true);
        add(playerHPBar);
        
        playerManaBar = new JProgressBar(0, 50);
        playerManaBar.setBounds(50, 85, 300, 20);
        playerManaBar.setForeground(new Color(0, 191, 255));
        playerManaBar.setStringPainted(true);
        add(playerManaBar);

        enemyHPBar = new JProgressBar(0, 100);
        enemyHPBar.setBounds(880, 50, 300, 30);
        enemyHPBar.setForeground(Color.RED);
        enemyHPBar.setStringPainted(true);
        add(enemyHPBar);

        playerStatsLabel = new JLabel("Player");
        playerStatsLabel.setForeground(Color.WHITE);
        playerStatsLabel.setBounds(50, 20, 300, 30);
        playerStatsLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        add(playerStatsLabel);

        enemyStatsLabel = new JLabel("Enemy");
        enemyStatsLabel.setForeground(Color.WHITE);
        enemyStatsLabel.setBounds(880, 20, 300, 30);
        enemyStatsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        enemyStatsLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        add(enemyStatsLabel);

        battleLog = new JTextArea();
        battleLog.setBounds(340, 450, 600, 100);
        battleLog.setEditable(false);
        battleLog.setOpaque(false);
        battleLog.setForeground(Color.WHITE);
        battleLog.setFont(new Font("Monospaced", Font.BOLD, 22));
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(battleLog);

        actionsPanel = new JPanel();
        actionsPanel.setBounds(50, 650, 1180, 100);
        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        JButton attackBtn = createStyledButton("ATTACK");
        attackBtn.addActionListener(e -> engine.performPlayerTurn(ActionType.ATTACK, null));

        JButton skillBtn = createStyledButton("SKILL");
        skillBtn.addActionListener(e -> showSkillMenu());

        JButton fleeBtn = createStyledButton("FLEE");
        fleeBtn.addActionListener(e -> engine.performPlayerTurn(ActionType.FLEE, null));

        actionsPanel.add(attackBtn);
        actionsPanel.add(skillBtn);
        actionsPanel.add(fleeBtn);
        add(actionsPanel);
    }
    
    public void updateBattleDisplay() {
        Player p = engine.getPlayer();
        Enemy e = engine.getCurrentEnemy();
        
        playerStatsLabel.setText(p.getName() + " (Lvl " + p.getLevel() + ")");
        enemyStatsLabel.setText(e.getName() + " (Lvl " + e.getLevel() + ")");
        
        updateStatsBars(); 
        
        battleLog.setText("A wild " + e.getName() + " appeared!");
        actionsPanel.setVisible(true);
        repaint();
    }

    public void updateStatsBars() {
        Player p = engine.getPlayer();
        Enemy e = engine.getCurrentEnemy();
        
        playerHPBar.setMaximum(p.getMaxHP());
        playerHPBar.setValue(p.getCurrentHP());
        playerHPBar.setString("HP: " + p.getCurrentHP() + "/" + p.getMaxHP());
        
        playerManaBar.setMaximum(p.getMaxMana());
        playerManaBar.setValue(p.getCurrentMana());
        playerManaBar.setString("Mana: " + p.getCurrentMana() + "/" + p.getMaxMana());
        
        if (e != null) {
            enemyHPBar.setMaximum(e.getMaxHP());
            enemyHPBar.setValue(e.getCurrentHP());
            enemyHPBar.setString("HP: " + e.getCurrentHP() + "/" + e.getMaxHP());
        }
        
        playerHPBar.repaint();
        playerManaBar.repaint();
        enemyHPBar.repaint();
    }

    private void showSkillMenu() {
        JPopupMenu skillMenu = new JPopupMenu();
        java.util.List<Skill> skills = engine.getPlayer().getInventory().getSkills();
        
        if (skills.isEmpty()) {
            skillMenu.add(new JMenuItem("No skills learned"));
        } else {
            for (Skill skill : skills) {
                JMenuItem item = new JMenuItem(skill.getName() + " (" + skill.getCost() + " Mana)");
                item.setFont(new Font("Monospaced", Font.BOLD, 16));
                item.addActionListener(e -> engine.performPlayerTurn(ActionType.USE_SKILL, skill));
                skillMenu.add(item);
            }
        }
        skillMenu.show(this, getWidth() / 2, 650);
    }

    public void animatePlayerText(String text) {
        startTextAnimation(text, () -> {
            Timer pause = new Timer(1000, evt -> engine.performEnemyTurn());
            pause.setRepeats(false);
            pause.start();
        });
    }

    public void animateEnemyText(String text) {
        startTextAnimation(text, null);
    }
    
    private void startTextAnimation(String text, Runnable onComplete) {
        textToAnimate = text;
        charIndex = 0;
        battleLog.setText("");
        
        if (textAnimationTimer != null && textAnimationTimer.isRunning()) {
            textAnimationTimer.stop();
        }

        textAnimationTimer = new Timer(30, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (charIndex < textToAnimate.length()) {
                    battleLog.append(String.valueOf(textToAnimate.charAt(charIndex)));
                    charIndex++;
                } else {
                    textAnimationTimer.stop();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
            }
        });
        textAnimationTimer.start();
    }

    public void showVictoryScreen(int xp, int gold) {
        actionsPanel.setVisible(false);
        battleLog.setText("VICTORY!\nRecieved: " + xp + " XP, " + gold + " Gold\n[Click to Continue]");
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                removeMouseListener(this);
                engine.finalizeBattleEnd(); 
                mainFrame.switchToPanel(MainFrame.MAP_PANEL_KEY);
            }
        });
    }

    public void showFleeSuccess(String msg) {
        actionsPanel.setVisible(false);
        battleLog.setText(msg + "\n[Click to Continue]");
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                removeMouseListener(this);
                engine.finalizeBattleEnd();
                mainFrame.switchToPanel(MainFrame.MAP_PANEL_KEY);
            }
        });
    }

    // --- AICI ESTE MODIFICAREA FINALA ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        Enemy e = engine.getCurrentEnemy();
        
        // Daca nu e inamic, desenam fundalul standard si iesim
        if (e == null) {
            if (ImageLoader.battleBgImage != null) {
                 g2d.drawImage(ImageLoader.battleBgImage, 0, 0, getWidth(), getHeight(), null);
            }
            return;
        }
        
        String name = e.getName();
        BufferedImage sceneImageToDraw = null;

        // Selectam imaginea scena completa in functie de inamic
        if (name.contains("Lucrarea") || name.contains("Licen")) {
            sceneImageToDraw = ImageLoader.bossSprite;
        } else if (name.contains("Slime")) {
            sceneImageToDraw = ImageLoader.slimeSprite;
        } else if (name.contains("Goblin")) {
            sceneImageToDraw = ImageLoader.goblinSprite;
        }
        
        // Desenam scena pe tot ecranul
        if (sceneImageToDraw != null) {
             g2d.drawImage(sceneImageToDraw, 0, 0, getWidth(), getHeight(), null);
        } else {
             // Fallback: daca lipseste imaginea specifica, punem fundalul standard
             if (ImageLoader.battleBgImage != null) {
                 g2d.drawImage(ImageLoader.battleBgImage, 0, 0, getWidth(), getHeight(), null);
            }
        }
        
        // IMPORTANT: NU mai desenam ImageLoader.playerSprite separat aici, 
        // pentru ca el este deja inclus in imaginile de tip scena.
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(150, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Monospaced", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        return button;
    }
}