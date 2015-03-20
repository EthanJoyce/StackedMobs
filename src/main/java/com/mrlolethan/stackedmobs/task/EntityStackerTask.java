package com.mrlolethan.stackedmobs.task;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import com.mrlolethan.stackedmobs.P;
import com.mrlolethan.stackedmobs.util.StacksUtil;

public class EntityStackerTask extends BukkitRunnable {

	@Override
	public void run() {
		final int radius = P.getInstance().getConfiguration().stackRadius;
		final Set<EntityType> entityTypes = P.getInstance().getConfiguration().mobTypes;

		// Iterate through all worlds
		for (World world : Bukkit.getServer().getWorlds()) {
			// Iterate through all entities in this world
			for (LivingEntity entity : world.getLivingEntities()) {
				// Don't waste precious CPU cycles on entities that don't stack or are invalid
				if (!entityTypes.contains(entity.getType()) || !entity.isValid()) {
					continue;
				}

				// Iterate through all entities in range
				for (Entity nearby : entity.getNearbyEntities(radius, radius, radius)) {
					if (!(nearby instanceof LivingEntity) || !nearby.isValid()) {
						continue; // Not a living entity or is invalid.
					}
					if (!entityTypes.contains(nearby.getType())) {
						continue; // This entity type is not enabled to stack.
					}
					StacksUtil.stack(entity, (LivingEntity) nearby);
				}
			}
		}
	}

}
