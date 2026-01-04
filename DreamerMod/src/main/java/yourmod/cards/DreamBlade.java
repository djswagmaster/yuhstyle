package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.MaterializeAction;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class DreamBlade extends AbstractEasyCard {
    public final static String ID = makeID("DreamBlade");

    public DreamBlade() {
        super(ID, 2, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Deal damage
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        // Materialize (reduce dream cost by magicNumber)
        atb(new MaterializeAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}