package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

public class Desperation extends AbstractEasyCard {
    public final static String ID = makeID("Desperation");

    private static final int BASE = 30;
    private static final int UPGRADE_PLUS = 5;

    public Desperation() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = BASE;
        baseMagicNumber = magicNumber = BASE;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void applyPowers() {
        int discardSize = AbstractDungeon.player.discardPile.size();
        baseDamage = Math.max(0, magicNumber - discardSize);
        super.applyPowers();
        isDamageModified = baseDamage != magicNumber;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int discardSize = AbstractDungeon.player.discardPile.size();
        baseDamage = Math.max(0, magicNumber - discardSize);
        super.calculateCardDamage(mo);
        isDamageModified = baseDamage != magicNumber;
    }

    @Override
    public void upp() {
        upgradeMagicNumber(UPGRADE_PLUS);
    }
}