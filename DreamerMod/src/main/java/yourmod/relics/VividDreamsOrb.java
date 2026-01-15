package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static yourmod.ModFile.makeID;

public class VividDreamsOrb extends AbstractEasyRelic {
    public static final String ID = makeID("VividDreamsOrb");

    public VividDreamsOrb() {
        super(ID, RelicTier.BOSS, LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    // The dream cost increase is handled in DreamManager.getCurrentDreamCost()
    // and DreamManager.calculateDreamCostForCard()

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}