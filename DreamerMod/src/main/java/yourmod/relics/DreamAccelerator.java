package yourmod.relics;

import static yourmod.ModFile.makeID;

public class DreamAccelerator extends AbstractEasyRelic {
    public static final String ID = makeID("DreamAccelerator");

    public DreamAccelerator() {
        super(ID, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    // The actual functionality is handled in ModFile.receiveOnPlayerTurnStart()
    // This relic just needs to exist and be checked

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}