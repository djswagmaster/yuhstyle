package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RaiseDreamDamageAction extends AbstractGameAction {
    private int damageIncrease;

    public RaiseDreamDamageAction(int damageIncrease) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.damageIncrease = damageIncrease;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            // Filter for cards that have damage
            if (AbstractDungeon.player.hand.group.stream()
                    .anyMatch(c -> c.baseDamage >= 0)) {

                AbstractDungeon.handCardSelectScreen.open(
                        "increase its damage",
                        1,
                        false,
                        true  // Changed to true to allow canceling
                );
                tickDuration();
                return;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
                AbstractCard selectedCard = AbstractDungeon.handCardSelectScreen.selectedCards.group.get(0);
                selectedCard.baseDamage += damageIncrease;
                selectedCard.applyPowers();

                AbstractDungeon.player.hand.addToTop(selectedCard);
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.clear();
            } else {
                // User canceled, just mark as retrieved
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            }
        }

        tickDuration();
    }
}