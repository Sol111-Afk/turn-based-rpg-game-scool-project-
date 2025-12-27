package javatemajoc;

import javax.swing.*;
import java.awt.*;
import static java.awt.Color.pink;
import java.io.FileNotFoundException;

public class MenuPanel extends JPanel {

    private MainFrame mainFrame;
    private GameEngine engine;
    private final String SAVE_FILE = "savegame_v2.dat"; 

    public MenuPanel(MainFrame mainFrame, GameEngine engine) {
        this.mainFrame = mainFrame;
        this.engine = engine;

        setLayout(new GridBagLayout()); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        
        gbc.gridy = 0;
        gbc.insets = new Insets(250, 10, 10, 10); 
        add(Box.createVerticalStrut(1), gbc); 

        JButton newGameBtn = createStyledButton("NEW GAME");
        newGameBtn.addActionListener(e -> {
            mainFrame.switchToPanel(MainFrame.MAP_PANEL_KEY);
        });
        
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10); 
        add(newGameBtn, gbc);

        JButton loadGameBtn = createStyledButton("LOAD GAME");
        loadGameBtn.addActionListener(e -> {
            try {
                engine.load(SAVE_FILE);
                mainFrame.getMapPanel().updatePixelPositionFromPlayer();
                mainFrame.switchToPanel(MainFrame.MAP_PANEL_KEY);
                JOptionPane.showMessageDialog(this, "Save loaded successfully!");
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "No save file found!", "Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading save: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        gbc.gridy = 2;
        add(loadGameBtn, gbc);

        JButton exitBtn = createStyledButton("EXIT");
        exitBtn.addActionListener(e -> System.exit(0));
        
        gbc.gridy = 3;
        add(exitBtn, gbc);
        
        JLabel creditsLabel = new JLabel("Created by Duca Ioana Teodora & SOL");
        
        creditsLabel.setForeground(new Color(220, 220, 220)); 
        creditsLabel.setFont(new Font("Monospaced", Font.BOLD, 16)); 
        creditsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        creditsLabel.setBackground(pink);
        gbc.gridy = 4;
        gbc.insets = new Insets(100, 0, 0, 0); 
        add(creditsLabel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ImageLoader.menuBgImage != null) {
            g.drawImage(ImageLoader.menuBgImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(new Color(34, 34, 34));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(80, 80, 80)); 
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Monospaced", Font.BOLD, 24)); 
        button.setPreferredSize(new Dimension(250, 60));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        return button;
    }
}