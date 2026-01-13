package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.ModifierStrikePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class ModifierMastery extends AbstractEasyCard {
    public final static String ID = makeID("ModifierMastery");

    public ModifierMastery() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new ModifierStrikePower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}