package yourmod.relics;

import static yourmod.ModFile.makeID;

public class LucidStarter extends AbstractEasyRelic {
    public static final String ID = makeID("LucidStarter");

    public LucidStarter() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    // The functionality is handled in ModFile.receiveOnPlayerTurnStart()
    // This relic triggers a DreamAction at the start of each turn

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}