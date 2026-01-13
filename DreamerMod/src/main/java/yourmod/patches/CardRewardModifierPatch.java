package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.dream.DreamManager;

import java.util.ArrayList;

public class CardRewardModifierPatch {

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static class ApplyPendingModifiers {
        @SpirePostfixPatch
        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> result) {
            DreamManager.getInstance().applyPendingRewardModifiers(result);
            return result;
        }
    }
}