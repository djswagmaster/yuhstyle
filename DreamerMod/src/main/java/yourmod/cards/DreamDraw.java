package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.DreamDrawPower;
import yourmod.powers.LucidityPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;

public class DreamDraw extends AbstractEasyCard {
    public final static String ID = makeID("DreamDraw");

    public DreamDraw() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DreamDrawPower(p, magicNumber));
    }

    @Override
    public void upp() {
        isInnate = true;

    }
}