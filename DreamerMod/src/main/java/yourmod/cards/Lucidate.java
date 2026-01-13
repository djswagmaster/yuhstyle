package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.MaterializeAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Lucidate extends AbstractEasyCard {
    public final static String ID = makeID("Lucidate");

    public Lucidate() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
       baseMagicNumber = magicNumber = 3;
       //block = baseBlock = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        atb(new MaterializeAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        //upgradeBlock(1);

    }
}