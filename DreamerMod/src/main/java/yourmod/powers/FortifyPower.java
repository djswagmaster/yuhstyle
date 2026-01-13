package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.actions.AddDurableToRandomCardAction;

import static yourmod.ModFile.makeID;

public class FortifyPower extends AbstractEasyPower {
    public static String POWER_ID = makeID("FortifyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FortifyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.flash();
        this.addToBot(new AddDurableToRandomCardAction(this.amount));
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = powerStrings.DESCRIPTIONS[0];
        } else {
            this.description = powerStrings.DESCRIPTIONS[1] + this.amount + powerStrings.DESCRIPTIONS[2];
        }
    }
}