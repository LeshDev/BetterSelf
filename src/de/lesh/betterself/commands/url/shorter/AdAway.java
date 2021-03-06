package de.lesh.betterself.commands.url.shorter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.lesh.betterself.Main;
import de.lesh.betterself.util.lib;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class AdAway extends ListenerAdapter {

	public static String output;
	public void onMessageReceived(MessageReceivedEvent e) {
		Message msg = e.getMessage();
		if(msg.getRawContent().startsWith(Main.CONFIG.getPrefix() + "adaway ") && msg.getAuthor().equals(Main.USER)&& !lib.getServerSecure(e)){
			String[] split = e.getMessage().getRawContent().split("\\s+", 2);
			String unshorting = split[1];
			String unshortener = "http://thor.johanpaul.net/lengthen/result?url=" + unshorting;
			
			try {
				URL shorten = new URL(unshortener);
				HttpURLConnection connect = (HttpURLConnection)shorten.openConnection();
				connect.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
				connect.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
				connect.setRequestProperty("Referer", "http://thor.johanpaul.net/lengthen");
				connect.setRequestMethod("GET");
				String response = null;
				try(Scanner s = new Scanner(connect.getInputStream())){
				    s.useDelimiter("\\A");
				    response = s.next();
				} catch(InputMismatchException e1){
					e1.getStackTrace();
				}
				Matcher m = Pattern.compile("value=\"(.*?)\"").matcher(response);
				if (m.find()) {
				    output = m.group(1);
				}
				
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.getChannel().sendMessage("[UNSHORTENER+] >> Original link: " + output).queueAfter(30, TimeUnit.SECONDS);
			
			System.out.println("[SUCCESSFUL] >> UNSHORTED+ - Original: " + unshorting + " - Unshorted: " + output);
			e.getMessage().delete().queueAfter(1, TimeUnit.MILLISECONDS);
		}
	}
}
