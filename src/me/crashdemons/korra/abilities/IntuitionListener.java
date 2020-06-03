/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.crashdemons.korra.abilities;

import com.projectkorra.ProjectKorra.ProjectKorra;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.command.*;
import org.bukkit.metadata.FixedMetadataValue;
/**
 *
 * @author Crash
 */
public class IntuitionListener 

    implements Listener
{
    static final String IntuitionMessage[]=new String[]{"OFF","ON"};

    private int toggleIntuition(Player player){
        int enabled=1;
        if(player.hasMetadata("intuitionEnabled"))
            enabled=player.getMetadata("intuitionEnabled").get(0).asInt();
        enabled^=1;
        player.setMetadata("intuitionEnabled", new FixedMetadataValue(ProjectKorra.plugin, enabled));
        return enabled;
    }

    //this is bad but there is no alternative since Korra intercepts ability commands;
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
            String cmd=event.getMessage();
            Player player = event.getPlayer();

            if(!cmd.equals("/intuition")) return;
            int state=toggleIntuition(player);
            player.sendMessage("Intuition ability text is now turned "+IntuitionMessage[state] + " for "+player.getName());
            event.setCancelled(true);
    }
}
