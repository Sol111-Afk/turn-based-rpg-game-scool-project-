package javatemajoc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;
import java.awt.Graphics2D;
import java.awt.Color;
import java.lang.Math;

public class ImageLoader {

    public static BufferedImage grassTile;
    public static BufferedImage wallTile;
    public static BufferedImage waterTile;
    public static BufferedImage chestTile; 
    public static BufferedImage playerSprite;
    
    // Inamici
    public static BufferedImage goblinSprite;
    public static BufferedImage slimeSprite; // NOU
    public static BufferedImage bossSprite;  // NOU (Licenta)
    
    public static BufferedImage battleBgImage;
    public static BufferedImage menuBgImage;
    public static BufferedImage inventoryBgImage;

    private static final String GRASS_PATH = "/resources/grass.png";
    private static final String WALL_PATH = "/resources/wall.png";
    private static final String WATER_PATH = "/resources/water.png";
    private static final String CHEST_PATH = "/resources/chest.png"; 
    private static final String PLAYER_PATH = "/resources/player.png";
    
    private static final String GOBLIN_PATH = "/resources/goblin.png";
    private static final String SLIME_PATH = "/resources/slime.png"; // NOU
    private static final String BOSS_PATH = "/resources/boss.png";   // NOUdddddddddddddddddddddddddddds
    
    private static final String BATTLE_BG_PATH = "/resources/battle_bg.png";
    private static final String MENU_BG_PATH = "/resources/menu_bg.png";
    private static final String INVENTORY_BG_PATH = "/resources/inventory_bg.png";

    public static void loadImages() {
        try {
            grassTile = readImage(GRASS_PATH);
            
            BufferedImage originalWallTile = readImage(WALL_PATH);
            wallTile = makeColorTransparent(originalWallTile, Color.WHITE, 30);
            
            waterTile = readImage(WATER_PATH);
            
            // --- CUFAR ---
            try { 
                BufferedImage originalChest = readImage(CHEST_PATH);
                int w = originalChest.getWidth();
                int h = originalChest.getHeight();
                Color c1 = new Color(originalChest.getRGB(0, 0), true);
                chestTile = makeColorTransparent(originalChest, c1, 50);
                Color c2 = new Color(originalChest.getRGB(w - 1, 0), true);
                chestTile = makeColorTransparent(chestTile, c2, 50);
                Color c3 = new Color(originalChest.getRGB(0, h - 1), true);
                chestTile = makeColorTransparent(chestTile, c3, 50);
                Color c4 = new Color(originalChest.getRGB(w - 1, h - 1), true);
                chestTile = makeColorTransparent(chestTile, c4, 50);
            } catch (Exception e) { }

            // --- JUCATOR ---
            BufferedImage originalPlayerSprite = readImage(PLAYER_PATH);
            playerSprite = makeColorTransparent(originalPlayerSprite, Color.BLACK, 15);
            
            // --- GOBLIN ---
            try {
                BufferedImage originalGoblin = readImage(GOBLIN_PATH);
                Color goblinBg = new Color(originalGoblin.getRGB(0, 0), true);
                goblinSprite = makeColorTransparent(originalGoblin, goblinBg, 30);
            } catch (Exception e) {}

            // --- SLIME (Inamic Roz) ---
            try {
                BufferedImage originalSlime = readImage(SLIME_PATH);
                Color slimeBg = new Color(originalSlime.getRGB(0, 0), true);
                slimeSprite = makeColorTransparent(originalSlime, slimeBg, 30);
            } catch (Exception e) {}

            // --- BOSS (Licenta) ---
            try {
                BufferedImage originalBoss = readImage(BOSS_PATH);
                Color bossBg = new Color(originalBoss.getRGB(0, 0), true);
                bossSprite = makeColorTransparent(originalBoss, bossBg, 30);
            } catch (Exception e) {}

            try { battleBgImage = readImage(BATTLE_BG_PATH); } catch (Exception e) { }
            try { inventoryBgImage = readImage(INVENTORY_BG_PATH); 
                  if (battleBgImage == null) battleBgImage = inventoryBgImage; } catch (Exception e) { }
            try { menuBgImage = readImage(MENU_BG_PATH); } catch (Exception e) { }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading resources: " + e.getMessage());
            System.exit(1);
        }
    }
    
    // ... Metodele helper (readImage, convertToARGB, makeColorTransparent, getTile) raman NESCHIMBATE ...
    // Le copiez mai jos doar ca sa fie fisierul complet daca dai copy-paste, 
    // dar sunt identice cu ce aveai.
    
    private static BufferedImage convertToARGB(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_INT_ARGB) return image;
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }
    
    private static BufferedImage makeColorTransparent(BufferedImage image, Color color, int tolerance) {
        BufferedImage dimg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dimg.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        int targetR = color.getRed(); int targetG = color.getGreen(); int targetB = color.getBlue();
        for (int i = 0; i < dimg.getHeight(); i++) {
            for (int j = 0; j < dimg.getWidth(); j++) {
                int rgb = dimg.getRGB(j, i);
                if ((rgb >> 24) == 0) continue;
                int r = (rgb >> 16) & 0xFF; int g_val = (rgb >> 8) & 0xFF; int b = rgb & 0xFF;
                if (Math.abs(r - targetR) <= tolerance && Math.abs(g_val - targetG) <= tolerance && Math.abs(b - targetB) <= tolerance) {
                    dimg.setRGB(j, i, 0x00FFFFFF);
                }
            }
        }
        return dimg;
    }

    private static BufferedImage readImage(String path) throws IOException {
        try (InputStream is = ImageLoader.class.getResourceAsStream(path)) {
            if (is == null) throw new IOException("Resource not found: " + path);
            BufferedImage originalImage = ImageIO.read(is);
            return convertToARGB(originalImage);
        }
    }

    public static BufferedImage getTile(int tileCode) {
        BufferedImage imageToReturn = null;
        switch (tileCode) {
            case 0: imageToReturn = grassTile; break;
            case 1: imageToReturn = wallTile; break;
            case 2: imageToReturn = waterTile; break;
            case 3: imageToReturn = chestTile; break; 
            default: imageToReturn = grassTile; break;
        }
        if (imageToReturn == null) {
            BufferedImage dummy = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = dummy.createGraphics();
            if (tileCode == 3) g.setColor(Color.YELLOW); 
            else g.setColor(Color.MAGENTA);
            g.fillRect(0, 0, 32, 32);
            g.dispose();
            return dummy;
        }
        return imageToReturn;
    }
}