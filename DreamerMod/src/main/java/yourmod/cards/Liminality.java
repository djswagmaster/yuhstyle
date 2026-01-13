package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;

public class Liminality extends AbstractEasyCard {
    public final static String ID = makeID("Liminality");

    public Liminality() {
        super(ID, 7, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 50;
        isMultiDamage = true;
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Only deal damage if played from dream slot (manifested)
        if (this.hasTag(CustomTags.DREAM_PLAYING)) {
            allDmg(AbstractGameAction.AttackEffect.FIRE);
        }
    }

    @Override
    public void upp() {
        upgradeDamage(20);
    }
}