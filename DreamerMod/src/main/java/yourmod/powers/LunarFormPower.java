package yourmod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static yourmod.ModFile.makeID;

public class LunarFormPower extends AbstractEasyPower {
    public static String POWER_ID = makeID("LunarFormPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LunarFormPower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, AbstractPower.PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        this.addToBot(new GainEnergyAction(this.amount));
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        // Make it Ethereal and add keyword to description if not already present
        if (!card.isEthereal) {
            card.isEthereal = true;
            if (!card.rawDescription.contains("Ethereal")) {
                card.rawDescription = "Ethereal. NL " + card.rawDescription;
                card.initializeDescription();
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }
}