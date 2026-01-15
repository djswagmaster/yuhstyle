package yourmod.powers;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.actions.DreamAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class DozeOffPower extends AbstractEasyPower {
    public static String POWER_ID = makeID("DozeOffPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DozeOffPower(AbstractCreature owner) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.BUFF, true, owner, 1);
        this.type = PowerType.BUFF;
    }

    @Override
    public void stackPower(int stackAmount) {
        // Do nothing - prevents stacking
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.flash();
        atb(new WaitAction(10));
        atb(new DreamAction());
        atb(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0];
    }
}