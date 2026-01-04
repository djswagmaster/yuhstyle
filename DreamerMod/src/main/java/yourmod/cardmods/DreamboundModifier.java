package yourmod.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

/**
 * Dreambound modifier - reduces dream cost by 1
 */
public class DreamboundModifier extends AbstractCardModifier {

    public DreamboundModifier() {
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        // Add "Dreambound" keyword to description
        return "dreamermod:Dreambound. NL " + rawDescription;
    }

    /**
     * This is called by DreamManager when calculating dream cost
     */
    public int getDreamCostReduction() {
        return 1;
    }

    @Override
    public String identifier(AbstractCard card) {
        return "DREAMBOUND";
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DreamboundModifier();
    }

    /**
     * Helper method to check if a card has dreambound
     */
    public static boolean isDreambound(AbstractCard card) {
        return CardModifierManager.hasModifier(card, "DREAMBOUND");
    }
}