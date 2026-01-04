package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import yourmod.actions.DreamAction;
import yourmod.dream.DreamManager;
import yourmod.tags.CustomTags;

public class InspireCardFilterPatch {

    @SpirePatch(
            clz = HandCardSelectScreen.class,
            method = "selectHoveredCard"
    )
    public static class PreventInvalidCardSelection {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(HandCardSelectScreen __instance) {
            // Only filter when DreamAction is active
            if (!DreamAction.isActive) {
                return SpireReturn.Continue();
            }

            // Check if the hovered card can be inspired
            if (__instance.hoveredCard != null) {
                // Always block Dream cards
                if (__instance.hoveredCard.hasTag(CustomTags.DREAMER_CARD)) {
                    return SpireReturn.Return(null);
                }

                if (!DreamManager.getInstance().canInspire(__instance.hoveredCard)) {
                    // Don't allow selecting this card
                    return SpireReturn.Return(null);
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "updateHoverLogic"
    )
    public static class GrayOutInvalidCards {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            // Only gray out when DreamAction is active
            if (!DreamAction.isActive) {
                return;
            }

            // Only affect cards that are actually in the player's hand
            if (AbstractDungeon.player == null ||
                    !AbstractDungeon.player.hand.contains(__instance)) {
                return;
            }

            // Gray out Dream cards
            if (__instance.hasTag(CustomTags.DREAMER_CARD)) {
                __instance.targetTransparency = 0.5f;
                return;
            }

            if (!DreamManager.getInstance().canInspire(__instance)) {
                // Gray out invalid cards
                __instance.targetTransparency = 0.5f;
            }
        }
    }
}