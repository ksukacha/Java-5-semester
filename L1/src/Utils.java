import java.io.*;
import java.util.*;

public class Utils {

    public static Map<String, String> readDictionary(File file)throws IOException{
        Map<String,String> dictionary;
        try(BufferedReader br = new BufferedReader(
               new InputStreamReader(
                       new FileInputStream(file), "Cp1251"))) {
            dictionary = new HashMap<>();
            String line;
            String wordWithoutDashes;
            while ((line = br.readLine()) != null) {
                wordWithoutDashes = line.replaceAll("-", "");
                dictionary.put(wordWithoutDashes, line);
            }
        }
        return dictionary;
   }

   public static String readFromFile(File file) throws IOException{
       try(BufferedReader br = new BufferedReader(
               new InputStreamReader(
                       new FileInputStream(file), "Cp1251"))) {
           char[] buf = new char[1024];
           StringBuffer sb = new StringBuffer();
           int res;
           while ((res=br.read(buf))>=0) {
               sb.append(String.valueOf(buf,0,res));
           }
           return sb.toString();
       }
   }

   public static void saveFile(File file, String text) throws IOException {
       try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "Cp1251"))) {
           bw.write(text);
           bw.flush();
       }
   }

   public static boolean wordCorrectDivided(String word, Map<String, String> dictionary){
       String wordWithoutDashes = word.replaceAll("-", "");
       if(dictionary.containsKey(wordWithoutDashes)){
           String value = dictionary.get(wordWithoutDashes);
           String[] correctParts = value.split("-");
           String[] curParts = word.split("-");
           for(int i = 0; i<correctParts.length; ++i){
               if(correctParts[i].length()>curParts[0].length()){
                   return false;
               }
               if(correctParts[i].length()==curParts[0].length()){
                   return true;
               }
               String twoParts = correctParts[i].concat(correctParts[i+1]);
               correctParts[i+1] = twoParts;
           }
       }
       return true;//если слова нет в словаре - полагаем, что корректно
   }

   public static ChangeMessage getWordChangeMessage(String word, Map<String, String> dictionary) {
       String wordWithoutDashes = word.replaceAll("-", "");
       String correctWordDivision = dictionary.get(wordWithoutDashes);
       boolean hasDash = false;
       int index=0;
       if(correctWordDivision.matches("[а-яА-Я]+(-[а-яА-Я]+)+")) {
           hasDash = true;
           int dashPosition = word.indexOf("-");
           int counter = 0;
           for (index = 0; index < correctWordDivision.length(); ++index) {
               if (counter < dashPosition) {
                   if (correctWordDivision.charAt(index) != '-') {
                       counter++;
                   }
               } else break;
           }
           int k = 1;
           while (correctWordDivision.charAt(index) != '-') {//будт-о ломается String outOfBounds: index 6 size 6; цель без переносов ломается
               index -= k;
               if(index >=0 && correctWordDivision.charAt(index) == '-'){
                   break;
               }
               index += 2 * k;
               if(index < correctWordDivision.length() && correctWordDivision.charAt(index) == '-'){
                   break;
               }
               index -= k;
               k++;
           }
       }
       StringBuffer sb = new StringBuffer();
       StringBuffer correctAnswer = new StringBuffer();
       sb.append("Заменить слово ");
       sb.append(word);
       sb.append(" на ");
       if(hasDash) {
           correctAnswer.append(correctWordDivision.substring(0, index).replaceAll("-", ""));
           correctAnswer.append("-");
           correctAnswer.append(correctWordDivision.substring(index + 1).replaceAll("-", ""));
           sb.append(correctAnswer);
       }else {
           sb.append(correctWordDivision);
           correctAnswer.append(correctWordDivision);
       }
       sb.append("?");
       return new ChangeMessage(sb.toString(), correctAnswer.toString(), hasDash);
   }


    public static class ChangeMessage{
        private String message;
        private String correctWord;
        private boolean hasDash;

        public ChangeMessage(String message, String correctWord, boolean hasDash) {
            this.message = message;
            this.correctWord = correctWord;
            this.hasDash = hasDash;
        }

        public String getMessage() {
            return message;
        }

        public String getCorrectWord() {
            return correctWord;
        }

        public boolean hasDash() {
            return hasDash;
        }
    }
}
