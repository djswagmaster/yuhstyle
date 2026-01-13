package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.Collections;

public class ChooseColorCardAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private AbstractCard.CardColor color;
    private int numChoices;

    public ChooseColorCardAction(AbstractCard.CardColor color, int numChoices) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.color = color;
        this.numChoices = numChoices;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> validCards = new ArrayList<>();
            
            for (AbstractCard card : CardLibrary.getAllCards()) {
                if (card.color == color && 
                    card.rarity != AbstractCard.CardRarity.BASIC &&
                    card.rarity != AbstractCard.CardRarity.SPECIAL &&
                    !card.hasTag(AbstractCard.CardTags.HEALING)) {
                    validCards.add(card.makeCopy());
                }
            }
            
            Collections.shuffle(validCards, new java.util.Random(AbstractDungeon.cardRandomRng.randomLong()));
            
            ArrayList<AbstractCard> choices = new ArrayList<>();
            for (int i = 0; i < numChoices && i < validCards.size(); i++) {
                choices.add(validCards.get(i));
            }
            
            if (choices.isEmpty()) {
                this.isDone = true;
                return;
            }
            
            AbstractDungeon.cardRewardScreen.customCombatOpen(choices, CardRewardScreen.TEXT[1], false);
            this.tickDuration();
            return;
        }
        
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                AbstractCard card = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                
                if (AbstractDungeon.player.hand.size() < 10) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                } else {
                    AbstractDungeon.player.discardPile.addToTop(card);
                }
                
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        
        this.tickDuration();
    }
}
