package util;

import main.SimpleEasyEastProject;

public  class moveThread implements Runnable{

    @Override
    public void run() {
    movement(2000,100,30);
    sleep(2000);
    EmitterType1(10);
    sleep(2000);
    EmitterType2(1,0.5,15);
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
    //Direct Emitter
    private void EmitterType1 (int num) {
        SimpleEasyEastProject.RoundEmitterRotation = 0;
        SimpleEasyEastProject.addTheta = 0;
        SimpleEasyEastProject.RoundEmitterNum = num;
    }

    private void EmitterType2 (double rotation, double theta, int num) {
        SimpleEasyEastProject.RoundEmitterRotation = rotation;
        SimpleEasyEastProject.addTheta = theta;
        SimpleEasyEastProject.RoundEmitterNum = num;
    }
}
