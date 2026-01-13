package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.powers.PermanencePower;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.applyToSelf;
import static yourmod.util.Wiz.atb;

public class ObjectPermanence extends AbstractEasyCard {
    public final static String ID = makeID("ObjectPermanence");

    public ObjectPermanence() {
        super(ID, -2, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void triggerWhenDrawn() {
        atb(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        applyToSelf(new PermanencePower(AbstractDungeon.player, magicNumber));
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Unplayable
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}