package yourmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import yourmod.actions.AutoPlayDreamCardAction;
import yourmod.dream.DreamManager;

import static yourmod.util.Wiz.att;

/**
 * Auto-play checker. Keep lifecycle of isPlaying simple: it's set true when we queue the AutoPlay action,
 * and set false by the final action that runs in the DreamCardUseActionPatch (so it's tied to the action queue).
 *
 * Removed skipChecks/frame-skip workaround — action queue finalization guarantees correctness.
 */
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

            if (dm.shouldAutoPlay()) {
                if (dm.hasCardInSlot() && dm.getCardInSlot().hasTag(yourmod.tags.CustomTags.DREAM_PLAYING)) {
                    System.err.println("WARNING: Card already has DREAM_PLAYING tag! Skipping auto-play.");
                    return;
                }

                System.out.println("AUTO-PLAY TRIGGERED - Setting isPlaying to TRUE");
                isPlaying = true;
                att(new AutoPlayDreamCardAction());
            }
        }
    }

    /**
     * Called by final queued action to clear the flag. Keep trivial.
     */
    public static void setPlaying(boolean playing) {
        System.out.println("DreamAutoPlayPatch.setPlaying() called: " + playing);
        isPlaying = playing;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }
}
