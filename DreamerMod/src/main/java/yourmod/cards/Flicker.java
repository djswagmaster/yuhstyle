package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.MaterializeAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Flicker extends AbstractEasyCard {
    public final static String ID = makeID("Flicker");

    public Flicker() {
        super(ID, 0, CardType.ATTACK, CardRarity.BASIC, CardTarget.NONE);
       baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        atb(new MaterializeAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        //this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        //initializeDescription();
    }
}