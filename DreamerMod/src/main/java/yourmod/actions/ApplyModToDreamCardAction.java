package yourmod.actions;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import yourmod.dream.DreamManager;

/**
 * Action that applies a card modifier to the card in the dream slot
 */
public class ApplyModToDreamCardAction extends AbstractGameAction {

    private AbstractCardModifier modifier;

    public ApplyModToDreamCardAction(AbstractCardModifier modifier) {
        this.modifier = modifier;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            DreamManager dm = DreamManager.getInstance();

            // Check if there's a card in the dream slot
            if (dm.hasCardInSlot()) {
                AbstractCard dreamCard = dm.getCardInSlot();

                // Apply the modifier to the dream card
                CardModifierManager.addModifier(dreamCard, modifier);
            }

            this.isDone = true;
            return;
        }

        tickDuration();
    }
}