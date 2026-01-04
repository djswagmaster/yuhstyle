package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import yourmod.dream.DreamManager;

public class MaterializeAction extends AbstractGameAction {
    private int materializeAmount;

    public MaterializeAction(int amount) {
        this.materializeAmount = amount;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            DreamManager dm = DreamManager.getInstance();

            // If no card in slot, do nothing
            if (!dm.hasCardInSlot()) {
                this.isDone = true;
                return;
            }

            // Materialize (reduce cost)
            // The auto-play patch will handle playing it if cost reaches 0
            dm.materialize(materializeAmount);

            this.isDone = true;
            return;
        }

        tickDuration();
    }
}