package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FortifyPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class Fortify extends AbstractEasyCard {
    public final static String ID = makeID("Fortify");

    public Fortify() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new FortifyPower(p, magicNumber));
    }

    @Override
    public void upp() {
        isInnate = true;
    }
}