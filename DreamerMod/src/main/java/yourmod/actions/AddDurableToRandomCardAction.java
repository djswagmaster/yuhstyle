package yourmod.actions;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.cardmods.DurableModifier;

import java.util.ArrayList;

public class AddDurableToRandomCardAction extends AbstractGameAction {

    public AddDurableToRandomCardAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.hand.isEmpty()) {
            this.isDone = true;
            return;
        }

        // Get all cards that don't already have Durable
        ArrayList<AbstractCard> validCards = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (!CardModifierManager.hasModifier(card, "DURABLE")) {
                validCards.add(card);
            }
        }

        // Apply Durable to random valid cards based on amount
        for (int i = 0; i < this.amount && !validCards.isEmpty(); i++) {
            int randomIndex = AbstractDungeon.cardRandomRng.random(validCards.size() - 1);
            AbstractCard targetCard = validCards.get(randomIndex);
            CardModifierManager.addModifier(targetCard, new DurableModifier());
            targetCard.superFlash();
            validCards.remove(randomIndex);
        }

        this.isDone = true;
    }
}