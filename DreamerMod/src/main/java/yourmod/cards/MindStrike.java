package yourmod.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cardmods.DreamboundModifier;

import static yourmod.ModFile.makeID;

public class MindStrike extends AbstractEasyCard {
    public final static String ID = makeID("MindStrike");


    public MindStrike() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 9;
        tags.add(CardTags.STRIKE);
        selfRetain = true;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        if (m != null && m.getIntentBaseDmg() >= 0) {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}