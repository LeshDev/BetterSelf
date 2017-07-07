package de.lesh.betterself;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.lesh.betterself.commands.*;
import de.lesh.betterself.util.Conf;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.JDAInfo;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.SimpleLog;

public class Main {

	public static JDA jda = null;
	public static Conf CONFIG = new Conf();
	public static Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	public static String fileName = "config.json";
	public static String dir = System.getProperty("user.dir") + "/";
    public static SimpleLog LOG = SimpleLog.getLog("Application");
    public static User USER = null;
	public static JDABuilder jdaB = new JDABuilder(AccountType.CLIENT).setToken(CONFIG.token());
	
	public static void main(String[] args) throws Exception{
		setup();
		jda = new JDABuilder(AccountType.CLIENT).setToken(CONFIG.token()).buildAsync();
		
		System.out.println("[BOOT] >> Launching BetterSelf");
		System.out.println("[BOOT] >> Version: BETA");
		System.out.println("[INFO] >> Checking JDA Version: " + JDAInfo.VERSION);
//
//		USER = jda.getSelfUser();
//		LOG.info("Succesful logged into " + USER.getName());
		
		jdaB.addEventListener(new Unshorter());
		
		System.out.println("[SUCCESSFUL] >> Added all EventListeners");
		System.out.println("[SUCCESSFUL] >> Activating RoleBot");
	}
	
	public static void setup(){
		try{
			File file = new File(dir + fileName);
			if(!file.exists()){
				if(file.createNewFile()){
					BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					writer.write(GSON.toJson(CONFIG));
					writer.close();
					LOG.info("Succesful created CONFIG file in " + dir);
				} else {
					LOG.info("Something went wrong. Error will be checked");
					System.exit(0);
				}
			} else {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				CONFIG = GSON.fromJson(reader, Conf.class);
				if(CONFIG.token() == null || CONFIG.token().isEmpty()) {
					LOG.fatal("There is no token. Insert your personal token to use this bot");
					return;
				} 
				if (CONFIG.prefix == null) {
					LOG.fatal("There is no prefix. Set your prefix for your commands");
					return;
				}
				LOG.info("Config has been loaded ... ");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
