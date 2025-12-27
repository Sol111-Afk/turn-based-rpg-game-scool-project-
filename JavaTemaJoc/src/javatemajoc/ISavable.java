package javatemajoc;

import java.io.IOException;

public interface ISavable {
    
    void save(String filename) throws IOException;
    
    Player load(String filename) throws IOException, ClassNotFoundException;
}