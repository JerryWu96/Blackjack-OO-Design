/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 */

public interface BlackjackAction {

    /**
     * Deals one card
     * @param deck instance of deck
     * @param hand instance of hand
     */
    void hit(BlackjackDeck deck, BlackjackHand hand);

    /**
     * The player could split into two hands, if the initial two cards are the same rank
     * @param player instance of player
     * @param hand instance of hand
     */
    void split(BlackjackPlayer player, BlackjackHand hand);

    /**
     * The player double up their bets and immediately followed by a hit and stand
     * @param deck instance of deck
     * @param hand instance of hand
     */
    void doubleUp(BlackjackDeck deck, BlackjackPlayer player, BlackjackHand hand);

    /**
     * the player ends and maintains the value of current hand
     */
    void stand();

    /**
     * Ask player to make initial bet
     * @param player - a player instance
     */
    void makeBet(BlackjackPlayer player);

    /**
     * Ask the player if he/she would like to cash out.
     * @param player - a player instance
     * @return true if player cash out, false otherwise
     */
    boolean cashOut(BlackjackPlayer player);
}