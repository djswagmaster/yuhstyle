package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import yourmod.dream.DreamManager;
import yourmod.dream.DreamSlotRenderer;

/**
 * MaterializeAction reduces the dream cost by the specified amount.
 * If cost reaches 0, the DreamAutoPlayPatch will trigger AutoPlayDreamCardAction.
 */
public class MaterializeAction extends AbstractGameAction {
    private int materializeAmount;

    public MaterializeAction(int amount) {
        this.materializeAmount = amount;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        DreamManager dm = DreamManager.getInstance();

        // If no card in slot, do nothing
        if (!dm.hasCardInSlot()) {
            this.isDone = true;
            return;
        }

        // Play manifest SFX
        CardCrawlGame.sound.play("STANCE_ENTER_CALM", 0.4f);

        // VFX at dream slot
        AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(
                DreamSlotRenderer.getSlotX(),
                DreamSlotRenderer.getSlotY(),
                AttackEffect.NONE
        ));

        // Materialize (reduce cost)
        // The DreamAutoPlayPatch will handle auto-playing if cost reaches 0
        dm.materialize(materializeAmount);

        this.isDone = true;
    }
}