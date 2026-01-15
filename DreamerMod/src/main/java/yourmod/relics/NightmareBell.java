package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FatiguePower;

import static yourmod.ModFile.makeID;

public class NightmareBell extends AbstractEasyRelic {
    public static final String ID = makeID("NightmareBell");
    private static final int FATIGUE_AMOUNT = 3;

    public NightmareBell() {
        super(ID, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(
                        monster,
                        AbstractDungeon.player,
                        new FatiguePower(monster, FATIGUE_AMOUNT),
                        FATIGUE_AMOUNT
                ));
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + FATIGUE_AMOUNT + DESCRIPTIONS[1];
    }
}