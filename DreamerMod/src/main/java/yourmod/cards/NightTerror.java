package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.forAllMonstersLiving;

public class NightTerror extends AbstractEasyCard {
    public final static String ID = makeID("NightTerror");

    public NightTerror() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 10;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(monster -> {
            if (hasDebuff(monster)) {
                atb(new DamageAction(monster, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
            }
        });
    }

    private boolean hasDebuff(AbstractMonster monster) {
        for (AbstractPower power : monster.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}