package yourmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.StarBounceEffect;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.shuffleIn;

public class RecurringNightmare extends AbstractEasyCard {
    public final static String ID = makeID("RecurringNightmare");

    public RecurringNightmare() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 18;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (Settings.FAST_MODE) {
            this.addToBot(new VFXAction(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.RED)));

            for(int i = 0; i < 4; ++i) {
                this.addToBot(new VFXAction(new StarBounceEffect(m.hb.cX, m.hb.cY)));
            }
        } else {
            this.addToBot(new VFXAction(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.RED), 0.4F));

            for(int i = 0; i < 4; ++i) {
                this.addToBot(new VFXAction(new StarBounceEffect(m.hb.cX, m.hb.cY)));
            }
        }
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        AbstractCard copy = makeStatEquivalentCopy();
        copy.freeToPlayOnce = false;
        shuffleIn(copy);
    }

    @Override
    public void upp() {
        upgradeDamage(6);
    }
}