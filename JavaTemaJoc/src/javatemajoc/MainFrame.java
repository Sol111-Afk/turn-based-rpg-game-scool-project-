package javatemajoc;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainFrame extends JFrame {

    public static final String MENU_PANEL_KEY = "Menu";
    public static final String MAP_PANEL_KEY = "Map";
    public static final String BATTLE_PANEL_KEY = "Battle";
    public static final String INVENTORY_PANEL_KEY = "Inventory"; // CHEIE NOUA
    
    private GameEngine engine;
    private CardLayout cardLayout;
    private JPanel mainPanel; 

    private StatsPanel statsPanel;
    private MapPanel mapPanel;
    private BattlePanel battlePanel;
    private MenuPanel menuPanel;
    private InventoryPanel inventoryPanel; // REFERINTA NOUA
    
    private final String SAVE_FILE = "savegame_v2.dat"; 

    public MainFrame(String title, GameEngine engine) {
        super(title);
        this.engine = engine;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 960);
        this.setResizable(false);
        
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        statsPanel = new StatsPanel(engine); 
        engine.setStatsPanelReference(statsPanel);
        
        mapPanel = new MapPanel(engine, this); 
        battlePanel = new BattlePanel(engine, this);
        menuPanel = new MenuPanel(this, engine);
        inventoryPanel = new InventoryPanel(this, engine); // INITIALIZARE NOUA
        
        mainPanel.add(menuPanel, MENU_PANEL_KEY);
        mainPanel.add(mapPanel, MAP_PANEL_KEY);
        mainPanel.add(battlePanel, BATTLE_PANEL_KEY);
        mainPanel.add(inventoryPanel, INVENTORY_PANEL_KEY); // ADAUGARE IN LAYOUT
        
        createMenuBar();
        
        this.add(statsPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        
        switchToPanel(MENU_PANEL_KEY);
    }
    
    public MapPanel getMapPanel() {
        return mapPanel;
    }

    public void switchToPanel(String panelName) {
        // Daca intram in inventar, actualizam datele
        if (panelName.equals(INVENTORY_PANEL_KEY)) {
            inventoryPanel.updateInventoryDisplay();
            inventoryPanel.requestFocusInWindow();
        }

        cardLayout.show(mainPanel, panelName);
        
        // Ascundem bara de stats in Meniu si Inventar (ca e deja in inventar)
        if (panelName.equals(MENU_PANEL_KEY) || panelName.equals(INVENTORY_PANEL_KEY)) {
            statsPanel.setVisible(false);
        } else {
            statsPanel.setVisible(true);
            statsPanel.updateStats();
        }
        
        if (panelName.equals(MAP_PANEL_KEY)) {
            mapPanel.requestFocusInWindow();
        }
        
        mainPanel.revalidate(); 
        mainPanel.repaint();    
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        
        JMenuItem saveItem = new JMenuItem("Save Game");
        saveItem.addActionListener(e -> saveGameAction());
        
        JMenuItem loadItem = new JMenuItem("Load Game");
        loadItem.addActionListener(e -> loadGameAction());
        
        JMenuItem exitToMenuItem = new JMenuItem("Exit to Menu");
        exitToMenuItem.addActionListener(e -> switchToPanel(MENU_PANEL_KEY));
        
        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        gameMenu.addSeparator();
        gameMenu.add(exitToMenuItem);
        
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private void saveGameAction() {
        try {
            engine.save(SAVE_FILE);
            JOptionPane.showMessageDialog(this, "Game Saved Successfully!", "Save Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGameAction() {
        try {
            engine.load(SAVE_FILE);
            statsPanel.updateStats(); 
            mapPanel.updatePixelPositionFromPlayer();
            mapPanel.requestFocusInWindow(); 
            JOptionPane.showMessageDialog(this, "Game Loaded Successfully!", "Load Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Save file not found.", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}