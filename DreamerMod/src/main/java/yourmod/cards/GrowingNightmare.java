package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.GrowingNightmarePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class GrowingNightmare extends AbstractEasyCard {
    public final static String ID = makeID("GrowingNightmare");

    public GrowingNightmare() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new GrowingNightmarePower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}