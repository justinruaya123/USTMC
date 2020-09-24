package com.ustmc.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class VectorUtils {

	public static Vector face(Location from, Location to) {
		return to.toVector().subtract(from.toVector());
	}

	public static Vector against(Location from, Location to) {
		return from.toVector().subtract(to.toVector());
	}

	public static Vector getAlternative(Vector v) {
		return v.subtract(v).subtract(v);
	}

	/**
	 * <a>Increments a by b.</a>
	 * 
	 * @return |a| + |b| or -(a) + -(b)
	 */
	public static Vector increment(Vector a, Vector b) {
		return new Vector((a.getX() + b.getX()), a.getY() + b.getY(), a.getZ() + b.getZ());
	}

	public static List<Location> getLineBlocks(Location a, Vector direction, int times, List<Material> opaque) {
		List<Location> lineBlocks = new ArrayList<Location>();
		Vector v = VectorUtils.toVector(a);
		Location vLoc = VectorUtils.toLocation(v, a.getWorld());
		for (int x = 1; x <= times; x++) {
			if (x > times) {
				return lineBlocks;
			}
			if (opaque == null && vLoc.getBlock().getType() != Material.AIR) {
				return lineBlocks;
			}
			if (opaque.contains(vLoc.getBlock().getType())) {
				return lineBlocks;
			}
			lineBlocks.add(VectorUtils.toLocation(v.add(direction.multiply(x)), a.getWorld()));
		}
		return lineBlocks;
	}

	public static Location getTargetBlock(Location a, Vector direction, int times, List<Material> opaque) {
		Vector v = VectorUtils.toVector(a);
		Location vLoc = VectorUtils.toLocation(v, a.getWorld());
		Location fl = vLoc;
		for (int x = 1; x <= times; x++) {
			if (x > times) {
				return fl;
			}
			if (opaque == null) {
				if (vLoc.getBlock().getType() != Material.AIR)
					return fl;
			} else if (opaque.contains(vLoc.getBlock().getType())) {
				return fl;
			}
			fl = fl.add(direction);
		}
		return fl;
	}

	public static Location toLocation(Vector v, World world) {
		return new Location(world, v.getX(), v.getY(), v.getZ());
	}

	public static Vector toVector(Location l) {
		return new Vector(l.getX(), l.getY(), l.getZ());
	}

	public static Vector parabolicVelocity(Vector from, Vector to, int heightGain) {
		// Gravity of a potion
		double gravity = 0.115;

		// Block locations
		int endGain = to.getBlockY() - from.getBlockY();
		double horizDist = Math.sqrt(distanceSquared(from, to));

		// Height gain
		int gain = heightGain;

		double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);

		// Solve quadratic equation for velocity
		double a = -horizDist * horizDist / (4 * maxGain);
		double b = horizDist;
		double c = -endGain;

		double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);

		// Vertical velocity
		double vy = Math.sqrt(maxGain * gravity);

		// Horizontal velocity
		double vh = vy / slope;

		// Calculate horizontal direction
		int dx = to.getBlockX() - from.getBlockX();
		int dz = to.getBlockZ() - from.getBlockZ();
		double mag = Math.sqrt(dx * dx + dz * dz);
		double dirx = dx / mag;
		double dirz = dz / mag;

		// Horizontal velocity components
		double vx = vh * dirx;
		double vz = vh * dirz;

		return new Vector(vx, vy, vz);
	}

	private static double distanceSquared(Vector from, Vector to) {
		double dx = to.getBlockX() - from.getBlockX();
		double dz = to.getBlockZ() - from.getBlockZ();

		return dx * dx + dz * dz;
	}
}
