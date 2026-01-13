package yourmod.actions;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

/**
 * Action that applies a card modifier to a card in hand
 * Only applies if the card doesn't already have that modifier
 */
public class ApplyModToHandCardAction extends AbstractGameAction {

    private static final String TEXT = "enhance.";
    private AbstractCardModifier modifier;
    private String modifierIdentifier;
    private boolean pickedCard = false;

    public ApplyModToHandCardAction(AbstractCardModifier modifier, String modifierIdentifier) {
        this.modifier = modifier;
        this.modifierIdentifier = modifierIdentifier;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // Check if hand is empty
            if (AbstractDungeon.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }

            // Get all cards in hand that don't already have this modifier
            ArrayList<AbstractCard> validCards = new ArrayList<>();
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (!CardModifierManager.hasModifier(card, modifierIdentifier)) {
                    validCards.add(card);
                }
            }

            // If no valid cards, just end
            if (validCards.isEmpty()) {
                this.isDone = true;
                return;
            }

            // Make valid cards glow
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (validCards.contains(card)) {
                    card.beginGlowing();
                } else {
                    card.stopGlowing();
                }
            }

            this.pickedCard = true;
            AbstractDungeon.handCardSelectScreen.open(TEXT, 1, true, true);
            tickDuration();
            return;
        }

        if (this.pickedCard) {
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                // Stop all cards from glowing
                for (AbstractCard card : AbstractDungeon.player.hand.group) {
                    card.stopGlowing();
                }

                if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                    AbstractCard selectedCard = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);

                    // Only apply if it doesn't already have the modifier
                    if (!CardModifierManager.hasModifier(selectedCard, modifierIdentifier)) {
                        CardModifierManager.addModifier(selectedCard, modifier.makeCopy());
                        selectedCard.superFlash();
                    }

                    // Return the card to hand
                    AbstractDungeon.player.hand.addToTop(selectedCard);
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                this.isDone = true;
                return;
            }
        }

        tickDuration();
    }
}