package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FatiguePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;

public class Lullaby extends AbstractEasyCard {
    public final static String ID = makeID("Lullaby");

    public Lullaby() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 10;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        applyToEnemy(m, new FatiguePower(m, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(5);
    }
}