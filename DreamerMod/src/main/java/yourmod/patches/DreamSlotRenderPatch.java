package yourmod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import yourmod.dream.DreamSlotRenderer;

public class DreamSlotRenderPatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "render"
    )
    public static class RenderDreamSlot {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance, SpriteBatch sb) {
            // Render the dream slot after the player renders
            DreamSlotRenderer.render(sb);
        }
    }
}