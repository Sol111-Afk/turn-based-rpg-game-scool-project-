package javatemajoc;

import javax.swing.SwingUtilities;

public class JavaTemaJoc {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
           
            ImageLoader.loadImages(); 
            
          
            GameEngine engine = GameEngine.getInstance(); 
            
            
            MainFrame frame = new MainFrame("RPG Project Tema Java", engine);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}