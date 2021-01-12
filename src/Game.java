import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author: Michael Kolling Version: 1.1 Date: March 2000
 * 
 * This class is the main class of the "Zork" application. Zork is a very
 * simple, text based adventure game. Users can walk around some scenery. That's
 * all. It should really be extended to make it more interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * routine.
 * 
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates the commands that
 * the parser returns.
 */
public class Game {
  private Parser parser;
  private Room currentRoom;
  private Character Player = new Character("Shtayo",200,10000,0,30,30);
  private ArrayList<Character> monsterList = new ArrayList<Character>();
  private ArrayList<Items> monsterItemsList = new ArrayList<Items>();
  private ArrayList<Items> roomItemsList = new ArrayList<Items>();
  private ArrayList<Integer> itemAssignMonster = new ArrayList<Integer>();
  private ArrayList<Integer> itemAssignRoom = new ArrayList<Integer>();
  private ArrayList<Integer> monsterAssignRoom = new ArrayList<Integer>();
  private ArrayList<String> keyArray = new ArrayList<String>();

  // This is a MASTER object that contains all of the rooms and is easily
  // accessible.
  // The key will be the name of the room -> no spaces (Use all caps and
  // underscore -> Great Room would have a key of GREAT_ROOM
  // In a hashmap keys are case sensitive.
  // masterRoomMap.get("GREAT_ROOM") will return the Room Object that is the Great
  // Room (assuming you have one).
  private HashMap<String, Room> masterRoomMap;

