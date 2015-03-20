package com.mrlolethan.stackedmobs.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;

import com.mrlolethan.stackedmobs.P;

public enum StacksUtil {;

	/** The stacked mob custom name format. */
	public static String CUSTOM_NAME_FORMAT = "x%1$d";
	public static int INVALID_STACK = -1;


	/*
	 * Stack/unstack methods
	 */
	public static boolean attemptUnstackOne(LivingEntity livingEntity) {
		ChatColor stackNumberColor = P.getInstance().getConfiguration().stackNumberColor;

		String displayName = livingEntity.getCustomName();
		int stackAmt = parseAmount(displayName);

		if (stackAmt <= 1) {
			// The stack is down to one mob
			livingEntity.setHealth(0);
			return false;
		}

		// Spawn a new mob and instantly kill it
		LivingEntity dupEntity = (LivingEntity) livingEntity.getWorld().spawnEntity(livingEntity.getLocation(), livingEntity.getType());
		dupEntity.setHealth(0);

		// Update the mob stack's status
		stackAmt--;
		String newDisplayName = String.format(stackNumberColor + CUSTOM_NAME_FORMAT, stackAmt);
		livingEntity.setCustomName(newDisplayName);
		return true;
	}

	public static boolean stack(LivingEntity target, LivingEntity stackee) {
		if (target.getType() != stackee.getType()) {
			return false; // The entities must be of the same type.
		}
		ChatColor stackNumberColor = P.getInstance().getConfiguration().stackNumberColor;

		String displayName = target.getCustomName();
		int alreadyStacked = parseAmount(displayName);
		int stackDelta = 1;

		// Check if the stackee is already a stack
		if (isStacked(stackee)) {
			stackDelta = parseAmount(stackee.getCustomName());
		}

		stackee.remove();

		if (alreadyStacked == INVALID_STACK) {
			// The target is NOT a stack
			String newDisplayName = String.format(stackNumberColor + CUSTOM_NAME_FORMAT, 1 + stackDelta);
			target.setCustomName(newDisplayName);
			target.setCustomNameVisible(true);
		} else {
			// The target is already a stack
			String newDisplayName = String.format(stackNumberColor + CUSTOM_NAME_FORMAT, alreadyStacked + stackDelta);
			target.setCustomName(newDisplayName);
		}
		return true;
	}

	/*
	 * "Helper" methods
	 */
	public static int parseAmount(String displayName) {
		if (displayName == null) {
			return INVALID_STACK; // No display name, therefor not a stack.
		}
		// Fetch the stack number color
		ChatColor stackNumberColor = P.getInstance().getConfiguration().stackNumberColor;

		String nameColor = ChatColor.getLastColors(displayName);
		if (nameColor.equals(ChatColor.COLOR_CHAR + stackNumberColor.getChar())) {
			return INVALID_STACK; // Not a valid stack.
		}
		String cleanDisplayName = ChatColor.stripColor(displayName).replace("x", "");
		return Integer.parseInt(cleanDisplayName);
	}

	public static boolean isStacked(LivingEntity entity) {
		return parseAmount(entity.getCustomName()) != INVALID_STACK;
	}

}
