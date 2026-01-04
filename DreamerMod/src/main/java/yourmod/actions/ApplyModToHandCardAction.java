package yourmod.actions;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

/**
 * Action that applies a card modifier to a card in hand
 * Only applies if the card doesn't already have that modifier
 */
public class ApplyModToHandCardAction extends AbstractGameAction {

    private static final String TEXT = "Choose a card to enhance.";
    private AbstractCardModifier modifier;
    private String modifierIdentifier;
    private boolean canCancel;

    public ApplyModToHandCardAction(AbstractCardModifier modifier, String modifierIdentifier) {
        this(modifier, modifierIdentifier, true);
    }

    public ApplyModToHandCardAction(AbstractCardModifier modifier, String modifierIdentifier, boolean canCancel) {
        this.modifier = modifier;
        this.modifierIdentifier = modifierIdentifier;
        this.canCancel = canCancel;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // Get all cards in hand that don't already have this modifier
            ArrayList<AbstractCard> validCards = new ArrayList<>();
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                // Check if card already has this modifier
                if (!CardModifierManager.hasModifier(card, modifierIdentifier)) {
                    validCards.add(card);
                }
            }

            // If no valid cards, just end
            if (validCards.isEmpty()) {
                this.isDone = true;
                return;
            }

            // If only one valid card and can't cancel, auto-select it
            if (validCards.size() == 1 && !canCancel) {
                CardModifierManager.addModifier(validCards.get(0), modifier.makeCopy());
                this.isDone = true;
                return;
            }

            // Create a temporary card group with only valid cards
            CardGroup tempGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : validCards) {
                tempGroup.addToTop(card);
            }

            // Open card selection screen
            AbstractDungeon.gridSelectScreen.open(
                    tempGroup,
                    1,
                    TEXT,
                    false,
                    false,
                    canCancel,
                    false
            );

            tickDuration();
            return;
        }

        // Check if a card was selected
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard selectedCard = AbstractDungeon.gridSelectScreen.selectedCards.get(0);

            // Apply the modifier to the selected card (make a copy to avoid sharing)
            CardModifierManager.addModifier(selectedCard, modifier.makeCopy());

            // Clear the selection
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        // Close the grid if it's still open
        if (AbstractDungeon.gridSelectScreen.targetGroup.size() > 0) {
            AbstractDungeon.gridSelectScreen.targetGroup.clear();
        }

        tickDuration();
    }
}