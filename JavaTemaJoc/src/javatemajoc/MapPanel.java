package javatemajoc;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapPanel extends JPanel implements ActionListener {
    
    private GameEngine engine;
    private MainFrame mainFrame;
    
    private double scaleFactor = 5.0; 
    private final int BASE_TILE_SIZE = 32; 
    private int tileSize; 
    
    private Timer gameLoop; 

    private int[][] mapData = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 3, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 1},
        {1, 0, 3, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 1, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 3, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    public MapPanel(GameEngine engine, MainFrame mainFrame) {
        this.engine = engine;
        this.mainFrame = mainFrame;
        
        this.tileSize = (int) (BASE_TILE_SIZE * scaleFactor);
        engine.getPlayer().snapToGrid(tileSize);
        
        setFocusable(true);
        setBackground(Color.BLACK);
        
        gameLoop = new Timer(16, this);
        gameLoop.start();

        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                requestFocusInWindow();
                engine.getPlayer().snapToGrid(tileSize);
                if (!gameLoop.isRunning()) gameLoop.start();
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (engine.getCurrentState() != GameState.MAP) return;
                
                if (engine.getPlayer().isMoving()) return;

                int key = e.getKeyCode();
                
                if (key == KeyEvent.VK_I) {
                    mainFrame.switchToPanel(MainFrame.INVENTORY_PANEL_KEY);
                    return;
                }

                int dx = 0;
                int dy = 0;

                switch (key) {
                    case KeyEvent.VK_W: dy = -1; break;
                    case KeyEvent.VK_S: dy = 1; break;
                    case KeyEvent.VK_A: dx = -1; break;
                    case KeyEvent.VK_D: dx = 1; break;
                }
                
                if (dx != 0 || dy != 0) {
                    Player p = engine.getPlayer();
                    int nextX = p.getX() + dx;
                    int nextY = p.getY() + dy;
                    
                    if (nextY >= 0 && nextY < mapData.length &&
                        nextX >= 0 && nextX < mapData[0].length) {
                        
                        if (mapData[nextY][nextX] == 3) {
                            engine.openChest();
                            mapData[nextY][nextX] = 0; 
                            p.setTargetPosition(nextX, nextY, tileSize);
                        }
                        else if (mapData[nextY][nextX] != 1) {
                            p.setTargetPosition(nextX, nextY, tileSize);
                        }
                    }
                }
            }
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (engine.getCurrentState() == GameState.MAP) {
            Player p = engine.getPlayer();
            
            if (p.isMoving()) {
                p.update();
                if (!p.isMoving()) {
                    engine.checkEncounter(); 
                    if (engine.getCurrentState() == GameState.BATTLE) {
                        mainFrame.switchToPanel(MainFrame.BATTLE_PANEL_KEY);
                    }
                }
            }
            repaint();
        }
    }
    
    public void updatePixelPositionFromPlayer() {
        engine.getPlayer().snapToGrid(tileSize);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int mapHeight = mapData.length;
        int mapWidth = mapData[0].length;
        
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        int playerPixelX = engine.getPlayer().getPixelX();
        int playerPixelY = engine.getPlayer().getPixelY();
        
        int cameraX = playerPixelX - panelWidth / 2 + tileSize / 2;
        int cameraY = playerPixelY - panelHeight / 2 + tileSize / 2;
        
        int maxCameraX = mapWidth * tileSize - panelWidth;
        int maxCameraY = mapHeight * tileSize - panelHeight;
        
        if (maxCameraX < 0) { cameraX = maxCameraX / 2; }
        else {
            if (cameraX < 0) cameraX = 0;
            if (cameraX > maxCameraX) cameraX = maxCameraX;
        }

        if (maxCameraY < 0) { cameraY = maxCameraY / 2; }
        else {
            if (cameraY < 0) cameraY = 0;
            if (cameraY > maxCameraY) cameraY = maxCameraY;
        }

        int startCol = Math.max(0, cameraX / tileSize);
        int endCol = Math.min(mapWidth, (cameraX + panelWidth) / tileSize + 2);
        int startRow = Math.max(0, cameraY / tileSize);
        int endRow = Math.min(mapHeight, (cameraY + panelHeight) / tileSize + 2);

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                int tileCode = mapData[row][col];
                int screenX = col * tileSize - cameraX;
                int screenY = row * tileSize - cameraY;
                
                // --- MODIFICARE IMPORTANTA AICI ---
                
                // 1. Desenam IARBA (Tile 0) peste tot ca fundal
                g.drawImage(ImageLoader.getTile(0), screenX, screenY, tileSize, tileSize, null);
                
                // 2. Daca tile-ul curent NU e iarba (e Cufar sau Zid), il desenam DEASUPRA
                if (tileCode != 0) {
                     g.drawImage(ImageLoader.getTile(tileCode), screenX, screenY, tileSize, tileSize, null);
                }
                
                // ----------------------------------
            }
        }
        
        if (ImageLoader.playerSprite != null) {
            int playerScreenX = playerPixelX - cameraX;
            int playerScreenY = playerPixelY - cameraY;
            g.drawImage(ImageLoader.playerSprite, playerScreenX, playerScreenY, tileSize, tileSize, null);
        }
    }
}