/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class encapsulates a Blackjack card game.
 */
public class BlackjackGame extends Game implements BlackjackAction {
    private final int WIN_VAL = 21;
    private final int DEALER_VAL = 17;
    private final int MIN_DEALER_VAL = 16;
    private final int MAX_DEALER_VAL = 18;
    private final int MAX_PLAYER = 4;
    private final int BALANCE = 100;
    private final int INITIAL_CARD_NUM = 2;

    private List<BlackjackPlayer> playerList;
    private BlackjackDealer dealer;
    private BlackjackDeck deck;
    private BlackjackJudge judge;
    private BlackjackGameLogger visualizer;
    private final String[] actions = {"hit", "stand", "doubleUp", "split"};

    private int winVal = WIN_VAL;
    private int dealerVal = DEALER_VAL;
    private int balance = BALANCE;
    private int playerCount;


    public BlackjackGame() {
        visualizer = new BlackjackGameLogger();
        visualizer.welcomeMsg();
        setGameParams();
        setPlayerNumber();
        initGame();
    }

    /**
     * Entry method of the BlackjackGame.
     */
    public void start() {
        System.out.println("\nGame starts!");
        while (!playerList.isEmpty()) {
            playARound();
            resetHands();
        }
        System.out.println("\nGame ends.");
    }

    /**
     * The main workflow of players in a single round. First, each player makes their bets, and then cards are dealt,
     * each player makes their move, dealer plays, and the round is wrapped up.
     */
    public void playARound() {
        System.out.println("\n*****************\nRound: " + getRound());

        for (BlackjackPlayer player : playerList) {
            makeBet(player);
        }
        dealInitialCards();
        playersPlay();
        dealerPlay();
        calcRoundResult();
        setRound(getRound()+1);
    }

    /**
     * Initialize game params of BlackJack Game.
     */
    private void setGameParams() {
        Scanner sc = new Scanner(System.in);
        visualizer.displaySetDefaultParams();
        String choice = sc.nextLine();
        if (!choice.equals("y") && !choice.equals("Y")) {
            System.out.println("The game will use default parameters.\n");
            return;
        }

        setDealerParam();
        setPlayerBalance();
        System.out.println("The game will use " + dealerVal + " as dealer stopping value and " + balance + " as balance.\n");
    }

    private void setDealerParam() {
        visualizer.displaySetDealerParam();
        Scanner scanner = new Scanner(System.in);
        int dealerValInput = getInteger(scanner.nextLine());
        if (dealerValInput >= MIN_DEALER_VAL && dealerValInput <= MAX_DEALER_VAL) {
            this.dealerVal = dealerValInput;
        }
    }

    private void setPlayerBalance() {
        visualizer.displaySetBalanceParam();
        Scanner scanner = new Scanner(System.in);
        int balanceInput = getInteger(scanner.nextLine());
        if (balanceInput > 1) {
            this.balance = balanceInput;
        }
    }


    private void setPlayerNumber() {
        visualizer.displaySetPlayerCountInfo(MAX_PLAYER);
        Scanner scanner = new Scanner(System.in);
        boolean isPlayerValid = false;
        int playerCountInput = -1;
        while (!isPlayerValid) {
            playerCountInput = getInteger(scanner.nextLine());
            if (1 <= playerCountInput && playerCountInput <= MAX_PLAYER) {
                System.out.println("The game will have " + playerCountInput + " player(s) in the beginning.");
                isPlayerValid = true;
            } else {
                visualizer.displayInvalidMsgForRange(1, MAX_PLAYER);
            }
        }
        this.playerCount = playerCountInput;
    }


    private void initGame() {
        deck = new BlackjackDeck();
        judge = new BlackjackJudge(dealerVal, winVal);
        dealer = new BlackjackDealer();
        playerList = new ArrayList<>(playerCount);
        for (int i = 0; i < playerCount; i++)
            playerList.add(new BlackjackPlayer(i+1, balance));
    }

