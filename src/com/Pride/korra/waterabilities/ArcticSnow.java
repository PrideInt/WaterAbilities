package com.Pride.korra.waterabilities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.IceAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class ArcticSnow extends IceAbility implements AddonAbility {
	
	private static String path = "ExtraAbilities.Prride.ArcticSnow.";
	private long cooldown;
	private long duration;
	private long chargeTime;
	private double selectRange;
	private long time;
	private boolean charged;
	private double damage;
	private int slowDuration;
	private int slowPower;
	private boolean doDamage;
	private int currPoint;
	private double radius;
	
	private Block sourceBlock;
	private Location location;

	public ArcticSnow(Player player) {
		super(player);
		// TODO Auto-generated constructor stub
		if (!bPlayer.canBend(this)) {
			return;
		}
		FileConfiguration config = ConfigManager.getConfig();
		cooldown = config.getLong(path + "Cooldown");
		selectRange = config.getDouble(path + "SelectRange");
		chargeTime = config.getLong(path + "ChargeTime");
		duration = config.getLong(path + "Duration");
		doDamage = config.getBoolean(path + "DoDamage");
		slowPower = config.getInt(path + "SlowPower");
		slowDuration = config.getInt(path + "SlowDuration");
		damage = config.getDouble(path + "Damage");
		radius = config.getDouble(path + "Radius");
		time = System.currentTimeMillis();
		sourceBlock = BlockSource.getWaterSourceBlock(player, selectRange, ClickType.SHIFT_DOWN, true, true, false);
		start();
	}

	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return cooldown;
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ArcticSnow";
	}

	@Override
	public boolean isHarmlessAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void progress() {
		// TODO Auto-generated method stub
		if (!player.isOnline() || player.isDead()) {
			remove();
			return;
		}
		if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
		arcticSnow();
	}
	
	private void arcticSnow() {
		if (player.isSneaking()) {
			if (time + chargeTime < System.currentTimeMillis()) {
				charged = true;
			} else {
				displayCircle();
			}
			if (charged) {
				if (time + duration < System.currentTimeMillis()) {
					bPlayer.addCooldown(this);
					remove();
					return;
				} else {
					animateCircle();
					animateStorm();
					slowArea();
				}
			}
		} else {
			bPlayer.addCooldown(this);
			remove();
			return;
		}
	}
	
	private void displayCircle() {
		for (int i = 0; i < 1; ++i) {
			currPoint += 360 / 60;
			if (currPoint > 360) {
				currPoint = 0;
			}
			animateCircle();
		}
	}
	
	private void animateCircle() {
		for (int i = 0; i < 1; ++i) {
			currPoint += 360 / 60;
			if (currPoint > 360) {
				currPoint = 0;
			}
			double angle = currPoint * Math.PI / 180.0D;
			double x = radius * Math.cos(angle);
			double z = radius * Math.sin(angle);
			if (sourceBlock != null) {
				Location loc = sourceBlock.getLocation().add(x, 0.0D, z);
				ParticleEffect.SNOW_SHOVEL.display(loc, 0F, 0F, 0F, 0F, 1);
			}
			
			double anglen = currPoint * Math.PI / 180.0D;
			double xn = radius * -Math.cos(anglen);
			double zn = radius * -Math.sin(anglen);
			if (sourceBlock != null) {
				Location locn = sourceBlock.getLocation().add(xn, 0.0D, zn);
				ParticleEffect.SNOW_SHOVEL.display(locn, 0F, 0F, 0F, 0F, 1);
			}
		}
	}
	
	private void animateStorm() {
		for (int i = 0; i < 1; ++i) {
			currPoint += 360 / 60;
			if (currPoint > 360) {
				currPoint = 0;
			}
			double angle = currPoint * Math.PI / 180.0D;
			double x = radius * Math.cos(angle);
			double z = radius * Math.sin(angle);
			Location loc = sourceBlock.getLocation().add(x, 0.0D, z);
			ParticleEffect.SNOW_SHOVEL.display(loc, 0.25F, 0.25F, 0.25F, 0.1F, 5);
			ParticleEffect.SNOW_SHOVEL.display(loc.add(0, 1, 0), 0.25F, 0.25F, 0.25F, 0.1F, 5);
			ParticleEffect.SNOW_SHOVEL.display(loc.add(0, 2, 0), 0.25F, 0.25F, 0.25F, 0.1F, 5);
		
			ParticleEffect.SNOWBALL_POOF.display(loc, 0.25F, 0.25F, 0.25F, 0.1F, 5);
			ParticleEffect.SNOWBALL_POOF.display(loc.add(0, 1, 0), 0.25F, 0.25F, 0.25F, 0.1F, 5);
			ParticleEffect.SNOWBALL_POOF.display(loc.add(0, 2, 0), 0.25F, 0.25F, 0.25F, 0.1F, 5);
			
			
			
			double anglen = currPoint * Math.PI / 180.0D;
			double xn = radius * -Math.cos(anglen);
			double zn = radius * -Math.sin(anglen);
			Location locn = sourceBlock.getLocation().add(xn, 0.0D, zn);
			ParticleEffect.SNOW_SHOVEL.display(locn, 0.25F, 0.25F, 0.25F, 0.1F, 5);
			ParticleEffect.SNOW_SHOVEL.display(locn.add(0, 1, 0), 0.25F, 0.25F, 0.25F, 0.1F, 5);
			ParticleEffect.SNOW_SHOVEL.display(locn.add(0, 2, 0), 0.25F, 0.25F, 0.25F, 0.1F, 5);
			
			ParticleEffect.SNOWBALL_POOF.display(locn, 0.25F, 0.25F, 0.25F, 0.1F, 5);
			ParticleEffect.SNOWBALL_POOF.display(locn.add(0, 1, 0), 0.25F, 0.25F, 0.25F, 0.1F, 5);
			ParticleEffect.SNOWBALL_POOF.display(locn.add(0, 2, 0), 0.25F, 0.25F, 0.25F, 0.1F, 5);
		}
	}
	
	private void slowArea() {
		for (Entity entity : GeneralMethods.getEntitiesAroundPoint(sourceBlock.getLocation(), radius)) {
			if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
				((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slowDuration, slowPower));
				if (doDamage) {
					DamageHandler.damageEntity(entity, damage, this);
				}
			}
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

	@Override
	public void load() {
		// TODO Auto-generated method stub
		ConfigManager.getConfig().addDefault(path + "Cooldown", 7500);
		ConfigManager.getConfig().addDefault(path + "Duration", 6000);
		ConfigManager.getConfig().addDefault(path + "ChargeTime", 1500);
		ConfigManager.getConfig().addDefault(path + "SelectRange", 30);
		ConfigManager.getConfig().addDefault(path + "DoDamage", true);
		ConfigManager.getConfig().addDefault(path + "Damage", 1);
		ConfigManager.getConfig().addDefault(path + "SlowPower", 3);
		ConfigManager.getConfig().addDefault(path + "SlowDuration", 80);
		ConfigManager.getConfig().addDefault(path + "Radius", 3);
		ConfigManager.defaultConfig.save();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.remove();
	}

}
