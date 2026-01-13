package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.AddDreamboundToRandomCardsAction;
import yourmod.powers.DeepConnectionPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class DeepConnection extends AbstractEasyCard {
    public final static String ID = makeID("DeepConnection");

    public DeepConnection() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AddDreamboundToRandomCardsAction(magicNumber));
        atb(new ApplyPowerAction(p, p, new DeepConnectionPower(p)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}