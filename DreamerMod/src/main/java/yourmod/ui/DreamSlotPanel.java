package yourmod.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;

/**
 * Custom panel that holds the Dream slot's CardGroup.
 * This ensures the card "exists" in game state (for Rampage, etc.)
 * while not being rendered by the normal pile rendering.
 */
public class DreamSlotPanel extends AbstractPanel {
    private static final float SHOW_X = Settings.WIDTH * 0.38f;
    private static final float SHOW_Y = Settings.HEIGHT * 0.45f;
    private static final float HIDE_X = -480.0F * Settings.scale;
    private static final float HIDE_Y = Settings.HEIGHT * 0.45f;

    // The custom CardGroup that holds our dream card
    // Using UNSPECIFIED type like limbo, but it's our own pile
    public static CardGroup dreamPile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    public DreamSlotPanel() {
        super(SHOW_X, SHOW_Y, HIDE_X, HIDE_Y, null, false);
    }

    @Override
    public void updatePositions() {
        super.updatePositions();
        // Update cards in the dream pile to prevent visual glitches
        if (!dreamPile.isEmpty()) {
            for (AbstractCard card : dreamPile.group) {
                // Prevent the card from fading out or doing weird things
                card.fadingOut = false;
                card.transparency = 1.0f;
                card.targetTransparency = 1.0f;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        // Don't render anything here - DreamSlotRenderer handles all rendering
        // This panel just holds the CardGroup
    }

    /**
     * Add a card to the dream pile
     */
    public static void addCard(AbstractCard card) {
        // CRITICAL SAFETY CHECK: Ensure pile is empty before adding
        if (!dreamPile.isEmpty()) {
            System.err.println("WARNING: Dream pile not empty when adding new card! Clearing...");
            System.err.println("  Current pile size: " + dreamPile.size());
            for (int i = 0; i < dreamPile.size(); i++) {
                System.err.println("  Card " + i + ": " + dreamPile.group.get(i).name);
            }

            // Emergency clear - this shouldn't happen but prevents corruption
            AbstractCard oldCard = dreamPile.group.get(0);
            dreamPile.clear();

            // Try to recover the old card
            if (AbstractDungeon.player != null && AbstractDungeon.player.hand.size() < 10) {
                oldCard.resetAttributes();
                AbstractDungeon.player.hand.addToTop(oldCard);
                System.err.println("  Recovered old card to hand: " + oldCard.name);
            } else {
                System.err.println("  Old card lost (hand full)");
            }
        }

        // Remove from all other piles first
        if (AbstractDungeon.player != null) {
            AbstractDungeon.player.hand.removeCard(card);
            AbstractDungeon.player.discardPile.removeCard(card);
            AbstractDungeon.player.drawPile.removeCard(card);
            AbstractDungeon.player.exhaustPile.removeCard(card);
            AbstractDungeon.player.limbo.removeCard(card);
        }

        // Add to our pile (should always be empty at this point)
        dreamPile.addToTop(card);

        // Reset visual state
        resetCardVisuals(card);
    }

    /**
     * Remove a card from the dream pile
     */
    public static void removeCard(AbstractCard card) {
        dreamPile.removeCard(card);
    }

    /**
     * Get the card in the dream pile (should only be one)
     */
    public static AbstractCard getCard() {
        if (dreamPile.isEmpty()) {
            return null;
        }
        return dreamPile.group.get(0);
    }

    /**
     * Check if there's a card in the dream pile
     */
    public static boolean hasCard() {
        return !dreamPile.isEmpty();
    }

    /**
     * Clear the dream pile
     */
    public static void clear() {
        dreamPile.clear();
    }

    /**
     * Reset a card's visual state
     */
    public static void resetCardVisuals(AbstractCard card) {
        if (card == null) return;
        card.unfadeOut();
        card.unhover();
        card.stopGlowing();
        card.transparency = 1.0f;
        card.targetTransparency = 1.0f;
        card.fadingOut = false;
        card.angle = 0f;
        card.targetAngle = 0f;
        card.drawScale = 0.75f;
        card.targetDrawScale = 0.75f;
    }

    /**
     * Return the card to hand (or discard if hand is full)
     */
    public static void returnCardToHand(AbstractCard card) {
        if (card == null) return;

        // Remove from dream pile
        dreamPile.removeCard(card);

        // Reset visuals
        resetCardVisuals(card);
        card.resetAttributes();
        card.applyPowers();

        // Add to hand or discard
        if (AbstractDungeon.player != null) {
            if (AbstractDungeon.player.hand.size() < 10) {
                AbstractDungeon.player.hand.addToTop(card);
            } else {
                AbstractDungeon.player.discardPile.addToTop(card);
            }
        }
    }

    /**
     * After a card is played, remove it from wherever it ended up
     * and put it back in the dream pile
     */
    public static void returnCardToDreamPile(AbstractCard card) {
        if (card == null) return;

        // Remove from wherever it ended up
        if (AbstractDungeon.player != null) {
            AbstractDungeon.player.discardPile.removeCard(card);
            AbstractDungeon.player.exhaustPile.removeCard(card);
            AbstractDungeon.player.hand.removeCard(card);
            AbstractDungeon.player.drawPile.removeCard(card);
            AbstractDungeon.player.limbo.removeCard(card);
        }

        // Make sure it's in dream pile
        if (!dreamPile.contains(card)) {
            dreamPile.addToTop(card);
        }

        // Reset visuals
        resetCardVisuals(card);
    }
}