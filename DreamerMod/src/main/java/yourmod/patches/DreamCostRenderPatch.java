package yourmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import yourmod.dream.DreamManager;
import yourmod.tags.CustomTags;
import yourmod.util.TexLoader;

import static yourmod.ModFile.makeImagePath;

public class DreamCostRenderPatch {

    private static Texture dreamEnergyTexture = null;

    @SpirePatch(
            clz = AbstractCard.class,
            method = "render",
            paramtypez = {SpriteBatch.class}
    )
    public static class RenderDreamCost {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
            // Only render dream cost if the dream slot is visible
            if (!DreamManager.getInstance().isDreamSlotVisible()) {
                return;
            }

            // Don't render on Dreamer cards
            if (__instance.hasTag(CustomTags.DREAMER_CARD)) {
                return;
            }

            // Don't render on cards that can't be inspired
            if (!DreamManager.getInstance().canInspire(__instance)) {
                return;
            }

            // Calculate dream cost using the centralized method
            int dreamCost = DreamManager.calculateDreamCostForCard(__instance);

            // Render dream cost on the right side of the card
            renderDreamCostOnCard(__instance, sb, dreamCost);
        }

        private static void renderDreamCostOnCard(AbstractCard card, SpriteBatch sb, int dreamCost) {
            // Load custom dream energy texture if not already loaded
            if (dreamEnergyTexture == null) {
                dreamEnergyTexture = TexLoader.getTexture(makeImagePath("ui/dreamEnergy.png"));
            }

            // Position on the right side - adjusted to be on the card
            float offsetX = AbstractCard.IMG_WIDTH * card.drawScale * Settings.scale * 0.64f;
            float offsetY = AbstractCard.IMG_HEIGHT * card.drawScale * Settings.scale * 0.58f;

            float x = card.current_x + offsetX;
            float y = card.current_y + offsetY;

            // Set color with card transparency
            Color color = Color.WHITE.cpy();
            color.a = card.transparency;
            sb.setColor(color);

            float orbSize = 80f * card.drawScale * Settings.scale;

            // Render the dream energy orb
            sb.draw(
                    dreamEnergyTexture,
                    x - orbSize / 2f,
                    y - orbSize / 2f,
                    orbSize / 2f,
                    orbSize / 2f,
                    orbSize,
                    orbSize,
                    1f,
                    1f,
                    0f,
                    0,
                    0,
                    dreamEnergyTexture.getWidth(),
                    dreamEnergyTexture.getHeight(),
                    false,
                    false
            );

            // Render the cost number on top
            FontHelper.renderFontCentered(
                    sb,
                    FontHelper.cardEnergyFont_L,
                    String.valueOf(dreamCost),
                    x,
                    y,
                    color,
                    card.drawScale
            );
        }
    }
}