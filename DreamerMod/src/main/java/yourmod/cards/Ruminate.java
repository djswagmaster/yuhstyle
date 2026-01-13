package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.DreamAction;
import yourmod.actions.MaterializeAction;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Ruminate extends AbstractEasyCard {
    public final static String ID = makeID("Ruminate");

    public Ruminate() {
        super(ID, 2, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseBlock = 8;
        magicNumber = baseMagicNumber = 2;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        blck();

        atb(new MaterializeAction(magicNumber));

    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
    }
}