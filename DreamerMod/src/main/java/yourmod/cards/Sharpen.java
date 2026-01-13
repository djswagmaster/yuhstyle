package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyXCostAction;
import yourmod.actions.RaiseDreamDamageAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.att;

public class Sharpen extends AbstractEasyCard {
    public final static String ID = makeID("Sharpen");

    public Sharpen() {
        super(ID, -1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        magicNumber = baseMagicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            att(new RaiseDreamDamageAction(magicNumber*effect));
            return true;
        }, 0));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}