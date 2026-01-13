package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

public class Envy extends AbstractEasyCard {
    public final static String ID = makeID("Envy");


    public Envy() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 6;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        dmg(m,AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        if (m.currentHealth > p.currentHealth) {
            this.addToBot(new HealAction(p, p, magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(2);
    }
}