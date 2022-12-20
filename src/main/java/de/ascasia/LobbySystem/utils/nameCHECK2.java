package de.ascasia.LobbySystem.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public class nameCHECK2 {
    // Function to remove the first and
    // the last character of a string
    public static String
    removeFirstandLast(String str)
    {

        // Removing first and last character
        // of a string using substring() method
        str = str.substring(2, str.length() - 1);

        // Return the modified string
        return str;
    }

    // Driver Code
    public static void main(String args[])
    {
        // Given String str
        String str = "GeeksForGeeks";

        // Print the modified string
        System.out.print(
                removeFirstandLast(str));
    }


    public static String main(String uuid) throws Exception {

        URL oracle = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        String name = null;

        if ((inputLine = in.lines().collect(Collectors.joining())) != null) {
            name = onlyName(inputLine);
            return name;
        }

        in.close();
        return name ;
    }



    public static String onlyName(String input) {

        String a = input;
        String b = a.replace("," , ":");
        String[] c = b.split(":");
        String d = c[3].replace("}" , "");
        String output = removeFirstandLast(d);

        return output;
    }
}

