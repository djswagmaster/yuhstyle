package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.EasyXCostAction;
import yourmod.actions.MaterializeAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.att;

public class Unleash extends AbstractEasyCard {
    public final static String ID = makeID("Unleash");

    public Unleash() {
        super(ID, -1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 0; // Upgrade bonus
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            int manifestAmount = effect + params[0]; // X + upgrade bonus (0 or 1)
            // Add in reverse order since att adds to top
            for (int i = effect - 1; i >= 0; i--) {
                att(new MaterializeAction(manifestAmount));
            }
            return true;
        }, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}