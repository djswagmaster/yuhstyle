package yourmod.cards;

import basemod.abstracts.AbstractCardModifier;
import basemod.cardmods.EtherealMod;
import basemod.cardmods.ExhaustMod;
import basemod.cardmods.InnateMod;
import basemod.cardmods.RetainMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.cardmods.DeadlyModifier;
import yourmod.cardmods.DreamboundModifier;
import yourmod.cardmods.DurableModifier;
import yourmod.dream.DreamManager;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;

public class DreamForge extends AbstractEasyCard {
    public final static String ID = makeID("DreamForge");

    // List of possible modifiers (excluding Deadly which is attack-only)
    private static final ArrayList<AbstractCardModifier> GENERAL_MODIFIERS = new ArrayList<>();

    static {
        GENERAL_MODIFIERS.add(new InnateMod());
        GENERAL_MODIFIERS.add(new ExhaustMod());
        GENERAL_MODIFIERS.add(new EtherealMod());
        GENERAL_MODIFIERS.add(new RetainMod());
        GENERAL_MODIFIERS.add(new DreamboundModifier());
        GENERAL_MODIFIERS.add(new DurableModifier());
    }

    public DreamForge() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // Add a random modifier to be applied to card rewards
        DreamManager.getInstance().addRandomRewardModifier();
    }

    /**
     * Get a random modifier appropriate for a card
     */
    public static AbstractCardModifier getRandomModifierForCard(AbstractCard card) {
        ArrayList<AbstractCardModifier> validMods = new ArrayList<>(GENERAL_MODIFIERS);

        // Add Deadly only for attack cards
        if (card.type == AbstractCard.CardType.ATTACK) {
            validMods.add(new DeadlyModifier());
        }

        // Pick a random one
        int index = com.megacrit.cardcrawl.dungeons.AbstractDungeon.cardRandomRng.random(validMods.size() - 1);
        return validMods.get(index).makeCopy();
    }

    @Override
    public void upp() {
        upgradeBaseCost(1);
    }
}