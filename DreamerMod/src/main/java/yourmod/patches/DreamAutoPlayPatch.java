package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import yourmod.actions.AutoPlayDreamCardAction;
import yourmod.dream.DreamManager;

import static yourmod.util.Wiz.att;

public class DreamAutoPlayPatch {

    private static boolean isPlaying = false;

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class CheckAutoPlayDreamCard {
        @SpirePostfixPatch
        public static void Postfix(GameActionManager __instance) {
            // Skip if already playing
            if (isPlaying) {
                return;
            }

            DreamManager dm = DreamManager.getInstance();

            // Check if dream card should auto-play
            if (dm.shouldAutoPlay()) {
                // Extra safety: check if the card is already tagged as playing
                if (dm.hasCardInSlot() && dm.getCardInSlot().hasTag(yourmod.tags.CustomTags.DREAM_PLAYING)) {
                    return;
                }

                isPlaying = true;
                att(new AutoPlayDreamCardAction());
            }
        }
    }

    public static void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }
}