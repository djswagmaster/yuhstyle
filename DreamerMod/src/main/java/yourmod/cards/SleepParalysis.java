package yourmod.cards;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import yourmod.powers.DrowsyPower;
import yourmod.powers.FatiguePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;

public class SleepParalysis extends AbstractEasyCard {
    public final static String ID = makeID("SleepParalysis");

    public SleepParalysis() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        exhaust = true;
        baseMagicNumber = magicNumber = 20;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> {
            applyToEnemy(mo, new FatiguePower(mo, magicNumber));
        });

        //applyToSelf(new StrengthPower(p, -magicNumber));
        //applyToSelf(new DexterityPower(p, -magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(5);
    }
}