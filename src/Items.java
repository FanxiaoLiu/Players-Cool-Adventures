import java.util.ArrayList;

public class Items {
    private Integer weight;
    private ArrayList<Integer> statBuffs = new ArrayList<Integer>();
    private String name;
    private Integer idTag;
    /*
        Index       Buff
        _________________
        0           +dP
        1           +hP
        2           +stor
        3           +crit
        4           +heal
    */
    public Items(Integer idNumber,Integer localWeight, String localName, Integer index0, Integer index1, Integer index2, Integer index3, Integer index4) {
        weight = localWeight;
        name = localName;
        statBuffs.add(index0);
        statBuffs.add(index1);
        statBuffs.add(index2);
        statBuffs.add(index3);
        statBuffs.add(index4);
        idTag = idNumber;
    }

    public Integer returnWeight() {
        return weight;
    }

    public Integer getIDTag() {
        return idTag;
    }

    public Items(Integer localWeight, String localName) {
        weight = localWeight;
        name = localName;
    }

    public void addStatBuff(Integer statBuff) {
        statBuffs.add(statBuff);
    }

    public void addStatBuff(Integer statBuff, int index) {
        statBuffs.add(index,statBuff);
    }

    public void changeName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public Integer getStatBuff(int index) {
        return statBuffs.get(index);
    }

    public Integer getSize() {
        return statBuffs.size();
    }

    public void changeStatBuff(int index, Integer newStatBuff) {
        statBuffs.remove(index);
        statBuffs.add(index, newStatBuff);
    }

    public void setStatBuff0(int index) {
        statBuffs.remove(index);
        statBuffs.add(index, 0);
    }
}
