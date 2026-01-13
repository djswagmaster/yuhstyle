package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DiscardToDeckAction extends AbstractGameAction {
    private String message;

    public DiscardToDeckAction(int amount, String message) {
        this.amount = amount;
        this.message = message;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.discardPile.isEmpty()) {
            this.isDone = true;
            return;
        }

        if (this.duration == Settings.ACTION_DUR_FAST) {
            int selectAmount = Math.min(this.amount, AbstractDungeon.player.discardPile.size());

            // Use anyNumber=true to allow confirming with fewer than max cards
            AbstractDungeon.gridSelectScreen.open(
                    AbstractDungeon.player.discardPile,
                    selectAmount,
                    true,  // anyNumber - allows confirming with 0 to selectAmount cards
                    message
            );

            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractDungeon.player.discardPile.moveToDeck(card, true);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        tickDuration();
    }
}