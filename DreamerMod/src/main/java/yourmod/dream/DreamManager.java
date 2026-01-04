package yourmod.dream;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import yourmod.CharacterFile;

public class DreamManager {
    private static DreamManager instance;

    // Dream slot state
    private AbstractCard cardInSlot;
    private int costPenalty;
    private int timesPlayed;
    private int timesDreamed;

    private DreamManager() {
        this.cardInSlot = null;
        this.costPenalty = 0;
        this.timesPlayed = 0;
        this.timesDreamed = 0;
    }

    public static DreamManager getInstance() {
        if (instance == null) {
            instance = new DreamManager();
        }
        return instance;
    }

    /**
     * Check if the dream slot should be visible to the player
     */
    public boolean isDreamSlotVisible() {
        if (AbstractDungeon.player == null) {
            return false;
        }

        // Visible if playing as Dreamer
        if (AbstractDungeon.player.chosenClass == CharacterFile.Enums.THE_DREAMER) {
            return true;
        }

        // Visible if player has Prismatic Shard
        if (AbstractDungeon.player.hasRelic(PrismaticShard.ID)) {
            return true;
        }

        // Also visible if there's a card in the slot (for edge cases)
        return cardInSlot != null;
    }

    /**
     * Check if the slot has a card
     */
    public boolean hasCardInSlot() {
        return cardInSlot != null;
    }

    /**
     * Get the card currently in the dream slot
     */
    public AbstractCard getCardInSlot() {
        return cardInSlot;
    }

    /**
     * Check if a card can be inspired (placed in dream slot)
     */
    public boolean canInspire(AbstractCard card) {
        if (card == null) {
            return false;
        }

        // Cannot inspire cards with cost < 0 (X-cost, Unplayable, etc.)
        if (card.cost < 0 || card.costForTurn < 0) {
            return false;
        }

        return true;
    }

    /**
     * Calculate the base dream cost for a card
     * Base cost + 1 if the card is Exhaust or Power type
     */
    public static int calculateBaseDreamCost(AbstractCard card) {
        int baseCost = card.cost;

        // Add +1 for exhaust or power cards
        if (card.exhaust || card.type == AbstractCard.CardType.POWER) {
            baseCost += 0;
        }

        return baseCost;
    }

    /**
     * Get the current dream cost (base + penalty - modifiers)
     */
    public int getCurrentDreamCost() {
        if (cardInSlot == null) {
            return 0;
        }

        int baseCost = calculateBaseDreamCost(cardInSlot);
        int finalCost = baseCost + costPenalty;

        // Check for card modifiers that reduce dream cost
        finalCost -= getDreamCostReductionFromModifiers(cardInSlot);

        return Math.max(0, finalCost);
    }

    /**
     * Calculate total dream cost reduction from card modifiers
     */
    private int getDreamCostReductionFromModifiers(AbstractCard card) {
        int reduction = 0;

        // Check for Dreambound modifier
        try {
            java.util.List<basemod.abstracts.AbstractCardModifier> modifiers =
                    basemod.helpers.CardModifierManager.modifiers(card);

            for (basemod.abstracts.AbstractCardModifier mod : modifiers) {
                // Check if it's a Dreambound modifier
                if (mod.identifier(card).equals("DREAMBOUND")) {
                    if (mod instanceof yourmod.cardmods.DreamboundModifier) {
                        reduction += ((yourmod.cardmods.DreamboundModifier) mod).getDreamCostReduction();
                    }
                }
            }
        } catch (Exception e) {
            // No modifiers or error
        }

        return reduction;
    }

    /**
     * Inspire a card - duplicate it into the dream slot
     * Replaces any existing card and resets cost penalty
     */
    public void inspireCard(AbstractCard card) {
        if (!canInspire(card)) {
            return;
        }

        // Make a copy of the card
        this.cardInSlot = card.makeStatEquivalentCopy();

        // Reset cost penalty and play counter
        this.costPenalty = 0;
        this.timesPlayed = 0;

        // Increment times dreamed counter
        this.timesDreamed++;
    }

    /**
     * Materialize - reduce the dream cost by the specified amount
     * Returns true if the card should be played (cost reached 0)
     */
    public boolean materialize(int amount) {
        if (cardInSlot == null || amount <= 0) {
            return false;
        }

        // Reduce the cost penalty (making cost lower)
        costPenalty -= amount;

        // If cost is now 0 or less, the card should be played
        return getCurrentDreamCost() <= 0;
    }

    /**
     * Called after a dream card is played
     * If it's exhaust or power, clear the slot
     * Otherwise, reset cost to base + number of times played
     */
    public void onDreamCardPlayed() {
        if (cardInSlot == null) {
            return;
        }

        // Clear slot for exhaust or power cards
        if (cardInSlot.exhaust || cardInSlot.type == AbstractCard.CardType.POWER) {
            clearSlot();
        } else {
            // Increment times played
            timesPlayed++;

            // Reset penalty to equal times played
            // This makes cost = base + timesPlayed
            costPenalty = timesPlayed;
        }
    }

    /**
     * Check if the dream card should auto-play (cost is 0)
     */
    public boolean shouldAutoPlay() {
        return hasCardInSlot() && getCurrentDreamCost() <= 0;
    }

    /**
     * Get the current cost penalty
     */
    public int getCostPenalty() {
        return costPenalty;
    }

    /**
     * Get the number of times the current dream card has been played
     */
    public int getTimesPlayed() {
        return timesPlayed;
    }

    /**
     * Get the number of times the player has dreamed this combat
     */
    public int getTimesDreamed() {
        return timesDreamed;
    }

    /**
     * Clear the dream slot
     */
    public void clearSlot() {
        this.cardInSlot = null;
        this.costPenalty = 0;
        this.timesPlayed = 0;
    }

    /**
     * Reset the dream manager (called at combat start)
     */
    public void onCombatStart() {
        clearSlot();
        this.timesDreamed = 0;
    }
    /**
     * Set the card in the slot directly
     * Used to update the slot with a modified card after playing
     */
    public void setCardInSlot(AbstractCard card) {
        this.cardInSlot = card;
    }
    /**
     * Update the card in the slot with a modified version's stats
     * Used after playing the dream card to preserve stat changes (like Rampage)
     */
    public void updateCardInSlot(AbstractCard playedCard) {
        if (playedCard != null && this.cardInSlot != null) {
            // Update the slot with a copy of the played card to preserve its modifications
            this.cardInSlot = playedCard.makeStatEquivalentCopy();
        }
    }
    /**
     * Clean up at end of combat
     */
    public void onCombatEnd() {
        clearSlot();
        this.timesDreamed = 0;
    }
}