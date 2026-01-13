package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import yourmod.util.ModifierHelper;

public class GainEnergyFromModifiersAction extends AbstractGameAction {
    private static final String TEXT = "gain Energy equal to its modifiers.";
    private boolean pickCard = true;

    public GainEnergyFromModifiersAction() {
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

            // Check if any card has modifiers
            boolean hasValidCard = false;
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (ModifierHelper.hasAnyModifier(card)) {
                    hasValidCard = true;
                    break;
                }
            }

            // If no cards have modifiers, end the action
            if (!hasValidCard) {
                this.isDone = true;
                return;
            }

            // Open hand card select screen
            AbstractDungeon.handCardSelectScreen.open(TEXT, 1, true, true);
            tickDuration();
            return;
        }

        // Check if a card was selected
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                AbstractCard selectedCard = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);

                // Count modifiers and gain that much energy
                int modifierCount = ModifierHelper.getModifierCount(selectedCard);
                if (modifierCount > 0) {
                    AbstractDungeon.player.gainEnergy(modifierCount);

                    // Flash effect
                    selectedCard.superFlash();
                }

                // Return the card to hand
                AbstractDungeon.player.hand.addToTop(selectedCard);
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }

        tickDuration();
    }
}