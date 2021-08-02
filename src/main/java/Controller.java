import java.awt.*;
import java.util.List;

public class Controller {
    private final PheromoneMap pheromoneMap;
    private final Drawer drawer;
    private final List<Agent> agents;

    public Controller(PheromoneMap pheromoneMap, Drawer drawer, List<Agent> agents) {
        this.pheromoneMap = pheromoneMap;
        this.drawer = drawer;
        this.agents = agents;
    }

    public void run() {
        pheromoneMap.evaporatePheromones();
        pheromoneMap.diffusePheromones();
        depositPheromones();
        moveAgents();
        drawer.repaint();

        Counter.nbFrame += 1;
        if (Counter.nbFrame % 60 == 0) {
            System.out.println("Fps: " + (int) (1 / ((System.currentTimeMillis() - Counter.t0) / (Counter.nbFrame * 1000))));
        }
    }

    private void depositPheromones() {
        agents.forEach(agent -> pheromoneMap.depositPheromone(agent.getIntX(), agent.getIntY()));
    }

    private void moveAgents() {
        agents.forEach(agent -> {
            turnAgent(agent);
            moveAgent(agent);
        });
    }

    private void turnAgent(Agent agent) {
        List<Point> sensors = agent.getSensors();
        List<Integer> pheromoneValues = sensors.stream().map(p -> {
            if (p.x == agent.getIntX() && p.y == agent.getIntY()) {
                System.out.println("You are not allowed to sens yourself!");
                System.out.println(agent);
                System.out.println(sensors);
                System.exit(1);
            }
            return pheromoneMap.getPheromone(p.x, p.y);
        }).toList();
        int left = pheromoneValues.get(0);
        int forw = pheromoneValues.get(1);
        int right = pheromoneValues.get(2);
        if (forw > left && forw > right) {
        } else if (left > forw && right > forw) {
            agent.turnRandom();
        } else if (right > left) {
            agent.turnRight();
        } else if (left > right) {
            agent.turnLeft();
        }
    }

    private void moveAgent(Agent agent) {
        float newX = agent.getX() + (float) Math.cos(agent.getAngle() * Math.PI);
        float newY = agent.getY() - (float) Math.sin(agent.getAngle() * Math.PI);
        if (newX < 0 || newY < 0 || newX > pheromoneMap.getWidth() - 1 || newY > pheromoneMap.getHeight() - 1) {
            newX = Math.max(Math.min(newX, pheromoneMap.getWidth() - 1), 0);
            newY = Math.max(Math.min(newY, pheromoneMap.getHeight() - 1), 0);
            agent.moveTo(newX, newY);
            agent.randomAngle();
        } else {
            agent.moveTo(newX, newY);
        }
    }
}
