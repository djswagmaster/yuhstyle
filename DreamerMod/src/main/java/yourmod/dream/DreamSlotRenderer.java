package yourmod.dream;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import yourmod.cardmods.DreamboundModifier;
import yourmod.util.TexLoader;

import static yourmod.ModFile.makeImagePath;

public class DreamSlotRenderer {
    // Position of the dream slot - further right and higher up
    private static final float SLOT_X = Settings.WIDTH * 0.38f;
    private static final float SLOT_Y = Settings.HEIGHT * 0.45f;

    // Card display size in the slot - 2/3 size
    private static final float CARD_SCALE = 0.5f;

    // Empty slot texture
    private static Texture emptySlotTexture = null;

    // Dreamy animation variables
    private static float dreamTimer = 0f;
    private static final float PULSE_SPEED = 2f;

    // Dreamy purple tint
    private static final Color DREAM_TINT = new Color(0.85f, 0.75f, 1.0f, 1.0f);

    public static void render(SpriteBatch sb) {
        DreamManager dm = DreamManager.getInstance();

        // Only render if the slot should be visible
        if (!dm.isDreamSlotVisible()) {
            return;
        }

        // Only render in combat rooms
        if (AbstractDungeon.getCurrRoom() == null ||
                AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            return;
        }

        if (dm.hasCardInSlot()) {
            renderCardInSlot(sb, dm);
        } else {
            renderEmptySlot(sb);
        }
    }

    private static void renderEmptySlot(SpriteBatch sb) {
        // Load empty slot texture if not already loaded
        if (emptySlotTexture == null) {
            emptySlotTexture = TexLoader.getTexture(makeImagePath("ui/dreamSlotEmpty.png"));
        }

        // Render the empty slot sprite
        float cardWidth = AbstractCard.IMG_WIDTH * CARD_SCALE * Settings.scale;
        float cardHeight = AbstractCard.IMG_HEIGHT * CARD_SCALE * Settings.scale;

        sb.setColor(Color.WHITE);
        sb.draw(
                emptySlotTexture,
                SLOT_X - cardWidth / 2f,
                SLOT_Y - cardHeight / 2f,
                cardWidth / 2f,
                cardHeight / 2f,
                cardWidth,
                cardHeight,
                1f,
                1f,
                0f,
                0,
                0,
                emptySlotTexture.getWidth(),
                emptySlotTexture.getHeight(),
                false,
                false
        );
    }

    private static void renderCardInSlot(SpriteBatch sb, DreamManager dm) {
        AbstractCard card = dm.getCardInSlot();
        int currentCost = dm.getCurrentDreamCost();

        // Update the card's powers/description
        card.applyPowers();

        // Update dream animation timer
        dreamTimer += com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        // Calculate pulsating effect
        float pulseScale = 1f + MathUtils.sin(dreamTimer * PULSE_SPEED) * 0.05f;
        float transparencyPulse = 0.85f + MathUtils.sin(dreamTimer * PULSE_SPEED * 0.7f) * 0.15f;

        // Slight floating movement
        float floatOffsetY = MathUtils.sin(dreamTimer * PULSE_SPEED * 0.5f) * 8f * Settings.scale;

        // Store original card properties
        float originalX = card.current_x;
        float originalY = card.current_y;
        float originalDrawScale = card.drawScale;
        float originalAngle = card.angle;
        float originalTransparency = card.transparency;

        // Set card position to slot with floating offset
        card.current_x = SLOT_X;
        card.current_y = SLOT_Y + floatOffsetY;
        card.drawScale = CARD_SCALE * pulseScale;
        card.angle = 0.0f;
        card.transparency = transparencyPulse;

        // Apply dreamy purple tint through SpriteBatch
        Color batchColor = sb.getColor();
        Color dreamyColor = DREAM_TINT.cpy();
        dreamyColor.a = transparencyPulse;
        sb.setColor(dreamyColor);

        // Render the card
        card.render(sb);

        // Reset batch color
        sb.setColor(batchColor);

        // Render current dream cost using dream energy orbs
        renderDreamCostOverlay(sb, currentCost, card.drawScale, transparencyPulse);

        // Restore original card properties
        card.current_x = originalX;
        card.current_y = originalY;
        card.drawScale = originalDrawScale;
        card.angle = originalAngle;
        card.transparency = originalTransparency;
    }

