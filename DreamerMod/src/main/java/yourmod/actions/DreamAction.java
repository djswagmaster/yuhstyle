package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.dream.DreamManager;
import yourmod.tags.CustomTags;

public class DreamAction extends AbstractGameAction {
    private static final String TEXT = "Dream of...";
    public static boolean isActive = false;

    public DreamAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // Check if any cards can be inspired
            boolean hasValidCards = false;
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                // Exclude Dream cards from the check
                if (!card.hasTag(CustomTags.DREAMER_CARD) && DreamManager.getInstance().canInspire(card)) {
                    hasValidCards = true;
                    break;
                }
            }

            // If no valid cards, just end
            if (!hasValidCards) {
                this.isDone = true;
                return;
            }

            // Set active BEFORE opening the screen
            isActive = true;

            // Make valid cards glow
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (!card.hasTag(CustomTags.DREAMER_CARD) && DreamManager.getInstance().canInspire(card)) {
                    card.beginGlowing();
                } else {
                    card.stopGlowing();
                }
            }

            // Open hand select screen - true, true allows skipping
            AbstractDungeon.handCardSelectScreen.open(TEXT, 1, true, true);

            tickDuration();
            return;
        }

        // Check if a card was selected
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            // Stop all cards from glowing
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                card.stopGlowing();
            }

            // Only process if cards were actually selected
            if (AbstractDungeon.handCardSelectScreen.selectedCards.size() > 0) {
                for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    // Only inspire if it's valid and not a Dream card
                    if (!card.hasTag(CustomTags.DREAMER_CARD) && DreamManager.getInstance().canInspire(card)) {
                        // Check if there's already a card in the dream slot
                        if (DreamManager.getInstance().hasCardInSlot()) {
                            AbstractCard existingDream = DreamManager.getInstance().getCardInSlot();
                            // Add a copy of the existing dream card to hand
                            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(existingDream.makeStatEquivalentCopy()));
                        }

                        DreamManager.getInstance().inspireCard(card);
                    }

                    // Exhaust the card
                    AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                }
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();

            // Set inactive AFTER processing is complete
            isActive = false;
        }

        tickDuration();
    }
}