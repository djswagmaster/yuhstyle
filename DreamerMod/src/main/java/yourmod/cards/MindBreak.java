package yourmod.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import yourmod.cardmods.DurableModifier;

import static yourmod.ModFile.makeID;

public class MindBreak extends AbstractEasyCard {
    public final static String ID = makeID("MindBreak");


    public MindBreak() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        magicNumber = baseMagicNumber = 1;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        dmg(m,AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber, true, AbstractGameAction.AttackEffect.NONE));

    }

    @Override
    public void upp() {
        upgradeDamage(4);
    }
}