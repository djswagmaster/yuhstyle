package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import yourmod.dream.DreamManager;
import yourmod.dream.DreamSlotRenderer;
import yourmod.tags.CustomTags;
import yourmod.ui.DreamSlotPanel;

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
                AbstractCard selectedCard = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);

                // Only inspire if it's valid and not a Dream card
                if (!selectedCard.hasTag(CustomTags.DREAMER_CARD) && DreamManager.getInstance().canInspire(selectedCard)) {
                    // CRITICAL FIX: Clear the slot COMPLETELY before adding new card
                    if (DreamManager.getInstance().hasCardInSlot()) {
                        // Get and remove old card SYNCHRONOUSLY and COMPLETELY
                        AbstractCard oldCard = DreamManager.getInstance().getCardInSlot();

                        // First, remove from dream pile completely
                        DreamSlotPanel.dreamPile.removeCard(oldCard);

                        // Reset the old card's state
                        DreamSlotPanel.resetCardVisuals(oldCard);
                        oldCard.resetAttributes();
                        oldCard.applyPowers();

                        // Add old card back to hand or discard
                        if (AbstractDungeon.player.hand.size() < 10) {
                            AbstractDungeon.player.hand.addToTop(oldCard);
                        } else {
                            AbstractDungeon.player.discardPile.addToTop(oldCard);
                        }

                        // Reset dream manager state
                        DreamManager.getInstance().resetSlotState();
                    }

                    // Play dreamy SFX
                    CardCrawlGame.sound.play("POWER_FLIGHT", 0.2f);
                    CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.3f);

                    // VFX at card location
                    AbstractDungeon.effectsQueue.add(new ExhaustCardEffect(selectedCard));

                    // VFX at dream slot
                    AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(
                            DreamSlotRenderer.getSlotX(),
                            DreamSlotRenderer.getSlotY(),
                            AttackEffect.NONE
                    ));

                    // The card is already removed from hand by the select screen
                    // Now inspire it (stores reference)
                    DreamManager.getInstance().inspireCard(selectedCard);
                } else {
                    // Invalid card - return it to hand
                    AbstractDungeon.player.hand.addToTop(selectedCard);
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