package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.GainEnergyFromModifiersAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Siphon extends AbstractEasyCard {
    public final static String ID = makeID("Siphon");

    public Siphon() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new GainEnergyFromModifiersAction());
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}