    /**
     * Initialize two cards to both players and dealers in alternating sequence.
     */
    private void dealInitialCards() {
        for (int idx = 0; idx < this.INITIAL_CARD_NUM; idx++) {
            for (BlackjackPlayer player : playerList) {
                BlackjackCard newCard = (BlackjackCard) deck.dealCard();
                player.getHandAt(0).addCard(newCard);
            }
            BlackjackCard newCard = (BlackjackCard) deck.dealCard();
            dealer.getHand().addCard(newCard);
        }
    }

    /**
     * The workflow of all players selecting their actions in a single round.
     */
    private void playersPlay() {
        for (BlackjackPlayer player : playerList) {
            System.out.println("\n#################\nPlayer " + player.getId() + " starts!");
            for (int i = 0; i < player.getHandCount(); i++) {
                BlackjackHand hand = player.getHandAt(i);
                visualizer.playHandInfo(player.getId(), player.getBalance(), i, hand.getBet());
                visualizer.displayDealerCard(dealer.getVisibleCard());

                if (judge.isNaturalBlackjack(hand)) {
                    visualizer.displayPlayerHand(hand);
                    System.out.println("Your current hand is a Natural Blackjack! Gorgeous!!!");
                    continue;
                }

                while (!judge.isBust(hand) && !judge.isBlackjack(hand)) {
                    visualizer.displayPlayerHand(hand);

                    String next_action = getUserAction(player, hand);
                    playAction(player, next_action, hand);
                    if (next_action.equals("stand") || next_action.equals("doubleUp")) {
                        break;
                    }
                }

                visualizer.displayPlayerHand(hand);

                if (judge.isBlackjack(hand)) {
                    System.out.println("Your current hand is a Blackjack! Congrats!");
                }
                if (judge.isBust(hand)) {
                    int displayedIdx = i + 1;
                    System.out.println("Player " + player.getId() + " hand " + displayedIdx + " is Bust!");
                }
            }
        }
        System.out.println("\nAll players' terms end!");
    }

