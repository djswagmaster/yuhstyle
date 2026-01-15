package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.powers.PermanencePower;

import static yourmod.ModFile.makeID;

public class PermanenceCharm extends AbstractEasyRelic {
    public static final String ID = makeID("PermanenceCharm");
    private static final int PERMANENCE_AMOUNT = 2;

    public PermanenceCharm() {
        super(ID, RelicTier.SHOP, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(
                AbstractDungeon.player,
                AbstractDungeon.player,
                new PermanencePower(AbstractDungeon.player, PERMANENCE_AMOUNT),
                PERMANENCE_AMOUNT
        ));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + PERMANENCE_AMOUNT + DESCRIPTIONS[1];
    }
}