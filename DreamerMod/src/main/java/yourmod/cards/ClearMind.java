package yourmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.dream.DreamManager;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class ClearMind extends AbstractEasyCard {
    public final static String ID = makeID("ClearMind");

    public ClearMind() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 6;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (!DreamManager.getInstance().hasCardInSlot()) {
            // No dream card - gain extra block
            atb(new GainBlockAction(p, p, magicNumber));
        }
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
    }
}