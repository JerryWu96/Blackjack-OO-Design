/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 */

public class BlackjackJudge extends Judge<BlackjackPlayer, BlackjackDealer> {

    private int dealerValue;

    private int winValue;

    /**
     * Constructor.
     * @param dealerValue value that the dealer will stop hitting when his/her hand reaches. In default it is 17.
     * @param winValue value that the Blackjack refers to. In default it is 21.
     */
    public BlackjackJudge(int dealerValue, int winValue) {
        this.dealerValue = dealerValue;
        this.winValue = winValue;
    }

    public int getDealerValue() {
        return dealerValue;
    }

    public void setDealerValue(int dealerValue) {
        this.dealerValue = dealerValue;
    }

    public int getWinValue() {
        return winValue;
    }

    public void setWinValue(int winValue) {
        this.winValue = winValue;
    }

    /**
     * Tells if the action for a specific hand of a player is valid.
     * @param player current player.
     * @param hand hand that the current player is holding.
     * @param action string represents the player action.
     * @return boolean. Returns true if the action is valid, false otherwise.
     */
    public boolean isActionValid(BlackjackPlayer player, BlackjackHand hand, String action) {
        switch (action) {
            case "hit":
                return !isBust(hand);
            case "split":
                return isSplittable(player, hand);
            case "doubleUp":
                return isEnoughBalance(player, hand.getBet());
        }
        return true;
    }

    /**
     * Tells if the current hand is bust.
     * @param hand the hand instance.
     * @return True if bust, false otherwise.
     */
    public boolean isBust(BlackjackHand hand) {
        return hand.getTotalValue() > this.winValue;
    }

    private boolean isEnoughBalance(BlackjackPlayer player, int bet) {
        return player.getBalance() - bet >= 0;
    }

    private boolean isSplittable(BlackjackPlayer player, BlackjackHand hand) {
        // check if there is only two cards in this hand &  check if balance can afford two bets
        if (hand.getCardCount() != 2) return false;
        if (!isEnoughBalance(player, hand.getBet())) return false;
        int cardValue1 = hand.getCardAt(0).getHardValue();
        int cardValue2 = hand.getCardAt(1).getHardValue();
        return cardValue1 == cardValue2;
    }

    /**
     * Tells if the dealer can still hit.
     * @param dealer dealer instance.
     * @return True if the dealer can hit, false otherwise.
     */
    public boolean canDealerHit(BlackjackDealer dealer) {
        return dealer.getHand().getTotalValue() < dealerValue;
    }

    /**
     * Tells if the hand is a Blackjack. A Blackjack means that the total value of the hand cards is 21.
     * @param hand hand instance.
     * @return if the current hand is Blackjack.
     */
    public boolean isBlackjack(BlackjackHand hand) {
        return hand.getTotalValue() == this.winValue;
    }

    /**
     * Tells if the hand is a natural Blackjack. A natural Blackjack is a BlackJack with one Ace and one Face Card.
     * @param hand hand instance.
     * @return if the current hand is natural Blackjack.
     */
    public boolean isNaturalBlackjack(BlackjackHand hand) {
        return isBlackjack(hand) && hand.getCardCount() == 2;
    }

    /**
     * Compaer each hand of the player and the one of the dealer.
     * @param player instance of player.
     * @param dealer instance of dealer.
     * @return Balance that the current player wins or loses. If wins or tie, it is positive, otherwise it is negative.
     */
    public int checkWinner(BlackjackPlayer player, BlackjackDealer dealer) {
        BlackjackHand dealerHand = dealer.getHand();
        int dealerValue = dealerHand.getTotalValue();

        int roundBalance = 0;

        if (isBust(dealerHand)) {
            // if dealer is bust
            for (int i = 0; i < player.getHandCount(); i++) {
                BlackjackHand playerHand = player.getHandAt(i);
                int bet = playerHand.getBet();

                if (!isBust(playerHand)) {
                    // if not bust, player hand wins
                    player.setBalance(bet * 2);
                    roundBalance += playerHand.getBet();
                } else {
                    // if this player hand bust, both player and dealer lose, tie
                    player.setBalance(bet);
                }
            }
        } else {
            // if dealer does not bust
            for (int i = 0; i < player.getHandCount(); i++) {
                BlackjackHand playerHand = player.getHandAt(i);
                int value = playerHand.getTotalValue();
                int bet = playerHand.getBet();

                if (isBust(playerHand)) {
                    // if player hand bust, player hand loses
                    roundBalance -= bet;
                } else {
                    // if player hand not bust
                    if (value < dealerValue) {
                        // if player hand value < dealer hand value, player hand loses
                        roundBalance -= bet;
                    } else if (value == dealerValue) {
                        if (isNaturalBlackjack(dealerHand) && isNaturalBlackjack(playerHand)) {
                            // both dealer hand & player hand is natural blackjack, tie
                            player.setBalance(bet);
                        } else if (isNaturalBlackjack(dealerHand) && !isBlackjack(playerHand)) {
                            // dealer hand == natural blackjack && player hand == blackjack, player hand loses
                            roundBalance -= bet;
                        } else if (isBlackjack(dealerHand) && isNaturalBlackjack(playerHand)) {
                            // dealer hand == blackjack && player hand == natural blackjack, player hand wins
                            player.setBalance(bet * 2);
                            roundBalance += bet;
                        } else {
                            // both blackjack or neither blackjack, nor natural blackjack, tie
                            player.setBalance(bet);
                        }
                    } else {
                        // if player hand value > dealer hand value, player hand wins
                        player.setBalance(bet * 2);
                        roundBalance += bet;
                    }
                }
            }
        }
        return roundBalance;
    }
}