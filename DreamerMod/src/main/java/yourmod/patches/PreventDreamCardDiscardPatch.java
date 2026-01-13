/*package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import yourmod.tags.CustomTags;

public class PreventDreamCardDiscardPatch {

    @SpirePatch(
            clz = CardGroup.class,
            method = "addToTop"
    )
    public static class PreventAddToDiscard {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(CardGroup __instance, AbstractCard c) {
            if (__instance.type == CardGroup.CardGroupType.DISCARD_PILE &&
                    c != null && c.hasTag(CustomTags.DREAM_PLAYING)) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = CardGroup.class,
            method = "addToBottom"
    )
    public static class PreventAddToDiscardBottom {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(CardGroup __instance, AbstractCard c) {
            if (__instance.type == CardGroup.CardGroupType.DISCARD_PILE &&
                    c != null && c.hasTag(CustomTags.DREAM_PLAYING)) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}*/