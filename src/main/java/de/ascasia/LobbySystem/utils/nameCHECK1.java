package de.ascasia.LobbySystem.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class nameCHECK1 {
    // Function to remove the first and
    // the last character of a string
    public static String
    removeFirstandLast(String str)
    {

        // Removing first and last character
        // of a string using substring() method
        str = str.substring(1, str.length() - 1);

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


    public static String main(String name) throws Exception {

        URL oracle = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        String uuid = null;

        if ((inputLine = in.readLine()) != null) {
            uuid = onlyUUID(inputLine);
            return uuid;
        }

        in.close();
        return uuid ;
    }



    public static String onlyUUID(String input) {

        String a = input;
        String b = a.replace("," , ":");
        String[] c = b.split(":");
        String d = c[3].replace("}" , "");
        String output = removeFirstandLast(d);

        return output;
    }
}

