package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;

public class FortifiedDreamPower extends AbstractEasyPower {
    public static String POWER_ID = makeID("FortifiedDreamPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FortifiedDreamPower(AbstractCreature owner, int turns) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.BUFF, false, owner, turns);
        this.isTurnBased = true;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * 2f;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.amount--;
            if (this.amount <= 0) {
                this.addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this));
            } else {
                updateDescription();
            }
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