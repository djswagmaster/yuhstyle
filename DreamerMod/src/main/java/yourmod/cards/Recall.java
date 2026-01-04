package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.DreamRecallAction;
import yourmod.actions.MaterializeAction;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Recall extends AbstractEasyCard {
    public final static String ID = makeID("Recall");

    public Recall() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
       baseMagicNumber = magicNumber = 1;
       exhaust = true;
        tags.add(CustomTags.DREAMER_CARD);

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        atb(new DreamRecallAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}