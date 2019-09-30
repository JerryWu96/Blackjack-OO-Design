/**
 * Created by Jiatong Hao, Xiankang Wu and Lijun Chen on 9/27/2019.
 */

import java.util.*;

public class BlackjackDeck extends Deck {

    /**
     * Initialize the deck with cards.
     */
    public void createDeck() {
        List<BlackjackCard> cards = new ArrayList<BlackjackCard>();
        String[] suits = new String[]{"Spade", "Heart", "Club", "Diamond"};
        for (String suit : suits) {
            cards.add(new BlackjackAceCard(suit));
            for (int i = 2; i <= 10; i++) {
                cards.add(new BlackjackCard(suit, i));
            }
            for (int i = 11; i <= 13; i++) {
                cards.add(new BlackjackFaceCard(suit, i));
            }
        }
        setCards(cards);
    }
}
