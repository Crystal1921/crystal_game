package util;

import main.SimpleEasyEastProject;

import java.util.Random;

public  class moveThread implements Runnable{
    Random random = new Random();
    @Override
    public void run() {
    sleep(1000);
    movement(750,100+random.nextInt(50),30);
    sleep(1000);
    EmitterType1(15);
    sleep(1000);
    moveAttack(750,100+random.nextInt(50),-150);
    EmitterType2(1,0.5,15);
    sleep(500);
    EmitterType1(10);
    sleep(500);
    EmitterType1(15);
    movement(750,50+random.nextInt(50),-90);
    moveAttack(750,50+random.nextInt(50),90);
    EmitterType2(1,0.5,15);
    movement(750,100+random.nextInt(100),75);
    sleep(250);
    moveAttack(500,200+ random.nextInt(150),-35);
    moveAttack(250,200+ random.nextInt(150),180);
    }
    private void sleep(long sleep) {
        try {
            if ( sleep <= 0 ) sleep = 0;
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void movement (int sleepDuration, int distance, double theta) {
        sleep(sleepDuration);
        int emitterNum = SimpleEasyEastProject.RoundEmitterNum;
        SimpleEasyEastProject.RoundEmitterNum = 0;
        for (double i = 0; i < distance; i++) {
            SimpleEasyEastProject.cartesian.addX(Math.cos(Math.toRadians(theta)));
            SimpleEasyEastProject.cartesian.addY(Math.sin(Math.toRadians(theta)));
            sleep(10);
        }
        SimpleEasyEastProject.RoundEmitterNum = emitterNum;
    }
    private void moveAttack (int sleepDuration, int distance, double theta) {
        sleep(sleepDuration);
        for (double i = 0; i < distance; i++) {
            SimpleEasyEastProject.cartesian.addX(Math.cos(Math.toRadians(theta)));
            SimpleEasyEastProject.cartesian.addY(Math.sin(Math.toRadians(theta)));
            sleep(10);
        }
    }
    //Direct Emitter
    private void EmitterType1 (int num) {
        SimpleEasyEastProject.RoundEmitterRotation = 0;
        SimpleEasyEastProject.addTheta = 0.5;
        SimpleEasyEastProject.RoundEmitterNum = num;
    }
    //Round Emitter
    private void EmitterType2 (double rotation, double theta, int num) {
        SimpleEasyEastProject.RoundEmitterRotation = rotation;
        SimpleEasyEastProject.addTheta = theta;
        SimpleEasyEastProject.RoundEmitterNum = num;
    }
}
