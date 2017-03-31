import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Encoder
{
    public static void main(String args[])
    {
        String filePath  = args[0];
        int    charCount = Integer.parseInt(args[1]);
        int    sum       = 0;
        char   currChar  = 'a';

        Map<Object, Integer> frequencyMap = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath)))
        {
            for (String line; (line = bufferedReader.readLine()) != null; )
            {
                Integer currentInt = Integer.parseInt(line);
                frequencyMap.put(currChar++, currentInt);
                sum += currentInt;
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Error while reading file: " + ioe.toString());
            System.exit(-1);
        }

        HuffmanTree tree = HuffmanCode.buildTree(frequencyMap);

        Map<Object, String> mapping = new HashMap<>(frequencyMap.size());
        HuffmanCode.fillMappingTable(tree, new StringBuffer(), mapping);

        for (Object o : mapping.keySet())
        {
            System.out.println(o + " : " + mapping.get(o));
        }

        String randomTextFilePath   = "testText";
        String decodedFileExtension = "dec1";

        WriteRandomlyToFile(charCount, randomTextFilePath, frequencyMap);
        EncodeFile(randomTextFilePath, mapping);
        DecodeFile(randomTextFilePath, mapping);
    }

    public static void WriteRandomlyToFile(int count,
                                           String filePath,
                                           Map<Object, Integer> frequencyMap)
    {
        RandomSelector randomSelector = new RandomSelector<>(frequencyMap);
        try (FileWriter fileWriter = new FileWriter(filePath))
        {
            for (int i = 0; i < count; i++)
            {
                fileWriter.write(randomSelector.next().toString().toCharArray());
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Exception while writing random text: " + ioe);
            System.exit(-1);
        }
    }

    public static void EncodeFile(String filePath, Map<Object, String> encoding)
    {
        String encodedFileExtension = ".enc1";
        //@formatter:off
        try (FileReader fileReader = new FileReader(filePath);
             FileWriter fileWriter = new FileWriter(filePath + encodedFileExtension))
        {//@formatter:on
            for (int c = fileReader.read(); c > -1; c = fileReader.read())
            {
                String encoded = encoding.get((char) c);
                if (encoded != null)
                {
                    fileWriter.write(encoded.toCharArray());
                }
                System.out.println(c + " encoded to " + encoded);
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Exception while writing random text: " + ioe);
        }
    }

    public static void DecodeFile(String filePath, Map<Object, String> decoding)
    {
        String decodedFileExtension = ".dec1";
        String encodedFileExtension = ".enc1";
        //@formatter:off
        try (FileReader fileReader = new FileReader(filePath + encodedFileExtension);
             FileWriter fileWriter = new FileWriter(filePath + decodedFileExtension))
        {//@formatter:on

            String buffer = "";

            for (int c = fileReader.read(); c > -1; c = fileReader.read())
            {
                buffer += c;

                String decoded;
                for(Entry<String,String> mapPair : decoding.entrySet()) {
                    Object key = mapPair.getKey();
                    String value = mapPair.getValue();
                    if (buffer == value)
                    {
                        decoded = key;
                        fileWriter.write(key);
                    }
                }

                System.out.println(buffer + " decoded to " + decoded);
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Exception while writing random text: " + ioe);
        }
    }
}