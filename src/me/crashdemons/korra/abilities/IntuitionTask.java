/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.crashdemons.korra.abilities;

import com.projectkorra.ProjectKorra.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.potion.*;
import org.bukkit.util.Vector;
import java.util.*;
import java.lang.reflect.*;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.metadata.FixedMetadataValue;
/**
 *
 * @author Crash
 */
public class IntuitionTask extends BukkitRunnable  {
    final static long cool_max = 7500;
    final static long exp_min = 1395;
    final static long exp_max = 1506;


    public IntuitionTask(){
        super();
    }
    private long getCooldown(BendingPlayer bp,String ability){
        if(ability==null || ability.equals("")) return cool_max;
        if(!bp.isOnCooldown(ability)) return 0;
        try{
            Field f = bp.getClass().getDeclaredField("cooldowns");
            f.setAccessible(true);
            ConcurrentHashMap cooldowns = (ConcurrentHashMap) f.get(bp);
            Long tsCooldown = (Long) cooldowns.get(ability);
            Long tsCurrent = System.currentTimeMillis();
            Long cooldown_remaining=tsCooldown-tsCurrent;
            if(cooldown_remaining<0) return 0;
            if(cooldown_remaining>cool_max) return cool_max;
            return cooldown_remaining;

        } catch (NullPointerException e) {
            return cool_max/2;
        } catch (NoSuchFieldException e) {
            return cool_max/2;
        } catch (IllegalAccessException e) {
            return cool_max/2;
        }
    }
    private float getCooldownPercent(BendingPlayer bp,String ability){
        double remaining=(float)getCooldown(bp,ability);
        double max=(float) cool_max;
        return (float) (remaining/max);

    }


    private void updatePlayerRechargeBar(Player player,BendingPlayer bPlayer, String ability){
                double fraction=1.0f-getCooldownPercent(bPlayer,ability);
                player.setExp((float)fraction);
    }
    private void updatePlayerAbility(Player player, String splayer,String ability){
        if(player.hasMetadata("intuitionEnabled"))
            if(player.getMetadata("intuitionEnabled").get(0).asInt()==0) return;


        if(player.hasMetadata("intuitionSelectedAbility")){
            if(player.getMetadata("intuitionSelectedAbility").get(0).asString().equals(ability)) return;
        }
        player.setMetadata("intuitionSelectedAbility", new FixedMetadataValue(ProjectKorra.plugin, ability));
        player.sendMessage(ability);
    }
    public void run()
    {
        //ProjectKorra.plugin.getLogger().info("INTUITION: Changing everyone's XP");
            Player[] onlinePlayers = ProjectKorra.plugin.getServer().getOnlinePlayers();

            for (Player player : onlinePlayers){
                String splayer = player.getName();
                BendingPlayer bPlayer = Methods.getBendingPlayer(splayer);
                if(bPlayer==null) continue;
                String ability = Methods.getBoundAbility(player);
                updatePlayerRechargeBar(player,bPlayer,ability);
                if(ability==null || ability.equals("")) continue;
                updatePlayerAbility(player, splayer,ability);
            }
    }





}
