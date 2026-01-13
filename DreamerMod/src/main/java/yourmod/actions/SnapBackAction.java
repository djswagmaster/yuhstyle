package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.dream.DreamManager;

public class SnapBackAction extends AbstractGameAction {

    public SnapBackAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        DreamManager dm = DreamManager.getInstance();

        if (dm.hasCardInSlot()) {
            AbstractCard dreamCard = dm.getCardInSlot();
            AbstractCard copy = dreamCard.makeStatEquivalentCopy();

            // Add the card to hand
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(copy));

            // Clear the dream slot
            dm.clearSlot();
        }

        this.isDone = true;
    }
}