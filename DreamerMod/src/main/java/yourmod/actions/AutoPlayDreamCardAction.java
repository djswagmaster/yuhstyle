package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import yourmod.dream.DreamManager;
import yourmod.patches.DreamAutoPlayPatch;

import static yourmod.util.Wiz.att;

public class AutoPlayDreamCardAction extends AbstractGameAction {

    public AutoPlayDreamCardAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            DreamManager dm = DreamManager.getInstance();

            // Double check the card should still play
            if (!dm.hasCardInSlot() || dm.getCurrentDreamCost() > 0) {
                DreamAutoPlayPatch.setPlaying(false);
                this.isDone = true;
                return;
            }

            AbstractCard dreamCard = dm.getCardInSlot();

            // Immediately call onDreamCardPlayed to update the cost/penalty
            // This prevents the card from being played multiple times
            dm.onDreamCardPlayed();

            // Make a copy to play
            AbstractCard cardToPlay = dreamCard.makeStatEquivalentCopy();

            // Set up the card for playing
            cardToPlay.freeToPlayOnce = true;
            cardToPlay.purgeOnUse = true; // PURGE after playing, don't add to discard

            // Get a random target for enemy-targeting cards
            AbstractMonster target = null;
            if (cardToPlay.target == AbstractCard.CardTarget.ENEMY ||
                    cardToPlay.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                target = AbstractDungeon.getRandomMonster();
            }

            // Apply powers to the card (this ensures self-buffing cards work)
            cardToPlay.applyPowers();
            if (target != null) {
                cardToPlay.calculateCardDamage(target);
            }

            // Queue the card to be played
            AbstractDungeon.player.limbo.addToTop(cardToPlay);
            cardToPlay.current_x = Settings.WIDTH / 2.0f;
            cardToPlay.current_y = Settings.HEIGHT / 2.0f;
            cardToPlay.target_x = Settings.WIDTH / 2.0f;
            cardToPlay.target_y = Settings.HEIGHT / 2.0f;

            att(new NewQueueCardAction(cardToPlay, target, false, true));

            // Reset the playing flag
            att(new AbstractGameAction() {
                @Override
                public void update() {
                    DreamAutoPlayPatch.setPlaying(false);
                    this.isDone = true;
                }
            });

            this.isDone = true;
            return;
        }

        tickDuration();
    }
}

