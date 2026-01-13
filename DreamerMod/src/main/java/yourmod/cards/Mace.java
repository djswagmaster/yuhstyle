package yourmod.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.MaterializeAction;
import yourmod.cardmods.DurableModifier;

import static yourmod.ModFile.makeID;

public class Mace extends AbstractEasyCard {
    public final static String ID = makeID("Mace");


    public Mace() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        magicNumber = baseMagicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        dmg(m,AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        this.addToBot(new MaterializeAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(1);
        upgradeMagicNumber(1);
    }
}