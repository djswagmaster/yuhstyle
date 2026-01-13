package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static yourmod.ModFile.makeID;

public class Miasma extends AbstractEasyCard {
    public final static String ID = makeID("Miasma");


    public Miasma() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        magicNumber = baseMagicNumber = 2;
        this.cardsToPreview = new Slimed();

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            this.addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
        this.addToBot(new MakeTempCardInDiscardAction(new Slimed(), 1));

    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}