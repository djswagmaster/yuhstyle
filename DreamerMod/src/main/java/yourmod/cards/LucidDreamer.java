package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.LucidDreamerPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class LucidDreamer extends AbstractEasyCard {
    public final static String ID = makeID("LucidDreamer");

    public LucidDreamer() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new ApplyPowerAction(p, p, new LucidDreamerPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}