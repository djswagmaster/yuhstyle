package yourmod.dream;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import yourmod.CharacterFile;
import yourmod.powers.DeepConnectionPower;
import yourmod.powers.DreamDrawPower;
import yourmod.powers.DreamShieldPower;
import yourmod.powers.LucidDreamerPower;
import yourmod.powers.LucidityPower;
import yourmod.powers.PermanencePower;
import yourmod.ui.DreamSlotPanel;

import java.util.ArrayList;

public class DreamManager {
    private static DreamManager instance;

    // Dream slot state
    // The actual card is stored in DreamSlotPanel.dreamPile
    private int originalBaseCost; // Store the original cost when inspired (immune to Snecko)
    private int costPenalty;
    private int timesPlayed;
    private int timesDreamed;

    // Pending modifiers for card rewards
    private ArrayList<basemod.abstracts.AbstractCardModifier> pendingRewardModifiers = new ArrayList<>();
    private int pendingRandomModifiers = 0;

    private DreamManager() {
        this.originalBaseCost = 0;
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
        return hasCardInSlot();
    }

    /**
     * Check if the slot has a card
     */
    public boolean hasCardInSlot() {
        return DreamSlotPanel.hasCard();
    }

    /**
     * Get the card currently in the dream slot
     */
    public AbstractCard getCardInSlot() {
        return DreamSlotPanel.getCard();
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
     * Get the ORIGINAL base cost of a card from CardLibrary (immune to Snecko)
     */
    public static int getOriginalCardCost(AbstractCard card) {
        if (card == null) {
            return 0;
        }

        // Look up the card in CardLibrary to get original cost
        AbstractCard libraryCard = CardLibrary.getCard(card.cardID);
        if (libraryCard != null) {
            // If the card is upgraded, get the upgraded version's cost
            if (card.upgraded) {
                AbstractCard upgradedCopy = libraryCard.makeCopy();
                upgradedCopy.upgrade();
                return upgradedCopy.cost;
            }
            return libraryCard.cost;
        }

        // Fallback to card's current cost if not found in library
        return card.cost;
    }

    /**
     * Calculate the base dream cost for a card (uses original cost, immune to Snecko)
     */
    public static int calculateBaseDreamCost(AbstractCard card) {
        int baseCost = getOriginalCardCost(card);
        return baseCost;
    }

    /**
     * Calculate the dream cost for ANY card (for display on cards in hand)
     * Accounts for Dreambound and DeepConnectionPower
     */
    public static int calculateDreamCostForCard(AbstractCard card) {
        if (card == null) {
            return 0;
        }

        int baseCost = calculateBaseDreamCost(card);
        int reduction = getDreamCostReductionFromModifiersStatic(card);
        int finalCost = Math.max(0, baseCost - reduction);

        // Check for VividDreamsOrb relic (increases all dream costs by 1)
        if (AbstractDungeon.player != null &&
                AbstractDungeon.player.hasRelic(yourmod.relics.VividDreamsOrb.ID)) {
            finalCost += 1;
        }

        return finalCost;
    }

    /**
     * Static version of getDreamCostReductionFromModifiers for use with any card
     */
    public static int getDreamCostReductionFromModifiersStatic(AbstractCard card) {
        int reduction = 0;

        // Check if player has DeepConnectionPower (doubles Dreambound effect)
        boolean hasDeepConnection = AbstractDungeon.player != null &&
                AbstractDungeon.player.hasPower(DeepConnectionPower.POWER_ID);
        int dreamboundValue = hasDeepConnection ? 2 : 1;

        // Check for Dreambound modifier
        try {
            java.util.List<basemod.abstracts.AbstractCardModifier> modifiers =
                    basemod.helpers.CardModifierManager.modifiers(card);

            for (basemod.abstracts.AbstractCardModifier mod : modifiers) {
                // Check if it's a Dreambound modifier
                if (mod.identifier(card).equals("DREAMBOUND")) {
                    if (mod instanceof yourmod.cardmods.DreamboundModifier) {
                        reduction += dreamboundValue;
                    }
                }
            }
        } catch (Exception e) {
            // No modifiers or error
        }

        return reduction;
    }

    /**
     * Get the current dream cost (base + penalty - modifiers)
     * Uses stored original cost for the card in slot (immune to Snecko)
     */
    public int getCurrentDreamCost() {
        if (!hasCardInSlot()) {
            return 0;
        }

        // Use the stored original base cost (set when card was inspired)
        int finalCost = originalBaseCost + costPenalty;

        // Check for card modifiers that reduce dream cost
        finalCost -= getDreamCostReductionFromModifiers(getCardInSlot());

        // Check for VividDreamsOrb relic (increases all dream costs by 1)
        if (AbstractDungeon.player != null &&
                AbstractDungeon.player.hasRelic(yourmod.relics.VividDreamsOrb.ID)) {
            finalCost += 1;
        }

        return Math.max(0, finalCost);
    }

    /**
     * Calculate total dream cost reduction from card modifiers
     */
    private int getDreamCostReductionFromModifiers(AbstractCard card) {
        int reduction = 0;

        // Check if player has DeepConnectionPower (doubles Dreambound effect)
        boolean hasDeepConnection = AbstractDungeon.player != null &&
                AbstractDungeon.player.hasPower(DeepConnectionPower.POWER_ID);
        int dreamboundValue = hasDeepConnection ? 2 : 1;

        // Check for Dreambound modifier
        try {
            java.util.List<basemod.abstracts.AbstractCardModifier> modifiers =
                    basemod.helpers.CardModifierManager.modifiers(card);

            for (basemod.abstracts.AbstractCardModifier mod : modifiers) {
                // Check if it's a Dreambound modifier
                if (mod.identifier(card).equals("DREAMBOUND")) {
                    if (mod instanceof yourmod.cardmods.DreamboundModifier) {
                        reduction += dreamboundValue;
                    }
                }
            }
        } catch (Exception e) {
            // No modifiers or error
        }

        return reduction;
    }

    /**
     * Inspire a card - move to dream pile and track it
     * Uses the actual card instance, not a copy!
     */
    public void inspireCard(AbstractCard card) {
        if (!canInspire(card)) {
            return;
        }

        // Add to our custom dream pile (handles removing from other piles)
        DreamSlotPanel.addCard(card);

        // Store the original base cost (immune to Snecko)
        this.originalBaseCost = calculateBaseDreamCost(card);

        // Reset cost penalty and play counter to 0
        // This way the first play will add +1 to both
        this.costPenalty = 0;
        this.timesPlayed = 0;

        // Increment times dreamed counter
        this.timesDreamed++;

        // Trigger onDream for relevant powers
        triggerOnDreamPowers();
    }

    /**
     * Return the dream card to hand (when replaced or at end of combat)
     */
    public void returnCardToHand() {
        AbstractCard card = getCardInSlot();
        if (card != null) {
            // Remove from dream pile first
            DreamSlotPanel.dreamPile.removeCard(card);

            // Then add to hand with proper cleanup
            DreamSlotPanel.resetCardVisuals(card);
            card.resetAttributes();
            card.applyPowers();

            if (AbstractDungeon.player != null) {
                if (AbstractDungeon.player.hand.size() < 10) {
                    AbstractDungeon.player.hand.addToTop(card);
                } else {
                    AbstractDungeon.player.discardPile.addToTop(card);
                }
            }

            // Reset state
            resetSlotState();
        }
    }

    /**
     * Remove dream card from all piles after being played, put back in dream pile
     */
    public void returnCardToDreamPile() {
        AbstractCard card = getCardInSlot();
        if (card != null) {
            DreamSlotPanel.returnCardToDreamPile(card);
        }
    }

    /**
     * Trigger onDream method for powers that care about dreaming
     */
    private void triggerOnDreamPowers() {
        if (AbstractDungeon.player == null) {
            return;
        }

        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof LucidityPower) {
                ((LucidityPower) power).onDream();
            }
            if (power instanceof DreamDrawPower) {
                ((DreamDrawPower) power).onDream();
            }
        }
    }

    /**
     * Trigger onDreamCardPlayed for powers that care about manifesting
     */
    public void triggerOnDreamCardPlayedPowers() {
        if (AbstractDungeon.player == null) {
            return;
        }

        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof LucidDreamerPower) {
                ((LucidDreamerPower) power).onDreamCardPlayed();
            }
            if (power instanceof DreamShieldPower) {
                ((DreamShieldPower) power).onDreamCardPlayed();
            }
        }
    }

    /**
     * Materialize - reduce the dream cost by the specified amount
     * Returns true if the card should be played (cost reached 0)
     */
    public boolean materialize(int amount) {
        if (!hasCardInSlot() || amount <= 0) {
            return false;
        }

        // Reduce the cost penalty (making cost lower)
        costPenalty -= amount;

        // If cost is now 0 or less, the card should be played
        return getCurrentDreamCost() <= 0;
    }

    /**
     * Called after a dream card is played
     * If it's exhaust or power, clear the slot (unless Permanence prevents it)
     * Otherwise, reset cost to base + number of times played
     *
     * NOTE: This method is actually handled by DreamCardUseActionPatch now,
     * but kept for backwards compatibility
     */
    public void onDreamCardPlayed() {
        AbstractCard card = getCardInSlot();
        if (card == null) {
            return;
        }

        // Check if this card would normally be cleared (exhaust or power)
        boolean shouldClear = (card.exhaust || card.type == AbstractCard.CardType.POWER);
        boolean hasPermanence = AbstractDungeon.player != null &&
                AbstractDungeon.player.hasPower(PermanencePower.POWER_ID);

        if (shouldClear && !hasPermanence) {
            // No Permanence - card is truly exhausted/removed, clear slot
            clearSlot();
        } else if (shouldClear && hasPermanence) {
            // Permanence prevents clearing - treat like normal card
            timesPlayed++;
            costPenalty = timesPlayed;

            // Return card to dream pile
            returnCardToDreamPile();

            // Reduce Permanence stacks
            AbstractPower permanence = AbstractDungeon.player.getPower(PermanencePower.POWER_ID);
            if (permanence.amount <= 1) {
                AbstractDungeon.actionManager.addToTop(
                        new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                                AbstractDungeon.player, AbstractDungeon.player, permanence));
            } else {
                permanence.amount--;
                permanence.updateDescription();
            }
        } else {
            // Normal card - increment times played
            timesPlayed++;
            costPenalty = timesPlayed;

            // Return card to dream pile for next use
            returnCardToDreamPile();
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
     * Get the stored original base cost of the card in slot
     */
    public int getOriginalBaseCost() {
        return originalBaseCost;
    }

    public void incrementTimesPlayed() {
        timesPlayed++;
        // SET to timesPlayed (creates new baseline)
        costPenalty = timesPlayed;

        System.out.println("  incrementTimesPlayed: timesPlayed=" + timesPlayed + ", costPenalty=" + costPenalty);
    }
    /**
     * Apply the current dream cost penalty to the provided card instance immediately.
     * This is intended to be called on newly-created materialized cards so they reflect the
     * current DreamManager penalty right away.
     */
    public void applyCurrentPenaltyTo(com.megacrit.cardcrawl.cards.AbstractCard card) {
        if (card == null) return;
        int penalty = getCostPenalty(); // existing method you already have
        if (penalty <= 0) return;

        try {
            // Use setCostForTurn so the card instance used in the current combat/turn respects the penalty.
            // Do not overwrite base cost; only adjust costForTurn for the current instance.
            int current = card.costForTurn;
            // If costForTurn isn't initialized (-1 used in some cases), use base cost
            if (current < 0) {
                current = card.cost;
            }
            int newCost = Math.max(0, current - penalty);
            card.setCostForTurn(newCost);
            // If you also want the UI to show reduced cost, set isCostModifiedForTurn to true:
            card.isCostModifiedForTurn = (newCost != card.cost);
        } catch (Exception e) {
            System.err.println("DreamManager.applyCurrentPenaltyTo error: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Clear the dream slot
     */
    public void clearSlot() {
        DreamSlotPanel.clear();
        this.originalBaseCost = 0;
        this.costPenalty = 0;
        this.timesPlayed = 0;
    }

    /**
     * Reset just the slot state counters (called when panel is already cleared)
     */
    public void resetSlotState() {
        this.originalBaseCost = 0;
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
     * Clean up at end of combat
     */
    public void onCombatEnd() {
        clearSlot();
        this.timesDreamed = 0;
    }

    /**
     * Add a modifier to be applied to the next card rewards
     */
    public void addPendingRewardModifier(basemod.abstracts.AbstractCardModifier modifier) {
        pendingRewardModifiers.add(modifier);
    }

    /**
     * Add a random modifier to be applied to the next card rewards
     */
    public void addRandomRewardModifier() {
        pendingRandomModifiers++;
    }

    /**
     * Apply pending modifiers to card rewards and clear them
     */
    public void applyPendingRewardModifiers(ArrayList<AbstractCard> cards) {
        // Apply specific modifiers
        if (!pendingRewardModifiers.isEmpty()) {
            for (AbstractCard card : cards) {
                for (basemod.abstracts.AbstractCardModifier mod : pendingRewardModifiers) {
                    basemod.helpers.CardModifierManager.addModifier(card, mod.makeCopy());
                }
            }
            pendingRewardModifiers.clear();
        }

        // Apply random modifiers
        if (pendingRandomModifiers > 0) {
            for (AbstractCard card : cards) {
                for (int i = 0; i < pendingRandomModifiers; i++) {
                    basemod.abstracts.AbstractCardModifier randomMod =
                            yourmod.cards.DreamForge.getRandomModifierForCard(card);
                    basemod.helpers.CardModifierManager.addModifier(card, randomMod);
                }
            }
            pendingRandomModifiers = 0;
        }
    }

    /**
     * Validate dream pile state - for debugging
     */
    public void validateDreamPileState() {
        if (DreamSlotPanel.dreamPile.size() > 1) {
            System.err.println("ERROR: Multiple cards in dream pile! Count: " + DreamSlotPanel.dreamPile.size());
            for (int i = 0; i < DreamSlotPanel.dreamPile.size(); i++) {
                System.err.println("  Card " + i + ": " + DreamSlotPanel.dreamPile.group.get(i).name);
            }
        }
    }
}