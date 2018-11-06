package com.Pride.korra.waterabilities;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.Pride.korra.listener.AbilListener;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class WaterSprite extends WaterAbility implements AddonAbility {
	
	public ArrayList<Player> update = new ArrayList<Player>();
	private static String path = "ExtraAbilities.Prride.WaterSprite.";
	private long cooldown;
	private long duration;
	private long time;
	private int regenPower;
	private Block sourceBlock;
	private double selectRange;
	Random random = new Random();
	private double t = 3.0;

	public WaterSprite(Player player) {
		super(player);
		// TODO Auto-generated constructor stub
		if (!bPlayer.canBend(this)) {
			return;
		}
		FileConfiguration config = ConfigManager.getConfig();
		cooldown = config.getLong(path + "Cooldown");	
		duration = config.getLong(path + "Duration");
		regenPower = config.getInt(path + "RegenPower");
		selectRange = config.getDouble(path + "SelectRange");
		time = System.currentTimeMillis();
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		sourceBlock = getWaterSourceBlock(player, selectRange, true);
		if (mainHand.getType() == Material.POTION || mainHand.getType() == Material.WATER_BUCKET || sourceBlock != null && !GeneralMethods.isRegionProtectedFromBuild(this, sourceBlock.getLocation())) {
			start();
			update.add(player);
			bPlayer.addCooldown(this);
		}
	}

	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return cooldown;
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "WaterSprite";
	}

	@Override
	public boolean isHarmlessAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void progress() {
		// TODO Auto-generated method stub
		if (player.isDead() || !player.isOnline()) {
			remove();
			return;
		}
		if (update.contains(player)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, regenPower));
			
			waterSprite(player);
			
			if (time + duration < System.currentTimeMillis()) {
				bPlayer.addCooldown(this);
				remove();
				return;
			}
		}
	}
	
	@Override
	public void remove() {
		super.remove();
		
		update.remove(player);
		
		if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
			player.removePotionEffect(PotionEffectType.REGENERATION);
		}
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "Prride";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "Build 1";
	}
	
	public void waterSprite(final Player player) {
		// parametric equation using rotation matrices from 
		// Steezyyy's channel https://www.youtube.com/channel/UCIA3ywM1G19ZlIfD1HOKOjg
		if (update.contains(player)) {
			t = t + Math.PI / 200;
			final Location loc = player.getLocation();
			double x, y, z;
			double x1, y1, z1;
			double x2, y2, z2;
			double r = 0.5;
			
			x2 = Math.sin(3 * t);
			y2 = 2 * Math.cos(t);
			z2 = Math.sin(2 * t);
			
			t -= Math.PI / 200;
			
			x1 = Math.sin(3 * t);
			y1 = 2 * Math.cos(t);
			z1 = Math.sin(2 * t);
			
			t += Math.PI / 200;
			
			Vector dir = new Vector(x2 - x1, y2 - y1, z2 - z1);
			Location loc2 = new Location(player.getWorld(), 0, 0, 0).setDirection(dir.normalize());
			loc2.setDirection(dir.normalize());
			
			if (random.nextInt(10) == 0) {
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3F, 0.1F);
			}
			
			for (double i = 0; i < 2; i++) {
				x = 0.2 * t;
				y = r * Math.sin(i) + 2 * Math.sin(10 * t) + 2.8;
				z = r * Math.cos(i);
				
				Vector v = new Vector(x, y, z);
				v = rotateFunction(v, loc2);
				loc.add(v.getX(), v.getY(), v.getZ());
				GeneralMethods.displayColoredParticle(loc, "47DDF7");
				ParticleEffect.SPLASH.display(loc, 0.3F, 0.3F, 0.3F, 0.1F, 5);
				loc.subtract(v.getX(), v.getY(), v.getZ());
			}
		}
	}
	
	public static Vector rotateFunction(Vector v, Location loc) {
		double yaw = loc.getYaw() / 180.0 * Math.PI;
		double pitch = loc.getPitch() / 180.0 * Math.PI;
		
		v = rotateAroundAxisX(v, pitch);
		v = rotateAroundAxisY(v, -yaw);
		return v;
	}
	
	public static Vector rotateAroundAxisX(Vector v, double a) {
		double y = Math.cos(a) * v.getY() - Math.sin(a) * v.getZ();
		double z = Math.sin(a) * v.getY() + Math.cos(a) * v.getZ();
		return v.setY(y).setZ(z);
	}
	
	public static Vector rotateAroundAxisY(Vector v, double b) {
		double x = Math.cos(b) * v.getX() + Math.sin(b) * v.getZ();
		double z = -Math.sin(b) * v.getX() + Math.cos(b) * v.getZ();
		return v.setX(x).setZ(z);
	}
	
	public static Vector rotateAroundAxisZ(Vector v, double g) {
		double x = Math.cos(g) * v.getX() - Math.sin(g) * v.getY();
		double y = Math.sin(g) * v.getX() + Math.cos(g) * v.getY();
		return v.setX(x).setY(y);
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new AbilListener(), ProjectKorra.plugin);
		
		ConfigManager.getConfig().addDefault(path + "Cooldown", 8000);
		ConfigManager.getConfig().addDefault(path + "Duration", 8000);
		ConfigManager.getConfig().addDefault(path + "SelectRange", 20);
		ConfigManager.getConfig().addDefault(path + "RegenPower", 1);
		ConfigManager.defaultConfig.save();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.remove();
	}

}
