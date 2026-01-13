package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.actions.MaterializeAction;

import static yourmod.ModFile.makeID;

public class DreamWeaverPower extends AbstractEasyPower{
    public static String POWER_ID = makeID("DreamWeaverPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DreamWeaverPower(AbstractCreature owner, int amount){
        super(POWER_ID,powerStrings.NAME, AbstractPower.PowerType.BUFF,false,owner,amount);

    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        this.flash();
        this.addToBot(new MaterializeAction(amount));


    }
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }
}
