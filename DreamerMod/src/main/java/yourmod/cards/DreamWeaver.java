package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.DreamAction;
import yourmod.actions.MaterializeAction;
import yourmod.powers.DreamWeaverPower;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class DreamWeaver extends AbstractEasyCard {
    public final static String ID = makeID("DreamWeaver");

    public DreamWeaver() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        atb(new ApplyPowerAction(p,p, new DreamWeaverPower(p,magicNumber),magicNumber));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}