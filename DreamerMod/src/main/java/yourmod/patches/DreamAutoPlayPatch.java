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
            DreamManager dm = DreamManager.getInstance();

            // Check if dream card should auto-play and we're not already playing it
            if (!isPlaying && dm.shouldAutoPlay()) {
                isPlaying = true;
                att(new AutoPlayDreamCardAction());
            }
        }
    }

    public static void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}