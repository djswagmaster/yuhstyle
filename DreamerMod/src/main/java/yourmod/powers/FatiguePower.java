package yourmod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;

public class FatiguePower extends AbstractEasyPower {
    public static String POWER_ID = makeID("FatiguePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public FatiguePower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void atEndOfRound() {
        // Double stacks at end of round (before player's next turn)
        flash();
        this.amount *= 2;
        updateDescription();
        checkStun();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        checkStun();
    }

    @Override
    public void onInitialApplication() {
        checkStun();
    }

    private void checkStun() {
        if (!(owner instanceof AbstractMonster)) {
            return;
        }

        if (this.amount >= owner.maxHealth) {
            flash();

            // Apply stun
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(owner, AbstractDungeon.player,
                            new StunMonsterPower((AbstractMonster) owner, 1), 1));

            // Remove all stacks
            AbstractDungeon.actionManager.addToTop(
                    new ReducePowerAction(owner, owner, this,owner.maxHealth));
        }
    }

    public float getProgressPercent() {
        return Math.min(1.0f, (float) this.amount / (float) owner.maxHealth);
    }

    @Override
    public void updateDescription() {
        int percent = (int)(getProgressPercent() * 100);
        this.description = powerStrings.DESCRIPTIONS[0] + percent + powerStrings.DESCRIPTIONS[1];
    }
}