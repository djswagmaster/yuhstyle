package yourmod.relics;

import yourmod.CharacterFile;
import yourmod.actions.DreamAction;

import static yourmod.ModFile.makeID;

public class DreamerItem extends AbstractEasyRelic {
    public static final String ID = makeID("DreamerItem");

    public DreamerItem() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, CharacterFile.Enums.DREAMER_COLOR);
    }

    public void atBattleStart() {
        this.flash();
        this.addToBot(new DreamAction());
    }
}
