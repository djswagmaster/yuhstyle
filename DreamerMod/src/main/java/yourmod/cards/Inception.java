package yourmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import yourmod.dream.DreamManager;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;

public class Inception extends AbstractEasyCard {
    public final static String ID = makeID("Inception");

    public Inception() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        exhaust = true;
        tags.add(CustomTags.DREAMER_CARD);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // Can only use if there's a card in the dream slot
        if (!DreamManager.getInstance().hasCardInSlot()) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.canUse(p, m);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        DreamManager dm = DreamManager.getInstance();

        if (dm.hasCardInSlot()) {
            AbstractCard dreamCard = dm.getCardInSlot();

            // Create a fresh copy of the dream card for the deck
            AbstractCard deckCopy = dreamCard.makeCopy();
            if (dreamCard.upgraded) {
                deckCopy.upgrade();
            }

            // Remove THIS card from master deck
            AbstractCard toRemove = null;
            for (AbstractCard c : p.masterDeck.group) {
                if (c.uuid.equals(this.uuid)) {
                    toRemove = c;
                    break;
                }
            }
            if (toRemove != null) {
                p.masterDeck.removeCard(toRemove);
            }

            // Add dream card copy to master deck with VFX
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(deckCopy, Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
        }
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}