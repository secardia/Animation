import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Animation {
    private final static int worldWidth = 1600;
    private final static int worldHeight = 800;
    private final static double fps = 60;
    private final static int nbAgent = 100000;

    public static void main(String[] args) throws InterruptedException {
        Counter.t0 = System.currentTimeMillis();

        PheromoneMap pheromoneMap = new PheromoneMap(worldWidth, worldHeight);
        Drawer drawer = new Drawer(worldWidth, worldHeight, pheromoneMap);
        Controller controller = new Controller(pheromoneMap, drawer, generateRandomAntsInCircleFacingCenter(nbAgent));

        JFrame frame = new JFrame();
        frame.setSize(worldWidth + 16, worldHeight + 39);
        frame.add(drawer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (true) {
            double lastIter = System.currentTimeMillis();
            controller.run();
            Thread.sleep((long) Math.max(1000 / fps - (System.currentTimeMillis() - lastIter), 0));
        }
    }

    private static List<Agent> generateRandomAgents(int nbAgent) {
        List<Agent> agents = new ArrayList<>(nbAgent);
        for (int i = 0; i < nbAgent; i++) {
            float x = (float) (Math.random() * (worldWidth - 1));
            float y = (float) (Math.random() * (worldHeight - 1));
            float angle = (float) (Math.random() * 2);
            agents.add(new Agent(x, y, angle));
        }
        return agents;
    }

    private static List<Agent> generateRandomAntsFacingCenter(int nbAgent) {
        List<Agent> agents = new ArrayList<>(nbAgent);
        for (int i = 0; i < nbAgent; i++) {
            float x = (float) (Math.random() * (worldWidth - 1));
            float y = (float) (Math.random() * (worldHeight - 1));
            float angle = (float) (Math.atan2(y - worldHeight / 2f, worldWidth / 2f - x) / Math.PI);
            agents.add(new Agent(x, y, angle));
        }
        return agents;
    }

    private static List<Agent> generateRandomAntsInCircleFacingCenter(int nbAgent) {
        List<Agent> agents = new ArrayList<>(nbAgent);
        for (int i = 0; i < nbAgent; i++) {
            float ang = (float) (Math.random() * 2);
            float dist = (float) (Math.random() * (worldHeight / 2));
            float x = worldWidth / 2f + (float) Math.cos(ang * Math.PI) * dist;
            float y = worldHeight / 2f - (float) Math.sin(ang * Math.PI) * dist;
            float angle = (float) (Math.atan2(y - worldHeight / 2f, worldWidth / 2f - x) / Math.PI);
            if (x > worldWidth - 1 || y > worldHeight - 1 || x < 0 || y < 0) {
                x = Math.max(Math.min(x, worldWidth - 1), 0);
                y = Math.max(Math.min(y, worldHeight - 1), 0);
            }
            agents.add(new Agent(x, y, angle));
        }
        return agents;
    }

    private static List<Agent> generateAgentsFacingOut(int nbAgent) {
        List<Agent> agents = new ArrayList<>(nbAgent);
        for (int i = 0; i < nbAgent; i++) {
            float x = worldWidth / 2f;
            float y = worldHeight / 2f;
            float angle = 2f / nbAgent * i;
            agents.add(new Agent(x, y, angle));
        }
        return agents;
    }

    private static List<Agent> generateAgentsTest(int nbAgent) {
        List<Agent> agents = new ArrayList<>(nbAgent);
        agents.add(new Agent(5, 2, 0.0f));
//        agents.add(new Agent(50, 50, 0.0f));
//        agents.add(new Agent(50, 80, 0.5f));
//        agents.add(new Agent(300,300,1));
//        agents.add(new Agent(400,400,1.5f));
        return agents;
    }
}
