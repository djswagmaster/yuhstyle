package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.LunarFormPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class LunarForm extends AbstractEasyCard {
    public final static String ID = makeID("LunarForm");

    public LunarForm() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new LunarFormPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}