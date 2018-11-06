package com.Pride.korra.waterabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class Scald extends WaterAbility implements AddonAbility {
	
	private ArrayList<FallingBlock> fblock = new ArrayList<FallingBlock>();
	private static String path = "ExtraAbilities.Prride.Scald.";
	private long cooldown;
	private Block sourceBlock;
	private FallingBlock fb;
	private double selectRange;
	Random random = new Random();
	private Vector direction;
	private int travelled;
	private int fireTicks;
	private double damage;
	private boolean enableParticles;
	private Location fbLoc;
	private int fallingBlocks;

	public Scald(Player player) {
		super(player);
		// TODO Auto-generated constructor stub
		if (!bPlayer.canBend(this)) {
			return;
		}
		if (bPlayer.isOnCooldown(this)) {
			remove();
			return;
		}
		FileConfiguration config = ConfigManager.getConfig();
		cooldown = config.getLong(path + "Cooldown");
		selectRange = config.getDouble(path + "SelectRange");
		fireTicks = config.getInt(path + "FireTicks");
		damage = config.getDouble(path + "Damage");
		fallingBlocks = config.getInt(path + "NumberOfFallingBlocks");
		enableParticles = config.getBoolean(path + "EnableParticles");
		sourceBlock = getWaterSourceBlock(player, selectRange, true);
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
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Scald";
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
		if (player.isDead() || !player.isOnline()) {
			remove();
			return;
		}
		if (player.isSneaking()) {
			if (sourceBlock != null) {
				List<Location> area = GeneralMethods.getCircle(sourceBlock.getLocation(), 3, 1, false, true, 1);
				for (Location loc : area) {
					if (enableParticles) {
						ParticleEffect.SPLASH.display(loc, 0F, 0F, 0F, 0F, 1);
					}
				}
			}
			travelled++;
			switch (travelled) {
			case 1:
				scaldingBlocks();
				player.getWorld().playSound(fbLoc, Sound.ENTITY_PLAYER_SPLASH, 0.3F, 0.1F);
			case 15:
				scaldingBlocks();
				player.getWorld().playSound(fbLoc, Sound.ENTITY_PLAYER_SPLASH, 0.3F, 0.1F);
			case 30:
				scaldingBlocks();
				player.getWorld().playSound(fbLoc, Sound.ENTITY_PLAYER_SPLASH, 0.3F, 0.1F);
			case 45:
				scaldingBlocks();
				player.getWorld().playSound(fbLoc, Sound.ENTITY_PLAYER_SPLASH, 0.3F, 0.1F);
			case 60:
				scaldingBlocks();
				player.getWorld().playSound(fbLoc, Sound.ENTITY_PLAYER_SPLASH, 0.3F, 0.1F);
			default:
				bPlayer.addCooldown(this);
				break;
			}
		} else {
			remove();
			bPlayer.addCooldown(this);
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void scaldingBlocks() {
		if (sourceBlock != null && !GeneralMethods.isRegionProtectedFromBuild(this, sourceBlock.getLocation())) {
			for (int i = 0; i < fallingBlocks; i++) {
				direction = player.getEyeLocation().getDirection();
				double x = random.nextDouble() * 0.2;
				double z = random.nextDouble() * 0.2;

				x = (random.nextBoolean()) ? x : -x;
				z = (random.nextBoolean()) ? z : -z;

				fb = player.getWorld().spawnFallingBlock(sourceBlock.getLocation(), Material.PRISMARINE, (byte) 0);
				fb.setVelocity(direction.clone().add(new Vector(x, 1, z)).normalize().multiply(0.9));
				fb.canHurtEntities();
				fb.setDropItem(false);
				fb.setFireTicks(90);
				fblock.add(fb);
				
				for (FallingBlock fblocks : fblock) {
					fbLoc = fblocks.getLocation();
					for (Entity entity : GeneralMethods.getEntitiesAroundPoint(fbLoc, 2.5)) {
						if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId()) {
							DamageHandler.damageEntity(entity, damage, this);
							entity.setFireTicks(fireTicks);
							if (random.nextInt(10) == 0) {
								player.getWorld().playSound(entity.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.1F, 0.1F);
							}
						}
					}
				}
			}
			WaterAbility.playWaterbendingSound(sourceBlock.getLocation());
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
		ConfigManager.getConfig().addDefault(path + "Cooldown", 6500);
		ConfigManager.getConfig().addDefault(path + "SelectRange", 20);
		ConfigManager.getConfig().addDefault(path + "FireTicks", 50);
		ConfigManager.getConfig().addDefault(path + "Damage", 1);
		ConfigManager.getConfig().addDefault(path + "EnableParticles", true);
		ConfigManager.getConfig().addDefault(path + "NumberOfFallingBlocks", 12);
		ConfigManager.defaultConfig.save();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.remove();
	}

}
