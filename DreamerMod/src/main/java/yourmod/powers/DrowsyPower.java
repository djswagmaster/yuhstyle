package yourmod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;

public class DrowsyPower extends AbstractEasyPower {
    public static String POWER_ID = makeID("DrowsyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    private static final float HP_PERCENT = 0.015f; // 1.5% of max HP

    private int currentThreshold = -1; // Stored threshold that grows

    public DrowsyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.DEBUFF, false, owner, amount);
    }

    private int getBaseThreshold() {
        int base;

        if (owner instanceof AbstractMonster) {
            AbstractMonster.EnemyType type = ((AbstractMonster) owner).type;

            if (type == AbstractMonster.EnemyType.BOSS) {
                base = 8;
            } else if (type == AbstractMonster.EnemyType.ELITE) {
                base = 6;
            } else {
                base = 3;
            }
        } else {
            base = 3;
        }

        int hpComponent = (int)(owner.maxHealth * HP_PERCENT);

        return Math.max(1, base + hpComponent);
    }

    public int getThreshold() {
        if (currentThreshold < 0) {
            currentThreshold = getBaseThreshold();
        }
        return currentThreshold;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        checkStun();
    }

    @Override
    public void onInitialApplication() {
        currentThreshold = getBaseThreshold();
        checkStun();
    }

    private void checkStun() {
        if (!(owner instanceof AbstractMonster)) {
            return;
        }

        int totalStuns = 0;
        int threshold = getThreshold();

        // Calculate how many stuns we can apply
        while (this.amount >= threshold) {
            this.amount -= threshold;
            totalStuns++;
            // Increase threshold by 50%
            currentThreshold = threshold + (threshold / 2);
            threshold = currentThreshold;
        }

        if (totalStuns > 0) {
            flash();

            // Apply all stuns at once
            AbstractDungeon.actionManager.addToTop(
                    new ApplyPowerAction(owner, AbstractDungeon.player,
                            new StunMonsterPower((AbstractMonster) owner, totalStuns), totalStuns));

            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToTop(
                        new RemoveSpecificPowerAction(owner, owner, this));
            } else {
                updateDescription();
            }
        }
    }

    @Override
    public void updateDescription() {
        int threshold = getThreshold();
        this.description = powerStrings.DESCRIPTIONS[0] + threshold + powerStrings.DESCRIPTIONS[1];
    }
}