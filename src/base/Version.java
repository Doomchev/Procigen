package base;

import java.io.IOException;

public class Version extends Serialization {
  public static Version read() throws IOException {
    int size = readInt();
    Version version = new Version();
    version.numbers = new int[size];
    for(int index = 0; index < size; index++) version.numbers[index] = readInt();
    return version;
  }
  
  public int[] numbers;

  public Version(int... numbers) {
    this.numbers = numbers;
  }
  
  public boolean lesssThan(Version version) {
    int[] numbers2 = version.numbers;
    int size = numbers2.length;
    for(int index = 0; index < numbers.length; index++) {
      if(index >= size) return false;
      int difference = numbers[index] - numbers2[index];
      if(difference > 0) return false;
      if(difference < 0) return true;
    }
    return false;
  }

  void write() throws IOException {
    writeInt(numbers.length);
    for(int index = 0; index < numbers.length; index++) writeInt(numbers[index]);
  }
}
