package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.ApplyModToHandCardAction;
import yourmod.cardmods.RetainModifier;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class HoldOn extends AbstractEasyCard {
    public final static String ID = makeID("HoldOn");

    public HoldOn() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 6;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();

        atb(new ApplyModToHandCardAction(new RetainModifier(), "RETAIN"));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}