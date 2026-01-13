package yourmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import yourmod.actions.DiscardToDeckAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class BacktrackPower extends AbstractEasyPower {
    public static String POWER_ID = makeID("BacktrackPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public BacktrackPower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && !AbstractDungeon.player.discardPile.isEmpty()) {
            flash();
            atb(new DiscardToDeckAction(amount, powerStrings.DESCRIPTIONS[2]));
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        } else {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[3];
        }
    }
}