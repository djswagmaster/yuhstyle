package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
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
        this.addToTop(new DrawCardAction(1));
        this.addToBot(new DreamAction());
    }
}
