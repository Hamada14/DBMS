package JDBC.UserInteface;

public class TableShape {
  public static final char[] EXTENDED = { 0x00C7, 0x00FC, 0x00E9, 0x00E2,
      0x00E4, 0x00E0, 0x00E5, 0x00E7, 0x00EA, 0x00EB, 0x00E8, 0x00EF, 0x00EE,
      0x00EC, 0x00C4, 0x00C5, 0x00C9, 0x00E6, 0x00C6, 0x00F4, 0x00F6, 0x00F2,
      0x00FB, 0x00F9, 0x00FF, 0x00D6, 0x00DC, 0x00A2, 0x00A3, 0x00A5, 0x20A7,
      0x0192, 0x00E1, 0x00ED, 0x00F3, 0x00FA, 0x00F1, 0x00D1, 0x00AA, 0x00BA,
      0x00BF, 0x2310, 0x00AC, 0x00BD, 0x00BC, 0x00A1, 0x00AB, 0x00BB, 0x2591,
      0x2592, 0x2593, 0x2502, 0x2524, 0x2561, 0x2562, 0x2556, 0x2555, 0x2563,
      0x2551, 0x2557, 0x255D, 0x255C, 0x255B, 0x2510, 0x2514, 0x2534, 0x252C,
      0x251C, 0x2500, 0x253C, 0x255E, 0x255F, 0x255A, 0x2554, 0x2569, 0x2566,
      0x2560, 0x2550, 0x256C, 0x2567, 0x2568, 0x2564, 0x2565, 0x2559, 0x2558,
      0x2552, 0x2553, 0x256B, 0x256A, 0x2518, 0x250C, 0x2588, 0x2584, 0x258C,
      0x2590, 0x2580, 0x03B1, 0x00DF, 0x0393, 0x03C0, 0x03A3, 0x03C3, 0x00B5,
      0x03C4, 0x03A6, 0x0398, 0x03A9, 0x03B4, 0x221E, 0x03C6, 0x03B5, 0x2229,
      0x2261, 0x00B1, 0x2265, 0x2264, 0x2320, 0x2321, 0x00F7, 0x2248, 0x00B0,
      0x2219, 0x00B7, 0x221A, 0x207F, 0x00B2, 0x25A0, 0x00A0 };

  private static final int HEAVYVERTICAL = 185, HEAVYHORIZONATAL = 204;
  private static final int UPPERRIGHT = 186, UPPERLEFT = 200;
  private static final int LOWERRIGHT = 187, LOWERLEFT = 199;
  private static final int POINTDOWNHEAVY = 202, POINTUPHEAVY = 201;
  private static final int POINTLEFTHEAVY = 184, POINTRIGHTHEAVY = 203;
  private static final int PLUS = 205;

  private static TableShape tableShapeSingleton;
  private StringBuilder[][] table;
  private int[] maxWidth;

  private TableShape() {
  }

  public static TableShape getInstance() {
    if (tableShapeSingleton == null) {
      tableShapeSingleton = new TableShape();
    }
    return tableShapeSingleton;
  }

  public static final char getAscii(int code) {
    if (code >= 0x80 && code <= 0xFF) {
      return EXTENDED[code - 0x7F];
    }
    return (char) code;
  }

  public static final void printChar(int code) {
    System.out.printf("%c", getAscii(code));
  }
 
  
  public void setTable(Table result) {
    int length = result.getLength();
    int width = 0;
    if (result.getLength() > 0) {
      width = result.getWidth();
    }
    table = new StringBuilder[length][width];
    maxWidth = new int[width];
    for (int i = 0; i < length; i++) {
      for (int j = 0; j < width; j++) {
    	String word = result.getCell(i, j);
    	if(word == null) {
    		word = "null";
    	}
        table[i][j] = new StringBuilder(word);
        maxWidth[j] = Math.max(maxWidth[j], table[i][j].length());
      }
    }
    updateTable();
  }

  private void updateTable() {
    for (int i = 0; i < table.length; i++) {
      for (int j = 0; j < table[i].length; j++) {
        while (maxWidth[j] > table[i][j].length()) {
          table[i][j].append(" ");
        }
      }
    }
  }

  public void print() {
    int columns = maxWidth.length;
    int tableLen = table.length;
    if (tableLen > 0) {
      printBody(columns, tableLen);
    }
  }

  private void printBody(int columns, int tableLen) {
    printUpperBoundBar(columns);
    for (int currentRow = 0; currentRow < tableLen; currentRow++) {
      printRowContent(currentRow, columns);
      if (currentRow != tableLen - 1) {
        printBodyHorzontalBars(columns);
      }
    }
    printLowerBoundBar(columns);
  }

  private void printUpperBoundBar(int columns) {
    printChar(UPPERLEFT);
    for (int i = 0; i < columns; i++) {
      for (int j = 0; j < maxWidth[i]; j++) {
        printChar(HEAVYHORIZONATAL);
      }
      if (i != columns - 1) {
        printChar(POINTDOWNHEAVY);
      }
    }
    printChar(UPPERRIGHT);
    System.out.println();
  }

  private void printRowContent(int currentRow, int columns) {
    printChar(HEAVYVERTICAL);
    for (int i = 0; i < columns; i++) {
      System.out.print(table[currentRow][i].toString());
      printChar(HEAVYVERTICAL);
    }
    System.out.println();
  }

  private void printBodyHorzontalBars(int columns) {
    printChar(POINTRIGHTHEAVY);
    for (int i = 0; i < columns; i++) {
      for (int j = 0; j < maxWidth[i]; j++) {
        printChar(HEAVYHORIZONATAL);
      }
      if (i != columns - 1) {
        printChar(PLUS);
      }
    }
    printChar(POINTLEFTHEAVY);
    System.out.println();
  }

  private void printLowerBoundBar(int columns) {
    printChar(LOWERLEFT);
    for (int i = 0; i < columns; i++) {
      for (int j = 0; j < maxWidth[i]; j++) {
        printChar(HEAVYHORIZONATAL);
      }
      if (i != columns - 1) {
        printChar(POINTUPHEAVY);
      }
    }
    printChar(LOWERRIGHT);
    System.out.println();
  }
}