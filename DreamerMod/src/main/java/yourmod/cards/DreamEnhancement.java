package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.ApplyModToHandCardAction;
import yourmod.cardmods.DreamboundModifier;
import yourmod.cards.AbstractEasyCard;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class DreamEnhancement extends AbstractEasyCard {
    public final static String ID = makeID("DreamEnhancement");

    public DreamEnhancement() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        baseBlock = 5;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain block
        blck();

        // Apply Dreambound modifier to a card in hand (won't apply if already has it)
        atb(new ApplyModToHandCardAction(new DreamboundModifier(), "DREAMBOUND"));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(2);
    }
}