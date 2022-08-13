package de.iv.jda.core;

import com.google.gson.Gson;
import de.iv.jda.data.Assignment;
import net.dv8tion.jda.api.entities.Member;

import javax.annotation.processing.Filer;
import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Uni {

    public static ArrayList<Assignment> assignments = new ArrayList<>();

    public static long getMillisFromString(String str) {
        // 1 __unit
        String[] args = str.split(" ");
        if(args.length > 2) throw new InvalidParameterException("There was a mistake regarding time formatting.");
        double amount = Double.parseDouble(args[0]);
        String unit = args[1];
        // amount * unit.getTime();
        char c = unit.charAt(0);
        long time = getMillisFromChar(c);

        return (long) (amount * time);

    }

    private static long getMillisFromChar(char c) {
        switch (c) {
            case 's':
                return 1000;
            case 'm':
                return 60000;
            case 'h':
                return 3600000;
            case 'd':
                return 86400000;
            default: return -1;
        }
    }

    public static Assignment createAssignment(Member sender, Member receiver, String title, String message, Date dateExpired) {
        Assignment a = new Assignment(sender.getIdLong(), receiver.getIdLong());
        a.setDateCreated(new Date(System.currentTimeMillis()));
        a.setTitle(title);
        a.setMessage(message);
        a.setDateExpired(dateExpired);
        assignments.add(a);
        return a;
    }

    public static void serialize(Object o, File file) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream oOut = new ObjectOutputStream(fileOut);

        oOut.writeObject(o);
        fileOut.close();
        oOut.close();
    }

    public static Object deserialize(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream oIn = new ObjectInputStream(fileIn);

        Object o = oIn.readObject();
        fileIn.close();
        oIn.close();
        return o;
    }

    public static void saveAssignments() throws IOException {
        File file = new File("assignments/");
        if(!file.exists()) {
            file.mkdirs();
        }
        assignments.forEach(a -> {
            File aFile = new File(file.getPath(), a.getTitle() + ".ser");
            if(!aFile.exists()) {
                try {
                    aFile.createNewFile();
                    serialize(a, aFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void loadAssignments() throws FileNotFoundException {
        File file = new File("assignments/");
        ArrayList<Assignment> n = new ArrayList<>();
        if(file.isDirectory()) {
            Arrays.stream(file.listFiles()).toList().forEach(f -> {
                try {
                    n.add((Assignment) deserialize(f));
                    f.delete();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
        Main.assignments = n;
    }

    public static ArrayList<Assignment> getAssignments() {
        return assignments;
    }
}
