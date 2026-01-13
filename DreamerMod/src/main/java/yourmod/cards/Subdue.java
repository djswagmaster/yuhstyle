package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.*;

public class Subdue extends AbstractEasyCard {
    public final static String ID = makeID("Subdue");

    public Subdue() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Reduce all enemies' strength temporarily
        forAllMonstersLiving(mo -> {
            applyToEnemy(mo, new StrengthPower(mo, -magicNumber));
            // Only apply GainStrengthPower if enemy doesn't have Artifact
            if (!mo.hasPower(ArtifactPower.POWER_ID)) {
                applyToEnemy(mo, new GainStrengthPower(mo, magicNumber));
            }
        });

        // Retain hand this turn
        applyToSelf(new EquilibriumPower(p, 1));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}