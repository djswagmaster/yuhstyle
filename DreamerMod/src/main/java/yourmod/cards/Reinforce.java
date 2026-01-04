package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.ApplyModToHandCardAction;
import yourmod.cardmods.DreamboundModifier;
import yourmod.cardmods.DurableModifier;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Reinforce extends AbstractEasyCard {
    public final static String ID = makeID("Reinforce");

    public Reinforce() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 5;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();

        atb(new ApplyModToHandCardAction(new DurableModifier(), "DURABLE"));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
    }
}