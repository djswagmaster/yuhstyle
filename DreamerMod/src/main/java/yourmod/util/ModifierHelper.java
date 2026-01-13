package yourmod.util;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ModifierHelper {

    public static int getModifierCount(AbstractCard card) {
        int count = 0;

        if (card.exhaust) count++;
        if (card.isEthereal) count++;
        if (card.isInnate) count++;
        if (card.selfRetain) count++;
        if (card.cost == -2) count++;

        try {
            count += CardModifierManager.modifiers(card).size();
        } catch (Exception e) {

        }

        return count;
    }

    public static boolean hasAnyModifier(AbstractCard card) {
        return getModifierCount(card) > 0;
    }
}