package yourmod.cards;

import com.megacrit.cardcrawl.actions.defect.DiscardPileToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class Recall extends AbstractEasyCard {
    public final static String ID = makeID("Recall");

    public Recall() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Only try to return a card if discard pile isn't empty
        if (!p.discardPile.isEmpty()) {
            atb(new DiscardPileToHandAction(1));
        }
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}