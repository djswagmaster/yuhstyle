package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FortifiedDreamPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Brace extends AbstractEasyCard {
    public final static String ID = makeID("Brace");

    public Brace() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new ApplyPowerAction(p, p, new FortifiedDreamPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        exhaust = false;
    }
}