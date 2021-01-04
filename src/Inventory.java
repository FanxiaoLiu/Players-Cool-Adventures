import java.util.ArrayList;

public class Inventory {
    private ArrayList<Items> inventoryItems;
    private ArrayList<Integer> overallStatBuff = new ArrayList<Integer>();
    private Integer inventorySize = 1000;
    private Integer totalStorStatBuff = 0;
    private Boolean isRoom = false;
    public Inventory() {
        inventoryItems = new ArrayList<Items>();
    }

    public Inventory(Boolean isRoomInventory) {
        inventoryItems = new ArrayList<Items>();
        isRoom = isRoomInventory;
    }

    public void setInventorySize() {
        if (!isRoom) {
            inventorySize = 1000;
            inventorySize += totalStorStatBuff;
        }
        else {
            inventorySize = 1000000;
        }
    }
    public void updateInventorySize() {
        setInventorySize();
        Integer totalInventoryItemSize = getTotalItemWeight();
        
        if (isInventoryFull(totalInventoryItemSize)) {
            System.out.println("Please remove an item from the inventory, it's too heavy for your bag to hold.");
            updateInventorySize();
        }
    }

    public int getNumberofItems() {
        return inventoryItems.size();
    }

    public void getTotalStorStatBuff(Integer buff) {
        totalStorStatBuff = buff;
    }

    public Integer getTotalItemWeight() {
        Integer totalInventoryItemSize = 0;
        for (int i = 0; i < inventoryItems.size(); i++) {
            totalInventoryItemSize += inventoryItems.get(i).returnWeight();
        }
        return totalInventoryItemSize;
    }


    public Integer returnCurrentInventorySize() {
        return inventorySize;
    }

    public void addItemsToInventory(Items item) {
        updateInventorySize();
        if (!isInventoryFull((getTotalItemWeight() + item.returnWeight())))
            inventoryItems.add(item);
        else
            System.out.println("This item would make the bag too heavy for you to carry. The item was not taken");
    }

    public ArrayList<Integer> getOverallStatBuff() {
        return overallStatBuff;
    }

    public Integer getOverallStatBuff(int index) {
        return overallStatBuff.get(index);
    }

    public Boolean isInventoryFull(int size) {
        if ((inventorySize - size) < 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void overallStatBuff() {
        Integer totalIndex0 = 0;
        Integer totalIndex1 = 0;
        Integer totalIndex2 = 0;
        Integer totalIndex3 = 0;
        Integer totalIndex4 = 0;
        int j = 0;
        int i = 0;
        if (inventoryItems.size() > 0) {
            try {
                for(j = 0; j < inventoryItems.get(0).getSize(); j++) {
                    for(i = 0; i < inventoryItems.size(); i++) {
                        if (j == 0) {
                            totalIndex0 += inventoryItems.get(i).getStatBuff(j);
                        }
                        else if (j == 1) {
                            totalIndex1 += inventoryItems.get(i).getStatBuff(j);
                        }
                        else if (j == 2) {
                            totalIndex2 += inventoryItems.get(i).getStatBuff(j); 
                        }
                        else if (j == 3) {
                            totalIndex3 += inventoryItems.get(i).getStatBuff(j); 
                        }
                        else if (j == 4) {
                            totalIndex4 += inventoryItems.get(i).getStatBuff(j); 
                        }
                        else {
                            throw new IndexOutOfBoundsException("The item" + inventoryItems.get(i).getName() + "has too many buffs.");
                        }

                    }
                    
                }
            }
            catch(Exception e) {
                throw new IndexOutOfBoundsException("The item " + inventoryItems.get(i).getName() +" does not have enough buffs. Please add more buffs.");
            }
        }

        overallStatBuff.add(totalIndex0);
        overallStatBuff.add(totalIndex1);
        overallStatBuff.add(totalIndex2);
        overallStatBuff.add(totalIndex3);
        overallStatBuff.add(totalIndex4);
    }

	public void removeItemsFromInventory(int index) {
        inventoryItems.remove(index);
    }
    
    public void replaceInventoryItem(int index,Items item) {
        if (!isInventoryFull((getTotalItemWeight() + item.returnWeight()))) {
            inventoryItems.remove(index);
            inventoryItems.add(index,item);
        }
        else {
            System.out.println("This item would make the bag too heavy for you to carry. The item was not taken");
        }
    }

    public Items getItemInInventory(int index) {
        return inventoryItems.get(index);
    }

}
