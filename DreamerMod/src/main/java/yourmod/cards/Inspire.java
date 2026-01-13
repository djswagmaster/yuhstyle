package yourmod.cards;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.DreamAction;
import yourmod.actions.MaterializeAction;
import yourmod.cardmods.DreamboundModifier;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Inspire extends AbstractEasyCard {
    public final static String ID = makeID("Inspire");

    public Inspire() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        CardModifierManager.addModifier(this, new DreamboundModifier());

    }

    public void use(AbstractPlayer p, AbstractMonster m) {

        atb(new GainEnergyAction(magicNumber));
        atb(new DrawCardAction(1));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}