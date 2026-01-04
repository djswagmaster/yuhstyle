package yourmod.cardmods;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DurableModifier extends AbstractCardModifier {

    private static final String ID = "DURABLE";
    private static final int BLOCK_AMOUNT = 3;

    public DurableModifier() {
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return "dreamermod:Durable. NL " + rawDescription;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK_AMOUNT)
        );
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DurableModifier();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}