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

    // Two constructors, one with an additional ID Value
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

    // Refreshes stats of a character, taking into account their inventory
    public void refreshStats() {
        characterInventoryStats.clear();
        playerInventory.overallStatBuff();
        for (int i = 0; i<characterStats.size(); i++) {
            characterInventoryStats.add(characterStats.get(i)+playerInventory.getOverallStatBuff(i));
        }
    }

    // Set beginning hP based on max hP
    public void setBeginningHP() {
        currentHP = characterInventoryStats.get(1);
    }

    // Reset heal chances to beginning
    public void resetHealChances() {
        healChances = 5;
    }

    // Get total heal chances left
    public Integer returnHealChances() {
        return healChances;
    }

    // Use heal chance
    public void useHealChance() {
        healChances--;
    }

    public void addHealChance() {
        healChances++;
    }

    public void addHealChance(int times) {
        healChances += times;
    }

    // Heals character, scales based off the heal stat on the character
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

    // Outputs damage taken
    //If currentHP is 0, then isDead = true
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

    // Calculate damage dealt, including critical damage
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

    // Edits inventory of the player
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

    public void replaceInventoryItem(int index, Items item) {
        playerInventory.replaceInventoryItem(index, item);
    }
}
