package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.FesterPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToEnemy;
import static yourmod.util.Wiz.atb;

public class Fester extends AbstractEasyCard {
    public final static String ID = makeID("Fester");

    public Fester() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.POISON);

        if (m.hasPower(FesterPower.POWER_ID)) {
            int stacks = m.getPower(FesterPower.POWER_ID).amount;
            atb(new DamageAction(m, new DamageInfo(p, stacks, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
        }

        applyToEnemy(m, new FesterPower(m, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}