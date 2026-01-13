package yourmod.cards;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.ApplyModToHandCardAction;
import yourmod.actions.DreamAction;
import yourmod.cardmods.DreamboundModifier;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class CastOff extends AbstractEasyCard {
    public final static String ID = makeID("CastOff");

    public CastOff() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        tags.add(CustomTags.DREAMER_CARD);

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        atb(new ApplyModToHandCardAction(new DreamboundModifier(), "DREAMBOUND"));
        atb(new WaitAction(10));
        atb(new DreamAction());

    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}