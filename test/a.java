import java.util.Scanner;
class  Pattern5 {
 public static void main(String[] args) {
  Scanner in= new Scanner(System.in);
  System.out.println("Enter the number of rows to be printed");
  int rows = in.nextInt();
  for(int y = 0; y <= rows * 2; y++ ) {
    for(int x = 0; x <= rows * 2; x++) {
      System.out.print(Math.abs(rows - x) + Math.abs(rows - y) == rows ? "**" : "  ");
    }
    System.out.println();
  }
 }  
}