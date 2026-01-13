package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.PermanencePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
import static yourmod.util.Wiz.atb;

public class Mummify extends AbstractEasyCard {
    public final static String ID = makeID("Mummify");

    public Mummify() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 10;
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        atb(new AbstractGameAction() {
            @Override
            public void update() {
                if (m != null && (m.isDying || m.currentHealth <= 0)) {
                    applyToSelf(new PermanencePower(p, magicNumber));
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upp() {
        upgradeDamage(5);
    }
}