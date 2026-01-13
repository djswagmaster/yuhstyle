package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FatiguePower;
import yourmod.powers.FesterPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.atb;

public class Discombobulate extends AbstractEasyCard {
    public final static String ID = makeID("Discombobulate");

    public Discombobulate() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 5;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        applyToEnemy(m, new FatiguePower(m, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(2);
    }
}