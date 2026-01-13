package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.DreamAction;
import yourmod.actions.MaterializeAction;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class DreamBlade extends AbstractEasyCard {
    public final static String ID = makeID("DreamBlade");

    public DreamBlade() {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = 8;
        tags.add(CustomTags.DREAMER_CARD);

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);

        atb(new DreamAction());
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}