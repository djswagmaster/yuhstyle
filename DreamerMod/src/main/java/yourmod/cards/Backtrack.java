package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.BacktrackPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class Backtrack extends AbstractEasyCard {
    public final static String ID = makeID("Backtrack");

    public Backtrack() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new BacktrackPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}