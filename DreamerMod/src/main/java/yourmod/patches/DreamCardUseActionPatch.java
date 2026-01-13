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
     * Handle the dream card after it's been played
     */
    private static void handleDreamCardAfterPlay(AbstractCard card, UseCardAction action) {
        // Clean up card state
        card.unhover();
        card.untip();
        card.stopGlowing();

        // Remove the DREAM_PLAYING tag
        card.tags.remove(CustomTags.DREAM_PLAYING);

        // Remove from ALL places - limbo, hand, AND dreamPile
        // (Card was in both dreamPile and limbo during play)
        AbstractDungeon.player.limbo.removeCard(card);
        AbstractDungeon.player.hand.removeCard(card);
        DreamSlotPanel.dreamPile.removeCard(card);

        // Trigger onCardDrawOrDiscard for consistency
        AbstractDungeon.player.onCardDrawOrDiscard();

        // Trigger powers that care about dream card being played
        DreamManager dm = DreamManager.getInstance();
        dm.triggerOnDreamCardPlayedPowers();

        // Handle what happens to the card based on its type
        if (card.exhaust || card.type == AbstractCard.CardType.POWER) {
            // Check for Permanence power first
            if (AbstractDungeon.player.hasPower(yourmod.powers.PermanencePower.POWER_ID)) {
                // Permanence prevents clearing - treat like normal card
                returnCardToDreamPile(card, dm);

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
                // No Permanence - card is truly exhausted/removed, clear slot
                DreamSlotPanel.clear();
                dm.resetSlotState();
            }
        } else {
            // Normal card - return to dream pile
            returnCardToDreamPile(card, dm);
        }

        // Reset the auto-play flag NOW, not in a separate action
        yourmod.patches.DreamAutoPlayPatch.setPlaying(false);

        // Mark action as done
        action.isDone = true;
    }

    private static void returnCardToDreamPile(AbstractCard card, DreamManager dm) {
        // Note: incrementTimesPlayed is called BEFORE playing in AutoPlayDreamCardAction
        // So we just need to add the card back to the pile

        // Add card back to dream pile
        DreamSlotPanel.addCard(card);

        // Reset card visuals
        DreamSlotPanel.resetCardVisuals(card);
    }
}