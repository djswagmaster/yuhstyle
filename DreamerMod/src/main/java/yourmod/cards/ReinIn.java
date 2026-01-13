package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.SetDreamCostAction;
import yourmod.dream.DreamManager;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class ReinIn extends AbstractEasyCard {
    public final static String ID = makeID("ReinIn");

    public ReinIn() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 8;
        magicNumber = baseMagicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        atb(new SetDreamCostAction(Math.max(1, DreamManager.getInstance().getCurrentDreamCost() - magicNumber)));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}