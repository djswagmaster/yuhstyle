package yourmod.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cardmods.DeadlyModifier;
import yourmod.cardmods.DreamboundModifier;

import static yourmod.ModFile.makeID;

public class Scythe extends AbstractEasyCard {
    public final static String ID = makeID("Scythe");


    public Scythe() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 8;
        CardModifierManager.addModifier(this, new DeadlyModifier());

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void upp() {
        upgradeDamage(2);
    }
}