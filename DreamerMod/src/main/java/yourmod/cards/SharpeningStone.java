package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.ApplyModToHandCardAction;
import yourmod.actions.RaiseDreamDamageAction;
import yourmod.cardmods.DeadlyModifier;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class SharpeningStone extends AbstractEasyCard {
    public final static String ID = makeID("SharpeningStone");

    public SharpeningStone() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        magicNumber = baseMagicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        atb(new RaiseDreamDamageAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}