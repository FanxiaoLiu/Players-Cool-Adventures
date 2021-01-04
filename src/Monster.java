import java.util.ArrayList;

public class Monster {
    private ArrayList<Integer> characterStats = new ArrayList<Integer>();
    private ArrayList<Integer> characterInventoryStats = new ArrayList<Integer>();
    private Inventory playerInventory = new Inventory();
    public Monster(int index0, int index1, int index2, int index3, int index4) {
        characterStats.add(index0);
        characterStats.add(index1);
        characterStats.add(index2);
        characterStats.add(index3);
        characterStats.add(index4);
    }
    public void refreshStats() {
        characterInventoryStats.clear();
        playerInventory.overallStatBuff();
        for (int i = 0; i<characterStats.size(); i++) {
            characterInventoryStats.add(characterStats.get(i)+playerInventory.getOverallStatBuff(i));
        }
    }

    public void addItemsToInventory(Items item) {
        playerInventory.addItemsToInventory(item);
    }

    public Integer displayStats(int index) {
        return characterInventoryStats.get(index);
    }
}
