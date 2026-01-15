package yourmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import yourmod.dream.DreamManager;
import yourmod.patches.DreamAutoPlayPatch;
import yourmod.tags.CustomTags;

import static yourmod.util.Wiz.att;

/**
 * Plays the dream card when its cost reaches 0.
 * The DreamCardUseActionPatch will intercept the card after playing
 * and return it to the dream pile (or clear the slot for exhaust/power cards).
 */
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

            // Get the ACTUAL card from the dream slot
            AbstractCard cardToPlay = dm.getCardInSlot();

            // Log for debugging
            System.out.println("Auto-playing dream card: " + cardToPlay.name +
                    " (Type: " + cardToPlay.type +
                    ", Exhaust: " + cardToPlay.exhaust + ")");

            // Play special SFX for auto-play
            CardCrawlGame.sound.play("POWER_FLIGHT", 0.5f);
            CardCrawlGame.sound.play("CARD_POWER_WOOSH", 0.3f);

            // Brief room tint for dramatic effect
            AbstractDungeon.effectsQueue.add(new RoomTintEffect(
                    new com.badlogic.gdx.graphics.Color(0.6f, 0.4f, 1.0f, 0.3f),
                    0.3f
            ));

            // Set up the card for playing
            cardToPlay.freeToPlayOnce = true;
            cardToPlay.purgeOnUse = false; // We handle where it goes

            // Add tag to indicate this card is being played from dream slot
            // The DreamCardUseActionPatch will check for this tag
            cardToPlay.tags.add(CustomTags.DREAM_PLAYING);

            // Get a random target for enemy-targeting cards
            AbstractMonster target = null;
            if (cardToPlay.target == AbstractCard.CardTarget.ENEMY ||
                    cardToPlay.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                target = AbstractDungeon.getRandomMonster();
            }

            // Apply powers to the card
            cardToPlay.applyPowers();
            if (target != null) {
                cardToPlay.calculateCardDamage(target);
            }

            // Position the card for playing animation
            cardToPlay.current_x = Settings.WIDTH / 2.0f;
            cardToPlay.current_y = Settings.HEIGHT / 2.0f;
            cardToPlay.target_x = Settings.WIDTH / 2.0f;
            cardToPlay.target_y = Settings.HEIGHT / 2.0f;

            // Reset visual state for playing
            cardToPlay.transparency = 1.0f;
            cardToPlay.fadingOut = false;

            // Add to limbo for the play animation, but DON'T remove from dreamPile yet!
            // This way hasCardInSlot() returns true during the card's effect.
            // The DreamCardUseActionPatch will handle removing from both places.
            AbstractDungeon.player.limbo.addToTop(cardToPlay);

            // Queue the card to be played
            // DreamCardUseActionPatch will intercept after playing, handle cleanup, and reset isPlaying flag
            att(new NewQueueCardAction(cardToPlay, target, false, true));

            this.isDone = true;
            return;
        }

        tickDuration();
    }
}