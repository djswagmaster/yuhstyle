package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.LucidityPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class DreamStrength extends AbstractEasyCard {
    public final static String ID = makeID("DreamStrength");

    public DreamStrength() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new LucidityPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}