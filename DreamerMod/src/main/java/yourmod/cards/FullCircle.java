package yourmod.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cardmods.DurableModifier;

import static yourmod.ModFile.makeID;

public class FullCircle extends AbstractEasyCard {
    public final static String ID = makeID("FullCircle");


    public FullCircle() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 1;
        baseBlock = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        blck();
        dmg(m,AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upp() {
        upgradeDamage(10);
        upgradeBlock(10);
    }
}