package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.dream.DreamManager;

public class DreamRecallAction extends AbstractGameAction {
    private int costReduction;

    public DreamRecallAction() {
        this(0);
    }

    public DreamRecallAction(int costReduction) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.costReduction = costReduction;
    }

    @Override
    public void update() {
        if (DreamManager.getInstance().hasCardInSlot()) {
            AbstractCard dreamCard = DreamManager.getInstance().getCardInSlot();
            AbstractCard copy = dreamCard.makeStatEquivalentCopy();

            // Apply cost reduction for this turn only
            if (costReduction > 0) {
                copy.setCostForTurn(copy.cost - costReduction);
            }

            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(copy));
        }

        this.isDone = true;
    }
}