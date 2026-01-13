package yourmod.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.defect.NewRipAndTearAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cardmods.DurableModifier;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Fling extends AbstractEasyCard {
    public final static String ID = makeID("Fling");


    public Fling() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = 11;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        this.addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        atb(new DrawCardAction(1));

    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}