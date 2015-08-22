package self.ebolo.progressmanager.appcentral.utils;

import it.gmariotti.cardslib.library.internal.Card;

import java.util.ArrayList;

public class CardManager {
    private ArrayList<Card> cards;

    public CardManager() {
        cards = new ArrayList<Card>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
