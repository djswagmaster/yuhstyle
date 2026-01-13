package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.actions.DreamAction;

import static yourmod.ModFile.makeID;

public class LucidDreamerPower extends AbstractEasyPower {
    public static String POWER_ID = makeID("LucidDreamerPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private int triggeredThisTurn = 0;

    public LucidDreamerPower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        triggeredThisTurn = 0;
    }

    public void onDreamCardPlayed() {
        if (triggeredThisTurn < this.amount) {
            triggeredThisTurn++;
            this.flash();
            this.addToBot(new DreamAction());
        }
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