package yourmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static yourmod.ModFile.makeID;
import static yourmod.util.Wiz.forAllMonstersLiving;

public class NightmareFuel extends AbstractEasyCard {
    public final static String ID = makeID("NightmareFuel");

    public NightmareFuel() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 15;
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> {
            mo.decreaseMaxHealth((int) (magicNumber*mo.maxHealth*.01));
        });
    }

    @Override
    public void upp() {
        //this.isInnate = true;
        upgradeMagicNumber(5);
    }
}