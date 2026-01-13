package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class OozeBlast extends AbstractEasyCard {
    public final static String ID = makeID("OozeBlast");

    public OozeBlast() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 18;
        baseMagicNumber = magicNumber = 2;
        isMultiDamage = true;
        this.cardsToPreview = new Slimed();

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.POISON);
        atb(new MakeTempCardInDiscardAction(new Slimed(), magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(6);
    }
}