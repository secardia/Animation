import lombok.Getter;

import java.awt.*;
import java.util.List;

@Getter
public class Agent {
    private static final float deltaAngle = 0.25f;
    private static final float deltaSensor = 2;

    private float x;
    private float y;
    private float angle;

    public Agent(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public List<Point> getSensors() {
        return List.of(
                new Point(
                        toInt(x + Math.cos((angle + deltaAngle) * Math.PI) * deltaSensor),
                        toInt(y - Math.sin((angle + deltaAngle) * Math.PI) * deltaSensor)
                ),
                new Point(
                        toInt(x + Math.cos(angle * Math.PI) * deltaSensor),
                        toInt(y - Math.sin(angle * Math.PI) * deltaSensor)
                ),
                new Point(
                        toInt(x + Math.cos((angle - deltaAngle) * Math.PI) * deltaSensor),
                        toInt(y - Math.sin((angle - deltaAngle) * Math.PI) * deltaSensor)
                )
        );
    }

    private int toInt(double f) {
        return Math.round((float) f);
//        return f < 0 ? -1 : (int) f;
    }

    public int getIntX() {
        return toInt(x);
    }

    public int getIntY() {
        return toInt(y);
    }

    public void turnLeft() {
        angle += deltaAngle;
    }

    public void turnRight() {
        angle -= deltaAngle;
    }

    public void turnRandom() {
        angle += (float) (Math.random() * (deltaAngle * 2) - deltaAngle);
    }

    public void randomAngle() {
        angle = (float) (Math.random() * 2);
    }

    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
