import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.MemoryImageSource;

@Getter
public class Drawer extends JPanel {
    public final static Color backgroundColor = Color.BLACK;
    public final static Color agentColor = Color.WHITE;

    private final int width;
    private final int height;
    private final PheromoneMap pheromoneMap;
    private final GraphicsConfiguration gfxConfig;
    private final MemoryImageSource mImageProducer;
    private final Image image;

    public Drawer(int width, int height, PheromoneMap pheromoneMap) {
        this.width = width;
        this.height = height;
        this.pheromoneMap = pheromoneMap;
        this.gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        this.mImageProducer = new MemoryImageSource(width, height, gfxConfig.getColorModel(), getPheromoneMap().getRgbs(), 0, width);
        this.mImageProducer.setAnimated(true);
        this.mImageProducer.setFullBufferUpdates(true);
        this.image = Toolkit.getDefaultToolkit().createImage(mImageProducer);
    }

    @Override
    public void paintComponent(Graphics g) {
        mImageProducer.newPixels();
        g.drawImage(image, 0, 0, this);
    }
}
