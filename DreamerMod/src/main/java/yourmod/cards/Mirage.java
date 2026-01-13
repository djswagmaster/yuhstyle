package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static yourmod.ModFile.makeID;

public class Mirage extends AbstractEasyCard {
    public final static String ID = makeID("Mirage");


    public Mirage() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseBlock = block = 2;
        magicNumber = baseMagicNumber = 2;
        isEthereal = true;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeBlock(1);
    }
}