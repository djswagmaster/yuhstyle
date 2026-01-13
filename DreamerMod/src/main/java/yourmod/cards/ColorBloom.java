package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.util.ModifierHelper;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class ColorBloom extends AbstractEasyCard {
    public final static String ID = makeID("ColorBloom");

    public ColorBloom() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 10;
        isInnate = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);

        int modifierCount = ModifierHelper.getModifierCount(this);
        if (modifierCount > 0) {
            atb(new DrawCardAction(modifierCount));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(5);
    }
}