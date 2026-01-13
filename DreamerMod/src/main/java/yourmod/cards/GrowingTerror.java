package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;

public class GrowingTerror extends AbstractEasyCard {
    public final static String ID = makeID("GrowingTerror");

    private boolean wasInHand = false;

    public GrowingTerror() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    @Override
    public void update() {
        super.update();

        boolean isInHand = AbstractDungeon.player != null &&
                AbstractDungeon.player.hand.contains(this);

        if (isInHand && !wasInHand) {
            // Just entered hand - increase damage
            baseDamage += magicNumber;
            applyPowers();
        }

        wasInHand = isInHand;
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}