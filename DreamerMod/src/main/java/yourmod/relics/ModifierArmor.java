package yourmod.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import yourmod.util.ModifierHelper;

import static yourmod.ModFile.makeID;

public class ModifierArmor extends AbstractEasyRelic {
    public static final String ID = makeID("ModifierArmor");
    private static final int BLOCK_AMOUNT = 2;

    public ModifierArmor() {
        super(ID, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public void onPlayCard(AbstractCard c, com.megacrit.cardcrawl.monsters.AbstractMonster m) {
        if (ModifierHelper.hasAnyModifier(c)) {
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainBlockAction(
                    AbstractDungeon.player,
                    AbstractDungeon.player,
                    BLOCK_AMOUNT
            ));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BLOCK_AMOUNT + DESCRIPTIONS[1];
    }
}