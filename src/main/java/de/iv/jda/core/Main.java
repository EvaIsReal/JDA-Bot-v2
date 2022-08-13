package de.iv.jda.core;

import de.iv.jda.commands.ServerSlashCommand;
import de.iv.jda.data.Assignment;
import de.iv.jda.listeners.ModalListener;
import de.iv.jda.listeners.SlashCommandManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
    private static Guild g;

    static HashMap<Member, RuntimeData> dataContainerMap = new HashMap<>();
    public static ArrayList<Assignment> assignments = new ArrayList<>();
    public static List<Member> MEMBERS = new ArrayList<>();
    private static JDA jda;

    public static void main(String[] args) throws LoginException, InterruptedException {
        Timer timer = new Timer();
        TimerTask task;

        jda = JDABuilder.createDefault(getToken())
                .setActivity(Activity.listening("/help"))
                .setChunkingFilter(ChunkingFilter.ALL)
                .disableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new SlashCommandManager())
                .addEventListeners(new ModalListener())
                .build()
                .awaitReady();

        try {
            Uni.loadAssignments();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        jda.retrieveCommands().complete().forEach(c -> c.delete().queue());
        g = jda.getGuildCache().getElementById("945071064210350090");
        applyCommands(g);

        System.out.println("BOT READY");
        ExecutorService service = Executors.newFixedThreadPool(1);
        task = new TimerTask() {
            @Override
            public void run() {
                if(!assignments.isEmpty()) {
                    assignments.forEach(service::submit);
                }
                try {
                    Uni.saveAssignments();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, 0, 1);

    }

    private static class AssCheck implements Runnable {
        @Override
        public void run() {

        }
    }

    public static String getToken() {
        File file = new File("config.pvt");
        if(file.exists()) {
            try {
                Scanner scanner = new Scanner(file);
                List<String> result = scanner.tokens().filter(a -> a.startsWith("TOKEN=")).toList();
                return result.get(0).substring(6);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JDA getJda() {
        return jda;
    }

    private static void applyCommands(Guild g) {
        SlashCommandManager.getServerSlashCommandList().forEach(c -> {
            if(g != null) {
                c.apply(g);
                System.out.println(c.name() + ", " + c.description());
            } else System.out.println("Guild is null");
        });
    }

    public static RuntimeData getRuntimeData(Member member) {
        RuntimeData data;
        if (dataContainerMap.containsKey(member)) {
            return dataContainerMap.get(member);
        } else {
            data = new RuntimeData(member);
            dataContainerMap.put(member, data);
            return data;
        }
    }

    public static Guild getGuild() {
        return g;
    }
}
