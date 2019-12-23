import javafx.util.Pair;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        StringBuilder text = new StringBuilder();
        StringBuilder answer = new StringBuilder();
        Scanner in = new Scanner(new File("sample1.pas"));
        while (in.hasNext()) {
            String s = in.nextLine();
            text.append(s);
            text.append("\r\n");
            answer.append(s);
            answer.append("\r\n");
        }
        in.close();

        List<Pair<Integer, Integer>> regionsToDelete = new ArrayList<>();

        Pattern patForCode = Pattern.compile("(((\\'([\\S\\s]+?)((\\//)|[\\{]|[\\(][\\*])([\\S\\s]+?)\\'))" +
                "|(:=(\\'([^\\'\\//\\{\\}\\(\\*\\)])*\\'[\\s]*\\+[\\s]*)*(\\'([^\\'\\//\\{\\}\n])*\\'))" +
                "|( \\//([\\S\\s]+?)\n)" +
                "|(\\{([\\S\\s]+?)\\}))"+
                "|\\(\\*[\\S\\s]+?\\*\\)");

        Pattern patForString = Pattern.compile("((\\'([\\S\\s]+?)([\\//]|[\\{]|[\\(][\\*])([\\S\\s]+?)\\'))");
        Pattern patForRawString = Pattern.compile("(:=\\'([^\\'\\//\\{\\}\\(\\*\\)])*\\')");

        Matcher matcherAll = patForCode.matcher(text);

        while (matcherAll.find()) {
            Matcher matcherStr = patForString.matcher(text.substring(matcherAll.start(), matcherAll.end()));
            Matcher matcherRawStr = patForRawString.matcher(text.substring(matcherAll.start(), matcherAll.end()));
            if (!matcherStr.find()&&!matcherRawStr.find()) {
                regionsToDelete.add(new Pair<>(matcherAll.start(), matcherAll.end()));
            }
        }

        for (int i = regionsToDelete.size() - 1; i > -1; i--) {
            answer.replace(regionsToDelete.get(i).getKey(), regionsToDelete.get(i).getValue(), " ");
        }

        System.out.print(answer);
    }
}
