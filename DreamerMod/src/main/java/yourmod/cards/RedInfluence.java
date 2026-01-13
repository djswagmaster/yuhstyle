package yourmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.actions.DreamAction;
import yourmod.actions.ChooseColorCardAction;
import yourmod.tags.CustomTags;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;

public class RedInfluence extends AbstractEasyCard {
    public final static String ID = makeID("RedInfluence");

    public RedInfluence() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        tags.add(CustomTags.DREAMER_CARD);

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new ChooseColorCardAction(AbstractCard.CardColor.RED, 3));
        
        if (upgraded) {
            atb(new DreamAction());
        }
    }

    @Override
    public void upp() {
        rawDescription = CardCrawlGame.languagePack.getCardStrings(ID).UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
