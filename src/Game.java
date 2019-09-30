/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 */

public abstract class Game {

    private int round;

    public Game() {
        this.round = 1;
    }

    abstract void start();

    int getRound() {
        return round;
    }

    void setRound(int round) {
        this.round = round;
    }

    abstract void playARound();
}
