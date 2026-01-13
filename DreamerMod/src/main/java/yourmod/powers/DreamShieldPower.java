package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;

public class DreamShieldPower extends AbstractEasyPower {
    public static String POWER_ID = makeID("DreamShieldPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public DreamShieldPower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.BUFF, false, owner, amount);
    }

    public void onDreamCardPlayed() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, amount));
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }
}