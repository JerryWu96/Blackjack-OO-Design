/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/23/2019.
 */

public class BlackjackPlayer extends Player {

    private int balance;

    /**
     * Constructor.
     * @param id player id.
     * @param balance Initial balance for the player.
     */
    public BlackjackPlayer(int id, int balance) {
        super(id);
        this.balance = balance;
        addHand(new BlackjackHand());
    }

    /**
     * Getter for player's balance
     * @return player balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Setter for player's balance
     * @param currency player balance
     */
    public void setBalance(int currency) {
        balance += currency;
    }

    /**
     * Getter for hands.
     * @return the number of hands.
     */
    public int getHandCount() {
        return getHands().size();
    }

    /**
     * Get the hand.
     * @param idx index of the hand.
     * @return an instance of BlackjackHand.
     */
    public BlackjackHand getHandAt(int idx) {
        return (BlackjackHand) getHands().get(idx);
    }
}
