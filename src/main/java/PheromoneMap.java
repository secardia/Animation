import lombok.Getter;
import lombok.SneakyThrows;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class PheromoneMap {
    public final static int pheromoneBaseValue = 100;
    public final static int r = 1;

    private final int width;
    private final int height;
    private final int[] valueToColor;
    private final int[] pheromones;
    private final int[] rgbs;

    public PheromoneMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.valueToColor = new int[pheromoneBaseValue + 1];
        for (int i = 0; i < valueToColor.length; i++) {
            int newRed = (Drawer.agentColor.getRed() * i + Drawer.backgroundColor.getRed() * (pheromoneBaseValue - i)) / pheromoneBaseValue;
            int newGreen = (Drawer.agentColor.getGreen() * i + Drawer.backgroundColor.getGreen() * (pheromoneBaseValue - i)) / pheromoneBaseValue;
            int newBlue = (Drawer.agentColor.getBlue() * i + Drawer.backgroundColor.getBlue() * (pheromoneBaseValue - i)) / pheromoneBaseValue;
            Color color = new Color(newRed, newGreen, newBlue);
            int c = color.getRGB() & 0x00FFFFFF;
            valueToColor[i] = c;
        }
        this.pheromones = new int[width * height];
        Arrays.fill(pheromones, 0);
        this.rgbs = new int[width * height];
        Arrays.fill(rgbs, valueToColor[0]);
    }

    public int getPheromone(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return 0;
        }
        return pheromones[x + y * width];
    }

    public void depositPheromone(int x, int y) {
        if (pheromones[x + y * width] != pheromoneBaseValue) {
            pheromones[x + y * width] = pheromoneBaseValue;
            rgbs[x + y * width] = valueToColor[pheromoneBaseValue];
        }
    }

    public void evaporatePheromones() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (pheromones[i + j * width] != 0) {
                    pheromones[i + j * width] -= 1;
                    rgbs[i + j * width] = valueToColor[pheromones[i + j * width]];
                }
            }
        }
    }

    @SneakyThrows
    public void diffusePheromones() {
        int div = 3;
        ArrayList<Thread> threadsX = new ArrayList<>();
        ArrayList<Thread> threadsY = new ArrayList<>();
        int[] res = new int[width * height];
        for (int i = 0; i < div; i++) {
            final int p = i;
            threadsX.add(new Thread(() -> diffuseX(res, p * height / div, (p + 1) * height / div)));
            threadsY.add(new Thread(() -> diffuseY(res, p * height / div, (p + 1) * height / div)));
        }
        for (Thread t : threadsX) {
            t.start();
        }
        for (Thread t : threadsX) {
            t.join();
        }
        for (int i = 0; i < pheromones.length; i++) {
            if (res[i] > pheromones[i])
                pheromones[i] = res[i];
        }
        for (Thread t : threadsY) {
            t.start();
        }
        for (Thread t : threadsY) {
            t.join();
        }
        for (int i = 0; i < pheromones.length; i++) {
            if (res[i] > pheromones[i])
                pheromones[i] = res[i];
            rgbs[i] = valueToColor[res[i]];
        }
    }

    private void diffuseX(int[] res, int start, int end) {
        for (int j = start; j < end; j++) {
            for (int i = 0; i < width; i++) {
                int sum = 0;
                for (int k = -r; k <= r; k++) {
                    int ik = Math.max(Math.min(i + k, width - 1), 0);
                    sum += pheromones[ik + j * width];
                }
                res[i + j * width] = sum / (2 * r + 1);
            }
        }
    }

    private void diffuseY(int[] res, int start, int end) {
        for (int j = start; j < end; j++) {
            for (int i = 0; i < width; i++) {
                int sum = 0;
                for (int k = -r; k <= r; k++) {
                    int jk = Math.max(Math.min(j + k, height - 1), 0);
                    sum += pheromones[i + jk * width];
                }
                res[i + j * width] = sum / (2 * r + 1);
            }
        }
    }

    private void printPheromones(int[] pheromones) {
        System.out.println("---");
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                System.out.print(pheromones[i + j * width] + ", ");
            }
            System.out.println();
        }
    }
}
