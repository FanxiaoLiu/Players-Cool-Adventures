import java.util.ArrayList;

public class Character {
    private ArrayList<Integer> characterStats = new ArrayList<Integer>();
    private ArrayList<Integer> characterInventoryStats = new ArrayList<Integer>();
    private Inventory playerInventory = new Inventory();
    private Integer currentHP;
    private String name;
    private Boolean isDead = false;
    private Integer healChances = 5;
    private Integer id = 0;

    public Character(String characterName, int index0, int index1, int index2, int index3, int index4) {
        characterStats.add(index0);
        characterStats.add(index1);
        characterStats.add(index2);
        characterStats.add(index3);
        characterStats.add(index4);
        name = characterName;
    }

    public Character(String characterName, Integer idValue, int index0, int index1, int index2, int index3, int index4) {
        characterStats.add(index0);
        characterStats.add(index1);
        characterStats.add(index2);
        characterStats.add(index3);
        characterStats.add(index4);
        name = characterName;
        id = idValue;
    }

    public void refreshStats() {
        characterInventoryStats.clear();
        playerInventory.overallStatBuff();
        for (int i = 0; i<characterStats.size(); i++) {
            characterInventoryStats.add(characterStats.get(i)+playerInventory.getOverallStatBuff(i));
        }
    }

    public void setBeginningHP() {
        currentHP = characterInventoryStats.get(1);
    }

    public void resetHealChances() {
        healChances = 5;
    }

    public Integer returnHealChances() {
        return healChances;
    }

    public void useHealChance() {
        healChances--;
    }

    public void addHealChance() {
        healChances++;
    }

    public void addHealChance(int times) {
        healChances += times;
    }

    public void healCharacter() {
        Integer heal = characterInventoryStats.get(4);
        if (currentHP == characterInventoryStats.get(1)) {
            System.out.println(name + "'s' health is already at max, it cannot go any higher.");
            System.out.println(name + " now has " + currentHP + " health points.");
        }
        else if (currentHP + heal > characterInventoryStats.get(1)) {
            currentHP = characterInventoryStats.get(1);
            System.out.println(name + "'s health was healed to max.");
            System.out.println(name + " now has " + currentHP + " health points.");
            healChances--;
            System.out.println("You used 1 heal. You have " + healChances + " heal(s) left. Use them wisely");
        }
        else {
            currentHP += heal;
            System.out.println(name + " was healed by " + heal);
            System.out.println(name + " now has " + currentHP + " health points.");
            healChances--;
            System.out.println("You used 1 heal. You have " + healChances + " heal(s) left. Use them wisely");
        }
    }

    public void damageTaken(Character otherPlayer) {
        Integer damageDealt = calculateDamageDealt(otherPlayer);
        System.out.print(otherPlayer.getName() + " dealt " + name + " " + damageDealt + " of damage.");
        if (currentHP - damageDealt > 0) {
            currentHP -= damageDealt;
            System.out.println(name + " now has " + currentHP + " health points.");
        }
        else {
            currentHP = 0;
            System.out.println(otherPlayer.getName() + " has killed " + name + ".");
            isDead = true;
        }
    }

    public int getNumberofItemsinInventory() {
        return playerInventory.getNumberofItems();
    }

    public void hurtCharacter(int hP) {
        currentHP -= hP;
    }

    private Integer calculateDamageDealt(Character otherPlayer) {
        Integer damageDealt = 0;

        Integer critRate = otherPlayer.displayStats(3);
        Integer dP = otherPlayer.displayStats(0);
        Boolean isCrit = false;

        if (critRate > 300) {
            critRate = 300;
        }

        int crit = (int)(Math.random() * 101);

        if (crit > critRate) {
            isCrit = false;
        }
        else {
            isCrit = true;
            System.out.println(otherPlayer.getName() + " has dealt critical damage to " + name + "!");
        }

        if (isCrit)
            damageDealt = (int)((((double)crit/100.0) * (double)dP)) + dP;
        else
            damageDealt = dP;

        return damageDealt;
    }

    public Boolean getisDead() {
        return isDead;
    }

    public String getName() {
        return name;
    }

    public Integer getCurrentHP() {
        return currentHP;
    }

    public void addItemsToInventory(Items item) {
        playerInventory.addItemsToInventory(item);
    }

    public void removeItemsFromInventory(int index) {
        playerInventory.removeItemsFromInventory(index);
    }

    public Items getItemInInventory(int index) {
        return playerInventory.getItemInInventory(index);
    }

    public Integer displayStats(int index) {
        return characterInventoryStats.get(index);
    }

    public static void main(String[] args) {
        Items swordofJustice = new Items(21, 100, "Sword of Justice", 2, 4, 7, 3, 3);
        Items bowofJustice = new Items(15, 50, "Bow of Justice", 2, 4, 7, 3, 3);
        Items spearofJustice = new Items(31 ,75, "Spear of Justice", 2, 4, 7, 3, 3);

    }

    public void replaceInventoryItem(int index, Items item) {
        playerInventory.replaceInventoryItem(index, item);
    }
}
