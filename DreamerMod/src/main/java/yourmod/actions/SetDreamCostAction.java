package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import yourmod.dream.DreamManager;
import yourmod.dream.DreamSlotRenderer;

/**
 * Sets the dream cost to a target value, but won't reduce it below that value
 * or increase it if already lower.
 */
public class SetDreamCostAction extends AbstractGameAction {
    private int targetCost;

    public SetDreamCostAction(int targetCost) {
        this.targetCost = targetCost;
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

        int currentCost = dm.getCurrentDreamCost();

        // Only reduce if current cost is higher than target
        if (currentCost > targetCost) {
            int reduction = currentCost - targetCost;

            // Play SFX
            CardCrawlGame.sound.play("STANCE_ENTER_CALM", 0.4f);

            // VFX at dream slot
            AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(
                    DreamSlotRenderer.getSlotX(),
                    DreamSlotRenderer.getSlotY(),
                    AttackEffect.NONE
            ));

            // Reduce the cost (but this won't trigger auto-play since we're targeting 1)
            dm.materialize(reduction);
        }

        this.isDone = true;
    }
}