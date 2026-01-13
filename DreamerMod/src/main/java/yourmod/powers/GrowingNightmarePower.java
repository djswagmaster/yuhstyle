package yourmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;

public class GrowingNightmarePower extends AbstractEasyPower {
    public static String POWER_ID = makeID("GrowingNightmarePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public GrowingNightmarePower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            flash();
            // Deal damage to all enemies
            addToBot(new DamageAllEnemiesAction(owner,
                    DamageInfo.createDamageMatrix(this.amount, true),
                    DamageInfo.DamageType.THORNS,
                    AbstractGameAction.AttackEffect.FIRE, true));

            // Double the amount
            this.amount += this.amount;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }
}