//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Thread;

import java.util.Random;
import main.SimpleEasyEastProject;

public class moveThread implements Runnable {
    Random random = new Random();

    public moveThread() {
    }

    public void run() {
        this.sleep(1000L);
        this.movement(750, 100 + this.random.nextInt(50), 30.0);
        this.sleep(1000L);
        this.EmitterType1(15);
        this.sleep(1000L);
        this.moveAttack(750, 100 + this.random.nextInt(50), -150.0);
        this.EmitterType2(1.0, 0.5, 18);
        this.sleep(500L);
        this.EmitterType1(10);
        this.sleep(500L);
        this.EmitterType1(15);
        this.movement(750, 50 + this.random.nextInt(50), -90.0);
        this.moveAttack(750, 50 + this.random.nextInt(50), 90.0);
        this.EmitterType2(1.0, 0.5, 14);
        this.movement(750, 100 + this.random.nextInt(100), 75.0);
        this.sleep(250L);
        this.moveAttack(500, 200 + this.random.nextInt(150), -35.0);
        this.moveAttack(250, 200 + this.random.nextInt(150), 180.0);
    }

    private void sleep(long sleep) {
        try {
            if (sleep <= 0L) {
                sleep = 0L;
            }

            Thread.sleep(sleep);
        } catch (InterruptedException var4) {
            throw new RuntimeException(var4);
        }
    }

    private void movement(int sleepDuration, int distance, double theta) {
        this.sleep(sleepDuration);
        int emitterNum = SimpleEasyEastProject.RoundEmitterNum;
        SimpleEasyEastProject.RoundEmitterNum = 0;

        for(double i = 0.0; i < (double)distance; ++i) {
            SimpleEasyEastProject.cartesian.addX(Math.cos(Math.toRadians(theta)));
            SimpleEasyEastProject.cartesian.addY(Math.sin(Math.toRadians(theta)));
            this.sleep(10L);
        }

        SimpleEasyEastProject.RoundEmitterNum = emitterNum;
    }

    private void moveAttack(int sleepDuration, int distance, double theta) {
        this.sleep(sleepDuration);

        for(double i = 0.0; i < (double)distance; ++i) {
            SimpleEasyEastProject.cartesian.addX(Math.cos(Math.toRadians(theta)));
            SimpleEasyEastProject.cartesian.addY(Math.sin(Math.toRadians(theta)));
            this.sleep(10L);
        }

    }

    private void EmitterType1(int num) {
        SimpleEasyEastProject.RoundEmitterRotation = 0.0;
        SimpleEasyEastProject.addTheta = 0.5;
        SimpleEasyEastProject.RoundEmitterNum = num;
    }

    private void EmitterType2(double rotation, double theta, int num) {
        SimpleEasyEastProject.RoundEmitterRotation = rotation;
        SimpleEasyEastProject.addTheta = theta;
        SimpleEasyEastProject.RoundEmitterNum = num;
    }
}
