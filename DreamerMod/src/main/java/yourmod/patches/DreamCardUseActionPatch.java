package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import yourmod.dream.DreamManager;
import yourmod.tags.CustomTags;
import yourmod.ui.DreamSlotPanel;

/**
 * Intercepts dream cards after they're played and returns them to the dream pile
 * instead of letting them go to discard or exhaust.
 */
public class DreamCardUseActionPatch {

    /**
     * Intercept right before moveToDiscardPile is called
     */
    @SpirePatch2(clz = UseCardAction.class, method = "update")
    public static class InterceptBeforeDiscard {
        @SpireInsertPatch(locator = DiscardLocator.class)
        public static SpireReturn<?> beforeDiscard(UseCardAction __instance, AbstractCard ___targetCard) {
            if (___targetCard != null && ___targetCard.hasTag(CustomTags.DREAM_PLAYING)) {
                // Don't let it go to discard - handle it ourselves
                handleDreamCardAfterPlay(___targetCard, __instance);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        // Find the moveToDiscardPile call
        public static class DiscardLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDiscardPile");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    /**
     * Also intercept before exhaust in case the dream card has exhaust
     */
    @SpirePatch2(clz = UseCardAction.class, method = "update")
    public static class InterceptBeforeExhaust {
        @SpireInsertPatch(locator = ExhaustLocator.class)
        public static SpireReturn<?> beforeExhaust(UseCardAction __instance, AbstractCard ___targetCard) {
            if (___targetCard != null && ___targetCard.hasTag(CustomTags.DREAM_PLAYING)) {
                // Don't let it go to exhaust - handle it ourselves
                handleDreamCardAfterPlay(___targetCard, __instance);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        // Find the moveToExhaustPile call
        public static class ExhaustLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(CardGroup.class, "moveToExhaustPile");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    /**
     * CATCH-ALL: Intercept at the END of UseCardAction.update()
     * This catches power cards and any other cards that don't go to discard/exhaust
     */
    @SpirePatch2(clz = UseCardAction.class, method = "update")
    public static class InterceptAtEnd {
        @SpirePostfixPatch
        public static void atEnd(UseCardAction __instance, AbstractCard ___targetCard, boolean ___exhaustCard) {
            // Only trigger if the card has the DREAM_PLAYING tag (meaning it hasn't been handled yet)
            if (___targetCard != null && ___targetCard.hasTag(CustomTags.DREAM_PLAYING)) {
                System.out.println("CATCH-ALL: Handling dream card at end of UseCardAction: " + ___targetCard.name);
                handleDreamCardAfterPlay(___targetCard, __instance);
            }
        }
    }

    /**
     * Handle the dream card after it's been played
     */
    private static void handleDreamCardAfterPlay(AbstractCard card, UseCardAction action) {
        System.out.println("=== HANDLING DREAM CARD AFTER PLAY ===");
        System.out.println("  Card: " + card.name);
        System.out.println("  Type: " + card.type);
        System.out.println("  Exhaust: " + card.exhaust);

        // Clean up card state
        card.unhover();
        card.untip();
        card.stopGlowing();

        // Remove the DREAM_PLAYING tag (CRITICAL - prevents infinite loops)
        card.tags.remove(CustomTags.DREAM_PLAYING);
        System.out.println("  Removed DREAM_PLAYING tag");

        // Remove from limbo, hand, discard, exhaust, draw - but NOT dreamPile yet!
        // The card needs to stay in dreamPile so Materialize effects can work
        AbstractDungeon.player.limbo.removeCard(card);
        AbstractDungeon.player.hand.removeCard(card);
        AbstractDungeon.player.discardPile.removeCard(card);
        AbstractDungeon.player.exhaustPile.removeCard(card);
        AbstractDungeon.player.drawPile.removeCard(card);
        // NOTE: NOT removing from dreamPile here!
        System.out.println("  Removed card from limbo/hand/discard/exhaust/draw (kept in dreamPile)");

        // Trigger onCardDrawOrDiscard for consistency
        AbstractDungeon.player.onCardDrawOrDiscard();

        // Trigger powers that care about dream card being played
        DreamManager dm = DreamManager.getInstance();
        dm.triggerOnDreamCardPlayedPowers();

        // Check if this card should be cleared (exhaust or power) WITHOUT Permanence
        boolean shouldClearSlot = (card.exhaust || card.type == AbstractCard.CardType.POWER);
        boolean hasPermanence = AbstractDungeon.player.hasPower(yourmod.powers.PermanencePower.POWER_ID);

        System.out.println("  Should clear slot: " + shouldClearSlot);
        System.out.println("  Has Permanence: " + hasPermanence);

        // ALWAYS reset the auto-play flag FIRST
        yourmod.patches.DreamAutoPlayPatch.setPlaying(false);
        System.out.println("  Reset isPlaying flag to FALSE");

        if (shouldClearSlot && !hasPermanence) {
            // POWER or EXHAUST card without Permanence - OBLITERATE IT
            // NOW we remove from dreamPile and clear
            DreamSlotPanel.dreamPile.removeCard(card);
            DreamSlotPanel.clear();
            dm.resetSlotState();

            System.out.println("  CLEARED SLOT (Power/Exhaust without Permanence)");
        } else if (shouldClearSlot && hasPermanence) {
            // POWER or EXHAUST card WITH Permanence - treat as normal card
            System.out.println("  Saved by Permanence - keeping in dream pile");
            incrementAfterEffects(card, dm);

            // Reduce Permanence stacks
            com.megacrit.cardcrawl.powers.AbstractPower permanence =
                    AbstractDungeon.player.getPower(yourmod.powers.PermanencePower.POWER_ID);
            if (permanence.amount <= 1) {
                AbstractDungeon.actionManager.addToTop(
                        new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                                AbstractDungeon.player, AbstractDungeon.player, permanence));
            } else {
                permanence.amount--;
                permanence.updateDescription();
            }
        } else {
            // Normal card (not exhaust, not power) - keep in dream pile
            System.out.println("  Normal card - keeping in dream pile");
            incrementAfterEffects(card, dm);
        }

        System.out.println("=== DONE HANDLING DREAM CARD ===");

        // Mark action as done
        action.isDone = true;
    }

    private static void incrementAfterEffects(AbstractCard card, DreamManager dm) {
        // Queue to TOP so it runs BEFORE the card's Materialize effects
        AbstractDungeon.actionManager.addToTop(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
            @Override
            public void update() {
                System.out.println("  INCREMENT FIRST: Running before Materialize");
                System.out.println("  Cost penalty BEFORE increment: " + dm.getCostPenalty());

                // Increment times played - this creates the baseline
                dm.incrementTimesPlayed();

                // Card is already in dream pile, just reset its visuals
                DreamSlotPanel.resetCardVisuals(card);

                System.out.println("  Cost penalty AFTER increment: " + dm.getCostPenalty());
                System.out.println("  Current cost: " + dm.getCurrentDreamCost());

                this.isDone = true;
            }
        });
    }
}