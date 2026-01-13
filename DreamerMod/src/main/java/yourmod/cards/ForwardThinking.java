package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

import java.util.ArrayList;

public class ForwardThinking extends AbstractEasyCard {
    public final static String ID = makeID("ForwardThinking");

    public ForwardThinking() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Exhaust all cards in discard pile
        ArrayList<AbstractCard> cardsToExhaust = new ArrayList<>(p.discardPile.group);
        for (AbstractCard card : cardsToExhaust) {
            atb(new ExhaustSpecificCardAction(card, p.discardPile));
        }

        atb(new ApplyPowerAction(p, p, new DrawPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        //upgradeMagicNumber(1);
        upgradeBaseCost(0);
    }
}