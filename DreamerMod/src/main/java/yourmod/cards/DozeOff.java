package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DozeOffPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class DozeOff extends AbstractEasyCard {
    public final static String ID = makeID("DozeOff");

    public DozeOff() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 8;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new ApplyPowerAction(p, p, new DozeOffPower(p)));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}