    private int getInteger(String str) {
        try {
            int res = Integer.parseInt(str);
            return res;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Ask the player to select the next action, and decide if it is valid.
     * @param player current player.
     * @param hand current hand the player is dealing with.
     * @return string of the action.
     */
    private String getUserAction(BlackjackPlayer player, BlackjackHand hand) {
        Scanner sc = new Scanner(System.in);
        boolean isValid = false;
        int input = -1;
        while (!isValid) {
            visualizer.displayActionChoices(actions);
            // if input is valid, change isValid = true
            input = getInteger(sc.nextLine());
            if (1 <= input && input <= actions.length && judge.isActionValid(player, hand, actions[input-1])) {
                System.out.println("Your action: " + actions[input - 1]);
                isValid = true;
            } else {
                visualizer.displayInvalidMsgForRange(1, actions.length);
            }
        }
        return actions[input - 1];
    }

    /**
     * Execute the action selected by a single player with factory design pattern.
     * @param player current player.
     * @param action current action selected by the player.
     * @param hand current hand the player is dealing with.
     */
    private void playAction(BlackjackPlayer player, String action, BlackjackHand hand) {
        switch (action) {
            case "hit":
                hit(deck, hand);
                break;
            case "stand":
                stand();
                break;
            case "doubleUp":
                doubleUp(deck, player, hand);
                break;
            case "split":
                split(player, hand);
                break;
        }
    }

    /**
     * logic of dealer. Our dealer will keep on hitting until his/her cards reaches the dealerVal, or bust.
     */
    private void dealerPlay() {
        System.out.println("\n#################\nDealer starts!");

        BlackjackHand dealerHand = dealer.getHand();

        visualizer.displayDealerHand(dealerHand);

        if (judge.isNaturalBlackjack(dealerHand)) {
            System.out.println("Dealer's current hand is a Natural Blackjack! Gorgeous!!!");
        }

        while (judge.canDealerHit(dealer)) {
            hit(deck, dealer.getHand());
            System.out.println("Dealer hits!");
            visualizer.displayDealerHand(dealerHand);
        }

        if (judge.isBlackjack(dealerHand)) {
            System.out.println("Your current hand is a Blackjack! Congrats!");
        }

        if (judge.isBust(dealerHand)) {
            System.out.println("Dealer hand is Bust!");
        }

        System.out.println("Dealer's term ends!");
        System.out.println("#################\n");
    }

    /**
     * Remove players with $0 balance, and ask other players if they would like to cash out or join the next round.
     */
    private void calcRoundResult() {
        List<BlackjackPlayer> toRemove = new ArrayList<>();
        for (BlackjackPlayer player : playerList) {
            int roundBalance = judge.checkWinner(player, dealer);
            visualizer.printPlayerBalance(player.getId(), roundBalance, player.getBalance(), getRound());
            if (player.getBalance() == 0) {
                visualizer.playerLeaves(player);
                toRemove.add(player);
            }
            else if (cashOut(player)) {
                visualizer.playerLeaves(player);
                toRemove.add(player);
            }
        }
        for (BlackjackPlayer player: toRemove) {
            playerList.remove(player);
        }
    }

    /**
     * Reset hands of players and dealer.
     */
    private void resetHands() {
        dealer.clearHands();
        dealer.addHand(new BlackjackHand());
        for (BlackjackPlayer player : playerList) {
            player.clearHands();
            player.addHand(new BlackjackHand());
        }
    }

    /**
     * Deals one card
     * @param deck instance of deck
     * @param hand instance of hand
     */
    @Override
    public void hit(BlackjackDeck deck, BlackjackHand hand) {
        BlackjackCard newCard = (BlackjackCard) deck.dealCard();
        hand.addCard(newCard);
    }

    /**
     * The player could split into two hands, if the two cards in the current hand are of the same rank （10）
     * Cards must be same value - In most casinos, Tens, Jacks, Queens and Kings all count as ten
     * and can be considered the same for splitting rules.
     * @param player instance of player
     * @param hand instance of hand
     */
    @Override
    public void split(BlackjackPlayer player, BlackjackHand hand) {
        BlackjackCard card = hand.getCardAt(0);
        BlackjackHand newHand = new BlackjackHand(card);
        int handBet = hand.getBet();
        hand.removeCard(card);
        player.addHand(newHand);
        int handCount = player.getHandCount();
        player.getHandAt(handCount - 1).setBet(handBet);
        player.setBalance(-handBet);
    }

    /**
     * The player double up their bets and immediately followed by a hit and stand
     * @param deck instance of deck
     * @param hand instance of hand
     */
    @Override
    public void doubleUp(BlackjackDeck deck, BlackjackPlayer player, BlackjackHand hand) {
        player.setBalance(-hand.getBet());
        hand.setBet(hand.getBet() * 2);
        hit(deck, hand);
    }

    /**
     * Player stands means that he/she finish the action of the current hand.
     */
    @Override
    public void stand() {
        return;
    }

    /**
     * Ask player to make initial bet
     * @param player - a player instance
     */
    @Override
    public void makeBet(BlackjackPlayer player) {
        Scanner sc = new Scanner(System.in);
        int input;
        boolean isValid = false;
        System.out.println("Current balance of player " + player.getId() + " is: " + player.getBalance());
        System.out.println("Player " + player.getId() + ", please enter an integer between 1 and " + player.getBalance() + " as bet: ");

        while (!isValid) {
            input = getInteger(sc.nextLine());
            if (input >= 1 && input <= player.getBalance()) {
                isValid = true;
                player.getHandAt(0).setBet(input);
                player.setBalance(-input);
            } else {
                System.out.println("Invalid input. Please enter an integer between 1 and " + player.getBalance() + " as bet: ");
            }
        }
    }

    /**
     * Ask the player if he/she would like to cash out.
     * @param player - a player instance
     * @return true if player cash out, false otherwise
     */
    @Override
    public boolean cashOut(BlackjackPlayer player) {

        Scanner scanner = new Scanner(System.in);
        boolean isCashOut = false;
        System.out.println("Player " + player.getId() + ", do you want to cash out? Please enter Y/y for yes. All other input means no.");
        String choice = scanner.nextLine();
        if (choice.equals("y") || choice.equals("Y")) {
            isCashOut = true;
        }
        return isCashOut;
    }
}