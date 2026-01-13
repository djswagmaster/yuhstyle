package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Premonition extends AbstractEasyCard {
    public final static String ID = makeID("Premonition");

    public Premonition() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        this.isInnate = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new SeekAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}