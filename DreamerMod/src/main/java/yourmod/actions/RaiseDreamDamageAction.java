package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import yourmod.dream.DreamManager;

public class RaiseDreamDamageAction extends AbstractGameAction {
    private int damageIncrease;

    public RaiseDreamDamageAction(int damageIncrease) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.damageIncrease = damageIncrease;
    }

    @Override
    public void update() {
        if (DreamManager.getInstance().hasCardInSlot()) {
            AbstractCard dreamCard = DreamManager.getInstance().getCardInSlot();
            if (dreamCard.baseDamage >= 0) {
                dreamCard.baseDamage += damageIncrease;
                dreamCard.applyPowers();
            }
        }
        this.isDone = true;
    }
}