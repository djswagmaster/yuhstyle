package yourmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.atb;
import static yourmod.util.Wiz.actionify;

public class Enlighten extends AbstractEasyCard {
    public final static String ID = makeID("Enlighten");

    public Enlighten() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        atb(actionify(() -> {
            // Get all upgradeable cards in hand (excluding this card)
            ArrayList<AbstractCard> upgradeable = new ArrayList<>();
            for (AbstractCard c : p.hand.group) {
                if (c.canUpgrade() && c != this) {
                    upgradeable.add(c);
                }
            }

            // Upgrade random cards
            for (int i = 0; i < magicNumber && !upgradeable.isEmpty(); i++) {
                int index = AbstractDungeon.cardRandomRng.random(upgradeable.size() - 1);
                AbstractCard toUpgrade = upgradeable.remove(index);
                toUpgrade.upgrade();
                toUpgrade.superFlash();
            }
        }));
    }

    @Override
    public void upp() {
        upgradeDamage(5);
    }
}