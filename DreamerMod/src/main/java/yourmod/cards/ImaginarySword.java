package yourmod.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cardmods.DreamboundModifier;

import static yourmod.ModFile.makeID;

public class ImaginarySword extends AbstractEasyCard {
    public final static String ID = makeID("ImaginarySword");


    public ImaginarySword() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        CardModifierManager.addModifier(this, new DreamboundModifier());

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}