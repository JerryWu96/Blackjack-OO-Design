/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 */

public class BlackjackGameLogger extends GameLogger {

    public void welcomeMsg() {
        System.out.println("Welcome to our game!");
        System.out.println("Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/23/2019.");
        System.out.println("########\n");
    }

    public void displaySetDefaultParams() {
        System.out.println("Before we start, do you want to change the parameters of the game?");
        System.out.println("Default dealer stopping value is 17, and default balance for each player is 100.");
        System.out.println("Enter Y/y to change. All other inputs means using default parameter.");
    }

    public void displaySetDealerParam() {
        System.out.println("Please enter a number between 16 and 18 as dealer's stopping value. ");
        System.out.println("All other inputs means using default parameter.");
    }

    public void displaySetBalanceParam() {
        System.out.println("Pleae enter a number larger than 1 as balance for each player.");
        System.out.println("All other inputs means using default parameter.");
    }

    public void displaySetPlayerCountInfo(int max_player) {
        System.out.println("Please tell us how many players will join the game.");
        System.out.println("We do not allow new players to join after the game starts.");
        System.out.println("The max number of players allowed is " + max_player + ".");
        System.out.println("Please enter an integer between 1 and " + max_player + ": ");
    }

    public void displayInvalidMsgForRange(int min_number, int max_number) {
        System.out.println("Invalid input. Please enter a number between " + min_number + " and " + max_number + ": ");
    }

    public void playHandInfo(int playerId, int playerBalance, int handIdx, int bet) {
        int displayedHandIdx = handIdx + 1;
        System.out.println("Player " + playerId + ", Hand no." + displayedHandIdx + ", Bet for this hand = $" +
                bet + ", Balance = $" + playerBalance );
    }

    public void displayDealerCard(BlackjackCard card) {
        System.out.println("Dealer's face-up card: " + card);
    }

    public void displayDealerHand(BlackjackHand hand) {
        System.out.println("Dealer's current hand is: \n" + hand);
        System.out.println("Dealer's current hand has value: " + hand.getTotalValue() + "\n");
    }

    public void displayPlayerHand(BlackjackHand hand) {
        System.out.println("Your current hand is: " + hand);
        System.out.println("Your current hand has value: " + hand.getTotalValue() + "\n");
    }


    public void displayActionChoices(String[] actions) {
        System.out.println("Please select your next action with its corresponding number (e.g., 1 to hit):");
        int idx = 1;
        for (String action : actions) {
            System.out.print(action + ": " + idx++ + "\t");
        }
        System.out.println();
    }

    public void printPlayerBalance(int playerId, int roundBalance, int playerBalance, int roundNum) {
        if (roundBalance > 0)
            System.out.println("At round " + roundNum + ", Player " + playerId + " wins $" + roundBalance + ".");
        else if (roundBalance == 0)
            System.out.println("At round " + roundNum + ", Player " + playerId + " is tie.");
        else
            System.out.println("At round " + roundNum + ", Player " + playerId + " loses $" + -roundBalance + ".");
        System.out.println("Player " + playerId + " current balance is $" + playerBalance);
    }

    public void playerLeaves(BlackjackPlayer player) {
        System.out.println("Player " + player.getId() + " leaves the game with $" + player.getBalance());
    }
}