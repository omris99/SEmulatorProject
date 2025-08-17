package ui.menu;

import ui.menu.option.MenuOption;

import java.util.List;
import java.util.Scanner;

public abstract class AbstractMenu implements Menu{
    private final String header;
    private final String underLineChar;
    List<MenuOption> menuOptions;

    public AbstractMenu(String header, String underLineChar, List<MenuOption> menuOptions) {
        this.header = header;
        this.underLineChar = underLineChar;
        this.menuOptions = menuOptions;
    }

    @Override
    public MenuOption printAndReturnUserChoice(){
        boolean validInput = false;
        int choice = 0;
        int i = 1;

        System.out.println(getUnderLine(header));
        System.out.println(header);
        System.out.println(getUnderLine(header));
        for(MenuOption option : menuOptions){
            System.out.println(String.format("%d. %s", i++, option.getMenuDisplay()));
        }
        System.out.print(String.format("\nPlease Enter Your Choice (%d - %d): ", 1, menuOptions.size()));
        Scanner scanner = new Scanner(System.in);
        while(!validInput){
            if(scanner.hasNextInt()){
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= menuOptions.size()) {
                    validInput = true;
                }
                else {
                    System.out.printf("Invalid choice. Must be between 1 and %d.%n", menuOptions.size());
                }
            }
            else {
                System.out.println("Invalid input. Please enter a number only.");
                scanner.nextLine();
            }
        }

        return menuOptions.get(choice - 1);
    }


    private String getUnderLine(String text){
        StringBuilder underLineToReturn = new StringBuilder();
        underLineToReturn.append(String.valueOf(underLineChar).repeat(text.length() + 2));
        return underLineToReturn.toString();
    }
}
