package yourmod.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class RetainModifier extends AbstractCardModifier {

    private static final String ID = "RETAIN";

    public RetainModifier() {
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return "Retain. NL " + rawDescription;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !card.selfRetain && !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.selfRetain = true;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new RetainModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}