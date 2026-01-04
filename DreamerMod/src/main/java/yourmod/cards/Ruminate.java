package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.DreamAction;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Ruminate extends AbstractEasyCard {
    public final static String ID = makeID("Ruminate");

    public Ruminate() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseBlock = 5;
        tags.add(CustomTags.DREAMER_CARD);

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain some block
        blck();

        // Inspire a card from hand
        atb(new DreamAction());
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}