  private void initRooms(String fileName) throws Exception {
    masterRoomMap = new HashMap<String, Room>();
    Scanner roomScanner;
    try {
      HashMap<String, HashMap<String, String>> exits = new HashMap<String, HashMap<String, String>>();
      roomScanner = new Scanner(new File(fileName));
      int c = 0;
      int v = 0;
      int roomNum = 0;
      while (roomScanner.hasNext()) {
        Room room = new Room();
        // Read the Name
        String roomName = roomScanner.nextLine();
        room.setRoomName(roomName.split(":")[1].trim());
        // Read the Description
        String roomDescription = roomScanner.nextLine();
        room.setDescription(roomDescription.split(":")[1].replaceAll("<br>", "\n").trim());
        // Read the Exits
        String roomExits = roomScanner.nextLine();
        // Puts the appropriate monsters inside the room.
        
        while (monsterAssignRoom.get(c) == roomNum) {
          room.addMonsterToRoom(monsterList.get(c));
          if (c < monsterAssignRoom.size()-1) {
            c++;
          }
          else {
            break;
          }
        }
        while (itemAssignRoom.get(v) == roomNum) {
          room.addItemsToInventory(roomItemsList.get(v));
          if (v < itemAssignRoom.size()-1) {
            v++;
          }
          else {
            break;
          }
        }
        int i = 0;
        // An array of strings in the format E-RoomName
        String[] rooms = roomExits.split(":")[1].split(",");
        HashMap<String, String> temp = new HashMap<String, String>();
        for (String s : rooms) {
          temp.put(s.split("-")[0].trim(), s.split("-")[1]);
        }

        exits.put(roomName.substring(10).trim().toUpperCase().replaceAll(" ", "_"), temp);

        // This puts the room we created (Without the exits in the masterMap)
        masterRoomMap.put(roomName.toUpperCase().substring(10).trim().replaceAll(" ", "_"), room);
        keyArray.add(roomName.toUpperCase().substring(10).trim().replaceAll(" ", "_"));

        // Now we better set the exits.

        roomNum++;
      }

      for (String key : masterRoomMap.keySet()) {
        Room roomTemp = masterRoomMap.get(key);
        HashMap<String, String> tempExits = exits.get(key);
        for (String s : tempExits.keySet()) {
          // s = direction
          // value is the room.

          String roomName2 = tempExits.get(s.trim());
          Room exitRoom = masterRoomMap.get(roomName2.toUpperCase().replaceAll(" ", "_"));
          roomTemp.setExit(s.trim().charAt(0), exitRoom);
        }
      }

      roomScanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Create the game and initialise its internal map.
   */
  public Game() {
    try {
      Player.refreshStats();
      Player.setBeginningHP();
      giveItemsToPlayer();
      createMonsters("Players-Cool-Adventures/data/monsters.dat");
      initRooms("Players-Cool-Adventures/data/rooms.dat");
      currentRoom = masterRoomMap.get("ROOM_1");
      putWinTreasure();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    parser = new Parser();
  }

  private void putWinTreasure() {
    Items winTreasure1 = new Items(1000, 0, "Fancy Treasure", 0,0,0,0,0);
    Items winTreasure2 = new Items(1000, 0, "Luxurious Treasure", 0,0,0,0,0);
    int i = 0;
    while (i<2) {
      int random = (int)(Math.random() * (keyArray.size()-1));
      for (int j = 0; j<keyArray.size()-1; j++) {
        if (j == random && i == 0) {
          masterRoomMap.get(keyArray.get(j)).addItemsToInventory(winTreasure1);;
        }
        else if (j == random && i == 1) {
          masterRoomMap.get(keyArray.get(j)).addItemsToInventory(winTreasure2);;
        }
      }
      i++;
    }
  }

  public void giveItemsToPlayer() {
    Items playerItem1 = new Items(11, 230, "Spear of Honor", 160, 0, 10, 30, 40);
    Player.addItemsToInventory(playerItem1);
  }

  public void createMonsters(String fileName) {
    Scanner monsterScanner;
    try {
      monsterScanner = new Scanner(new File(fileName));
      while (monsterScanner.hasNext()) {
        // Read the Name
        String monsterName = monsterScanner.nextLine();
        Integer assignedRoom = Integer.parseInt(monsterScanner.nextLine());
        Integer idValue = Integer.parseInt(monsterScanner.nextLine());
        Integer stat0 = Integer.parseInt(monsterScanner.nextLine());
        Integer stat1 = Integer.parseInt(monsterScanner.nextLine());
        Integer stat2 = Integer.parseInt(monsterScanner.nextLine());
        Integer stat3 = Integer.parseInt(monsterScanner.nextLine());
        Integer stat4 = Integer.parseInt(monsterScanner.nextLine());

        Character monster = new Character(monsterName,idValue,stat0,stat1,stat2,stat3,stat4);
        monsterList.add(monster);
        monsterAssignRoom.add(assignedRoom);
      }
      initializeItems("Players-Cool-Adventures/data/items.dat");
      int i = 0;
      int j = 0;
      while (j<monsterItemsList.size()) {
        monsterList.get(i).addItemsToInventory(monsterItemsList.get(j));
        j++;
        if (j<itemAssignMonster.size()-1) {
          if (itemAssignMonster.get(j+1) != itemAssignMonster.get(j)) {
            i++;
          }
        }
      }
      for (int o = 0; o<monsterList.size();o++) {
        monsterList.get(o).refreshStats();
        monsterList.get(o).setBeginningHP();
      }
    }
    catch (FileNotFoundException e){
      e.printStackTrace();
    }
  }

  public void initializeItems(String fileName) {
    Scanner itemScanner;
    try {
      itemScanner = new Scanner(new File(fileName));
      while (itemScanner.hasNext()) {
        // Read the Name
        String itemName = itemScanner.nextLine();
        Integer idNumber = Integer.parseInt(itemScanner.nextLine());
        Integer weight = Integer.parseInt(itemScanner.nextLine());
        String belongsTo = itemScanner.nextLine();
        Integer which = Integer.parseInt(itemScanner.nextLine());
        Integer stat0 = Integer.parseInt(itemScanner.nextLine());
        Integer stat1 = Integer.parseInt(itemScanner.nextLine());
        Integer stat2 = Integer.parseInt(itemScanner.nextLine());
        Integer stat3 = Integer.parseInt(itemScanner.nextLine());
        Integer stat4 = Integer.parseInt(itemScanner.nextLine());

        Items item = new Items(idNumber, weight, itemName, stat0, stat1, stat2, stat3, stat4);
        if (belongsTo.equals("monster")) {
          monsterItemsList.add(item);
          itemAssignMonster.add(which);
        }
        else {
          roomItemsList.add(item);
          itemAssignRoom.add(which);
        }
      }
    }
    catch (FileNotFoundException e){
      e.printStackTrace();
    }
  }

  public String getKeyFromValue(Room value) {
    for(Entry<String, Room> entry: masterRoomMap.entrySet()) {
      if(entry.getValue() == value) {
        System.out.println("The key for value " + value + " is " + entry.getKey());
        return entry.getKey();
      }
    }
    return null;
  } 

  public Character returnMonsters(int index) {
    for (int i = 0; i<monsterList.size(); i++) {
      if (index == i) {
        return monsterList.get(i);
      }
    }
    throw new IndexOutOfBoundsException("Not that many monsters.");
  }

  /**
   * Main play routine. Loops until end of play.
   */
  public void play() {
    printWelcome();
    // Enter the main command loop. Here we repeatedly read commands and
    // execute them until the game is over.

    boolean finished = false;
    while (!finished) {
      doesWin();
      Command command = parser.getCommand();
      finished = processCommand(command);
    }
    System.out.println("Thank you for playing.  Good bye.");
  }

  private void doesWin() {
    // Determine if player can win right now
  }

  /**
   * Print out the opening message for the player.
   */
  private void printWelcome() {
    System.out.println();
    System.out.println("Welcome to Zork!");
    System.out.println("Zork is a new, incredibly boring adventure game.");
    System.out.println("Type 'help' if you need help.");
    System.out.println();
    System.out.println(currentRoom.longDescription());
  }

  /**
   * Given a command, process (that is: execute) the command. If this command ends
   * the game, true is returned, otherwise false is returned.
   */
  private boolean processCommand(Command command) {
    Scanner in = new Scanner(System.in);
    if (command.isUnknown()) {
      System.out.println("I don't know what you mean...");
      return false;
    }
    String commandWord = command.getCommandWord();
    if (commandWord.equals("help"))
      printHelp();
    else if (commandWord.equals("go"))
      goRoom(command);
    else if (commandWord.equals("quit")) {
      if (command.hasSecondWord())
        System.out.println("Quit what?");
      else
        return true; // signal that we want to quit
    } else if (commandWord.equals("eat")) {
      System.out.println("Do you really think you should be eating at a time like this?");
    }
    else if (commandWord.equals("viewinventory")){
      if (Player.getNumberofItemsinInventory() > 0) {  
        int count = -1;
        while (count < 0) {
          System.out.println("These are all the items in your inventory. Enter the index number before the item to learn more about it.");
          for (int i = 0; i<Player.getNumberofItemsinInventory();i++) {
            System.out.println(i + " " + Player.getItemInInventory(i).getName());
          }
          int indexItem = 0;
          String nextLine = in.nextLine();

          try {
            indexItem = Integer.parseInt(nextLine);
            if (indexItem > (Player.getNumberofItemsinInventory()-1)) {
              System.out.println("Please enter an index within the range.");
            }
            else {
              System.out.println(Player.getItemInInventory(indexItem).getName() + "'s stats in this order: Damage Points, Hit Points, Storage, Critical Rate, Heal, Current HP");
              for (int i = 0; i<5; i++) {
                System.out.println(Player.getItemInInventory(indexItem).getStatBuff(i));
              }
              System.out.println("Do you wish to drop this item?");
                String doesDrop;
                int count2 = -1;
                while (count2 < 0) {
                  doesDrop = in.nextLine();
                  if (doesDrop.equals("yes")) {
                    currentRoom.addItemsToInventory(Player.getItemInInventory(indexItem));
                    Player.removeItemsFromInventory(indexItem);
                    Player.refreshStats();
                    count2 = 1;
                  }
                  else if (doesDrop.equals("no")) {
                    System.out.println("You did not drop the item.");
                    count2 = 1;
                  }
                  else {
                    System.out.println("Please enter either yes or no.");
                  }
              }
            }
          }
          catch (NumberFormatException e) {
            indexItem = 0;
            System.out.println("Please enter an index within the range or the word pickup.");
          }
          int count1 = -1;
          String doExitMenu = "";
          while (count1 < 0) {
            System.out.println("Do you wish to leave this menu? yes/no");
            doExitMenu = in.nextLine();
            if (doExitMenu.equals("yes")) {
              count = 1;
              count1 = 1;
            }
            else if (doExitMenu.equals("no")) {
              count = -1;
              count1 = 1;
            }
            else {
              System.out.println("Please enter either yes or no.");
            }
          }
        }
      }
      else {
        System.out.println("You sigh as you stare at your empty bag. You take some dirt from the ground and stuff it in there. It falls out somehow.");
      }
    }
    else if (commandWord.equals("fight")) {
      if (currentRoom.monsterPresent()) {
        System.out.println(Player.getName() + " will be fighting " + currentRoom.getMonsterFromRoom(0).getName() + ".");
        System.out.println("Monster Stats in this order: Damage Points, Hit Points, Storage, Critical Rate, Heal, Current HP");
        for (int i = 0; i<5;i++) {
          System.out.println(currentRoom.getMonsterFromRoom(0).displayStats(i));
        }
        System.out.println(currentRoom.getMonsterFromRoom(0).getCurrentHP());
        System.out.println(Player.getName() + "'s stats in this order: Damage Points, Hit Points, Storage, Critical Rate, Heal, Current HP");
        for (int i = 0; i<5;i++) {
          System.out.println(Player.displayStats(i));
        }
        System.out.println(Player.getCurrentHP());
        System.out.println("Do you still want to fight? yes/no");
        int i = -1;
        Boolean fightChoiceBool = true;
        while (i < 0) {
          String fightChoice = in.nextLine();
          if (fightChoice.equals("yes")) {
            fightChoiceBool = true;
            i = 1;
          }
          else if (fightChoice.equals("no")){
            fightChoiceBool = false;
            i = 1;
          }
          else {
            System.out.print("Please enter yes or no");
          }
        }
        if (fightChoiceBool) {
          fight(currentRoom.getMonsterFromRoom(0),Player);
        }
        else {
          System.out.println("You shamefully ran away.");
        }
      }
      else {
        System.out.println("You punch a wall out of frustration of having nothing to fight. You lose 10 hP");
        Player.hurtCharacter(10);
      }
    }
    else if (commandWord.equals("heal")) {
      if (Player.returnHealChances() > 0) {
        Player.healCharacter();
      }
      else {
        System.out.println("You have no more heals left. I did tell you to use them wisely. Tsk tsk. You can either fight something and defeat it to get an extra five heals, or give up, your choice.");
      }
    }
    else if (commandWord.equals("pickup")) {
      if (currentRoom.itemPresent()) {
        int count = -1;
        while(count < 0 && currentRoom.itemPresent()) {
          System.out.println("You find stuff in the room, all of which are below. Enter the index number before the item to learn more about it. Type pickup again to pickup an item.");
          for (int i = 0; i<currentRoom.getNumberofItemsinInventory();i++) {
            System.out.println(i + " " + currentRoom.getItemInInventory(i).getName());
          }
          int indexItem = 0;
          String nextLine = in.nextLine();
          if (nextLine.equals("pickup")) {
            System.out.println("Please enter the index of the item you want to pick up.");
            try {
              int pickupIndex = Integer.parseInt(in.nextLine());
              if (pickupIndex > (currentRoom.getNumberofItemsinInventory()-1)) {
                System.out.println("Please enter an index within the range or the word pickup.");
              }
              else {
                Player.addItemsToInventory(currentRoom.getItemInInventory(pickupIndex));
                currentRoom.removeItemsFromInventory(pickupIndex);
                System.out.println("You picked up a " + Player.getItemInInventory(Player.getNumberofItemsinInventory()-1).getName());
                Player.refreshStats();
                count = 1;
              }
            }
            catch (NumberFormatException e){
              System.out.println("Please enter a number within the range of the index.");
            }
          }
          else {
            try {
              indexItem = Integer.parseInt(nextLine);
              if (indexItem > (currentRoom.getNumberofItemsinInventory()-1)) {
                System.out.println("Please enter an index within the range or the word pickup.");
              }
              else {
                System.out.println(currentRoom.getItemInInventory(indexItem).getName() + "'s stats in this order: Damage Points, Hit Points, Storage, Critical Rate, Heal, Current HP");
                for (int i = 0; i<5; i++) {
                  System.out.println(currentRoom.getItemInInventory(indexItem).getStatBuff(i));
                }
              }
            }
            catch (NumberFormatException e) {
              indexItem = 0;
              System.out.println("Please enter an index within the range or the word pickup.");
            }
            int count1 = -1;
            String doExitMenu = "";
            while (count1 < 0) {
              System.out.println("Do you wish to leave this menu? yes/no");
              doExitMenu = in.nextLine();
              if (doExitMenu.equals("yes")) {
                count = 1;
                count1 = 1;
              }
              else if (doExitMenu.equals("no")) {
                count = -1;
                count1 = 1;
              }
              else {
                System.out.println("Please enter either yes or no.");
              }
            }
          }
        }
      Player.refreshStats();
      }
      else {
        System.out.println("You sigh in exasperation as you realize the room is dark and devoid of anything of value, that is except for a mushroom.");
        System.out.println("You pick up the mushroom and eat it. You get poisoned. You lose 10 HP.");
        Player.hurtCharacter(10);
      }
    }
    else if (commandWord.equals("scream")) {
      System.out.println("You scream and attract the attention of nearby monsters.");
      if (currentRoom.monsterPresent()) {
        System.out.println(Player.getName() + " will be fighting " + currentRoom.getMonsterFromRoom(0).getName() + ".");
        System.out.println("Monster Stats in this order: Damage Points, Hit Points, Storage, Critical Rate, Heal, Current HP");
        for (int i = 0; i<5;i++) {
          System.out.println(currentRoom.getMonsterFromRoom(0).displayStats(i));
        }
        System.out.println(currentRoom.getMonsterFromRoom(0).getCurrentHP());
        System.out.println(Player.getName() + "'s stats in this order: Damage Points, Hit Points, Storage, Critical Rate, Heal, Current HP");
        for (int i = 0; i<5;i++) {
          System.out.println(Player.displayStats(i));
        }
        System.out.println(Player.getCurrentHP());
        System.out.println("Do you still want to fight? yes/no");
        int i = -1;
        Boolean fightChoiceBool = true;
        while (i < 0) {
          String fightChoice = in.nextLine();
          if (fightChoice.equals("yes")) {
            fightChoiceBool = true;
            i = 1;
          }
          else if (fightChoice.equals("no")){
            fightChoiceBool = false;
            i = 1;
          }
          else {
            System.out.print("Please enter yes or no");
          }
        }
        if (fightChoiceBool) {
          fight(currentRoom.getMonsterFromRoom(0),Player);
        }
        else {
          System.out.println("You shamefully ran away.");
        }
      }
      else {
        System.out.println("You realize the monster was just a spider. You squish it. You feel better about yourself.");
      }
    }
    else if (commandWord.equals("viewstats")) {
      Player.refreshStats();
      System.out.println(Player.getName() + "'s stats in this order: Damage Points, Hit Points, Storage, Critical Rate, Heal, Current HP, Heal Chances");
      for (int i = 0; i<5;i++) {
        System.out.println(Player.displayStats(i));
      }
      System.out.println(Player.getCurrentHP());
      System.out.println(Player.returnHealChances());
    }
    else if (commandWord.equals("viewroom")) {
      if (currentRoom.monsterPresent() || currentRoom.itemPresent()) {
        Boolean exitMenu = false;
        while (!exitMenu) {
          if (currentRoom.monsterPresent()) {
            System.out.println("The room has " + currentRoom.monsterNumbers() + " monster(s).");
            for (int i = 0; i < currentRoom.monsterNumbers(); i++) {
              System.out.println(i + " " + currentRoom.getMonsterFromRoom(i).getName());
            }
            String input;
            Boolean exitSubMenu = false;
            while (!exitSubMenu) {
              System.out.println("Enter the index to learn more about the monster. Or exit to exit the sub menu.");
              input= in.nextLine();
              if (input.equals("exit")) {
                exitSubMenu = true;
              }
              else {
                int index = 0;
                try {
                  index = Integer.parseInt(input);
                  if (index > (currentRoom.monsterNumbers()-1)) {
                    System.out.println("Please enter an index within the range or the word exit to exit the sub menu.");
                  }
                  else {
                    System.out.println(currentRoom.getMonsterFromRoom(index).getName() + "'s stats in this order: Damage Points, Hit Points, Storage, Critical Rate, Heal, Current HP");
                    for (int i = 0; i<5; i++) {
                      System.out.println(currentRoom.getMonsterFromRoom(index).displayStats(i));
                    }
                    System.out.println(currentRoom.getMonsterFromRoom(index).getCurrentHP());
                  }
                } catch (NumberFormatException e) {
                  //TODO: handle exception
                  index = 0;
                  System.out.println("Please enter an index within the range or the word exit to exit the sub menu.");
                }
              }
            }
          }
          if (currentRoom.itemPresent() && !exitMenu) {
            int count3 = -1;
            while(count3 < 0 && currentRoom.itemPresent()) {
              System.out.println("You find stuff in the room, all of which are below. Enter the index number before the item to learn more about it.");
              for (int i = 0; i<currentRoom.getNumberofItemsinInventory();i++) {
                System.out.println(i + " " + currentRoom.getItemInInventory(i).getName());
              }
              int indexItem = 0;
              String nextLine = in.nextLine();
              try {
                indexItem = Integer.parseInt(nextLine);
                if (indexItem > (currentRoom.getNumberofItemsinInventory()-1)) {
                  System.out.println("Please enter an index within the range.");
                }
                else {
                  System.out.println(currentRoom.getItemInInventory(indexItem).getName() + "'s stats in this order: Damage Points, Hit Points, Storage, Critical Rate, Heal, Current HP");
                  for (int i = 0; i<5; i++) {
                    System.out.println(currentRoom.getItemInInventory(indexItem).getStatBuff(i));
                  }
                }
              }
              catch (NumberFormatException e) {
                indexItem = 0;
                System.out.println("Please enter an index within the range.");
              }
              int count1 = -1;
              String doExitMenu = "";
              int count4 = -1;
              while (count4 < 0) {
                System.out.println("Do you wish to leave this room menu?");
                doExitMenu = in.nextLine();
                if (doExitMenu.equals("yes")) {
                  count4 = 1;
                  exitMenu = true;
                  count1 = 1;
                  count3 = 1;
                }
                else if (doExitMenu.equals("no")) {
                  count4 = 1;
                  exitMenu = false;
                }
                else {
                  System.out.println("Please enter either yes or no.");
                }
              }
              while (count1 < 0) {
                System.out.println("Do you wish to leave this sub menu? yes/no");
                doExitMenu = in.nextLine();
                if (doExitMenu.equals("yes")) {
                  count3 = 1;
                  count1 = 1;
                }
                else if (doExitMenu.equals("no")) {
                  count1 = 1;
                }
                else {
                  System.out.println("Please enter either yes or no.");
                }
              }
            }
          Player.refreshStats();
          }
          else {
            System.out.println("There are no items in the room.");
            String doExitMenu = "";
              int count4 = -1;
              while (count4 < 0) {
                System.out.println("Do you wish to leave this room menu?");
                doExitMenu = in.nextLine();
                if (doExitMenu.equals("yes")) {
                  count4 = 1;
                  exitMenu = true;
                }
                else if (doExitMenu.equals("no")) {
                  count4 = 1;
                  exitMenu = false;
                }
                else {
                  System.out.println("Please enter either yes or no.");
                }
              }
          }

        }
      }
    }
    return false;
  }

  // implementations of user commands:
  /**
   * Print out some help information. Here we print some stupid, cryptic message
   * and a list of the command words.
   */
  private void printHelp() {
    System.out.println("You are lost. You are alone. You wander");
    System.out.println("around at Monash Uni, Peninsula Campus.");
    System.out.println();
    System.out.println("Your command words are:");
    parser.showCommands();
  }

  /**
   * Try to go to one direction. If there is an exit, enter the new room,
   * otherwise print an error message.
   */
  private void goRoom(Command command) {
    if (!command.hasSecondWord()) {
      // if there is no second word, we don't know where to go...
      System.out.println("Go where?");
      return;
    }
    String direction = command.getSecondWord();
    // Try to leave current room.
    Room nextRoom = currentRoom.nextRoom(direction);
    if (nextRoom == null)
      System.out.println("There is no door!");
    else {
      if (!currentRoom.monsterPresent()) {
        currentRoom = nextRoom;
        System.out.println(currentRoom.longDescription());
      }
      else if (currentRoom.getMonsterFromRoom(0).getCurrentHP() <= (currentRoom.getMonsterFromRoom(0).displayStats(1)/2)) {
        System.out.println("You sneak past the exhausted monster and reach the next room.");
        currentRoom = nextRoom;
        System.out.println(currentRoom.longDescription());
      }
      else {
        System.out.println("The Monster catches you trying to escape and drags you back into the room and punches you.");
        System.out.println("You lose 100 HP.");
        Player.hurtCharacter(100);
        System.out.println("Either beat the monster to less than 50% HP or kill it. Or you can just die.");
      }
    }
  }

  private void fight(Character monster, Character player) {
    if (!monster.getisDead() && !player.getisDead()) {
      /*
      Integer playerDamage = player.displayStats(0);
      Integer monsterDamage = monster.displayStats(0);
      Integer playerHP = player.displayStats(1);
      Integer monsterHP = monster.displayStats(1);
      Integer playerCrit = player.displayStats(3);
      Integer monsterCrit = monster.displayStats(3);
      Integer playerHeal = player.displayStats(4);
      Integer monsterHeal = monster.displayStats(4);
      Boolean isQuit = false;
      Integer playerCurrentHP = player.getCurrentHP();
      Integer monsterCurrentHP = monster.getCurrentHP();
      */

      if (!player.getisDead() && !monster.getisDead()) {
        monster.damageTaken(player);
        if (!monster.getisDead()) {
          player.damageTaken(monster);
        }
      }
      else {
        System.out.println("You should be dead, don't know why you aren't, go die.");
      }
      if (monster.getisDead()) {
        System.out.println(monster.getName() + " is now dead.");
        currentRoom.removeMonsterFromRoom(0);
        for (int i = 0; i<monster.getNumberofItemsinInventory(); i++) {
          currentRoom.addItemsToInventory(monster.getItemInInventory(0));
          monster.removeItemsFromInventory(0);
        }
        Player.addHealChance(5);
        System.out.println("You have gained 5 extra heals");
      }
    }
  }

  /*
  public static void main(String[] args) {
    Items swordofJustice = new Items(21 ,1000, "Sword of Justice", 2, 4, 7, 3, 3);
    Items bowofJustice = new Items(15 ,50, "Bow of Justice", 2, 4, 7, 3, 3);
    Items spearofJustice = new Items(31, 75, "Spear of Justice", 2, 4, 7, 3, 3);

    Character Player = new Character("Shtayo",200,10000,0,30,30);
    Character Monster = new Character("Lizard", 100, 1000, 20, 90, 30);

    Player.addItemsToInventory(swordofJustice);
    Player.addItemsToInventory(bowofJustice);
    Player.addItemsToInventory(spearofJustice);

    Player.refreshStats();
    Monster.refreshStats();
    for (int i = 0; i<5; i++) {
      System.out.println(Player.displayStats(i));
    }
    Player.setBeginningHP();
    Monster.setBeginningHP();

    fight(Monster, Player);




  }
  */
}