    private static void renderDreamCostOverlay(SpriteBatch sb, int cost, float scale, float alpha) {
        Texture dreamEnergyTexture = TexLoader.getTexture(makeImagePath("ui/dreamEnergy.png"));
        Texture dreamEnergyLargeTexture = TexLoader.getTexture(makeImagePath("ui/dreamEnergyLarge.png"));

        // Position on the right side of the card in the slot
        float offsetX = AbstractCard.IMG_WIDTH * scale * Settings.scale * 0.65f;
        float offsetY = AbstractCard.IMG_HEIGHT * scale * Settings.scale * 0.6f;
        float floatOffsetY = MathUtils.sin(dreamTimer * PULSE_SPEED * 0.5f) * 8f * Settings.scale;

        float x = SLOT_X + offsetX;
        float y = SLOT_Y + offsetY + floatOffsetY;

        // Render the CURRENT cost dream energy orb (large)
        Color orbColor = Color.WHITE.cpy();
        orbColor.a = alpha;
        sb.setColor(orbColor);
        float orbSize = 175f * scale * Settings.scale;

        sb.draw(
                dreamEnergyLargeTexture,
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
                dreamEnergyLargeTexture.getWidth(),
                dreamEnergyLargeTexture.getHeight(),
                false,
                false
        );

        // Render the current cost number
        Color costColor = Color.WHITE.cpy();
        costColor.a = alpha;
        FontHelper.renderFontCentered(
                sb,
                FontHelper.cardEnergyFont_L,
                String.valueOf(cost),
                x,
                y,
                costColor,
                scale * 1.2f
        );

        // Render the BASE + PENALTY - DREAMBOUND cost in smaller orb
        DreamManager dm = DreamManager.getInstance();
        if (dm.hasCardInSlot()) {
            int baseCost = DreamManager.calculateBaseDreamCost(dm.getCardInSlot());
            int timesPlayed = dm.getTimesPlayed();

            // Calculate Dreambound reduction
            int reduction = 0;
            try {
                for (AbstractCardModifier mod : CardModifierManager.modifiers(dm.getCardInSlot())) {
                    if (mod.identifier(dm.getCardInSlot()).equals("DREAMBOUND")) {
                        if (mod instanceof DreamboundModifier) {
                            reduction += ((DreamboundModifier) mod).getDreamCostReduction();
                        }
                    }
                }
            } catch (Exception e) {
                // No modifiers
            }

            int baseWithPenalty = Math.max(0, baseCost + timesPlayed - reduction);

            // Position the smaller orb
            float smallOrbX = x - (70f * scale * Settings.scale);
            float smallOrbY = y + (35f * scale * Settings.scale);

            // Render smaller orb
            sb.setColor(orbColor);
            float smallOrbSize = 100f * scale * Settings.scale;

            sb.draw(
                    dreamEnergyTexture,
                    smallOrbX - smallOrbSize / 2f,
                    smallOrbY - smallOrbSize / 2f,
                    smallOrbSize / 2f,
                    smallOrbSize / 2f,
                    smallOrbSize,
                    smallOrbSize,
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

            // Render base+penalty-dreambound cost
            FontHelper.renderFontCentered(
                    sb,
                    FontHelper.cardEnergyFont_L,
                    String.valueOf(baseWithPenalty),
                    smallOrbX,
                    smallOrbY,
                    costColor,
                    scale * 0.8f
            );
        }
    }

    public static float getSlotX() {
        return SLOT_X;
    }

    public static float getSlotY() {
        return SLOT_Y;
    }
}