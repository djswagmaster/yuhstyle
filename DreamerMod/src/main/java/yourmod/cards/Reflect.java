package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.dream.DreamManager;

import static yourmod.ModFile.makeID;

public class Reflect extends AbstractEasyCard {
    public final static String ID = makeID("Reflect");

    public Reflect() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = block = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    private int getDreamCountBonus() {
        return DreamManager.getInstance().getTimesDreamed();
    }

    @Override
    public void applyPowers() {
        int bonus = getDreamCountBonus();

        int originalBaseBlock = this.baseBlock;
        this.baseBlock += bonus;

        super.applyPowers();

        this.baseBlock = originalBaseBlock;

        if (bonus > 0) {
            this.isBlockModified = true;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int bonus = getDreamCountBonus();

        int originalBaseBlock = this.baseBlock;
        this.baseBlock += bonus;

        super.calculateCardDamage(mo);

        this.baseBlock = originalBaseBlock;

        if (bonus > 0) {
            this.isBlockModified = true;
        }
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}