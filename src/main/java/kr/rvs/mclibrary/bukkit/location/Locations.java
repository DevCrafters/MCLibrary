package kr.rvs.mclibrary.bukkit.location;

import kr.rvs.mclibrary.general.Numbers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Junhyeong Lim on 2017-10-08.
 */
public class Locations {
    public static String toString(Vector vector) {
        return vector != null ?
                String.format("x: %s, y: %s, z: %s", vector.getX(), vector.getY(), vector.getZ()) :
                "null";
    }

    public static Location setDirection(Location location, Vector vector) {
        final double _2PI = 2 * Math.PI;
        final double x = vector.getX();
        final double z = vector.getZ();

        if (x == 0 && z == 0) {
            location.setPitch(vector.getY() > 0 ? -90 : 90);
            return location;
        }

        double theta = Math.atan2(-x, z);
        location.setYaw((float) Math.toDegrees((theta + _2PI) % _2PI));

        double x2 = Numbers.square(x);
        double z2 = Numbers.square(z);
        double xz = Math.sqrt(x2 + z2);
        location.setPitch((float) Math.toDegrees(Math.atan(-vector.getY() / xz)));

        return location;
    }

    public static Location getTopLocation(Location location) {
        World world = location.getWorld();
        int height = world.getMaxHeight();
        int x = location.getBlockX();
        int z = location.getBlockZ();
        for (int i = height; i >= 0; i--) {
            Block block = world.getBlockAt(x, i, z);
            if (block.getType().isSolid())
                return new Location(world, x, i + 1, z);
        }

        return location;
    }

    public static Location getEmptyLocation(Location location, Predicate<Block> filter) {
        World world = location.getWorld();
        int maxHeight = world.getMaxHeight();
        int x = location.getBlockX();
        int z = location.getBlockZ();
        for (int i = location.getBlockY(); i < maxHeight; i++) {
            Block block = world.getBlockAt(x, i, z);
            if (block.getType() == Material.AIR
                    && filter.test(block))
                return new Location(world, x, i, z);
        }

        return location;
    }

    public static Location getEmptyLocation(Location location) {
        return getEmptyLocation(location, block -> true);
    }

    public static Vector toBlockVector(Vector vector) {
        return new Vector(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    public static Vector toBlockVector(Location location) {
        return toBlockVector(location.toVector());
    }

    public static List<Block> getRelatives(Block block, int radius, Predicate<Block> predicate) {
        List<Block> blocks = new ArrayList<>((int) Math.pow(radius * 2 + 1D, 2));
        World world = block.getWorld();
        int blockX = block.getX();
        int blockZ = block.getZ();
        for (int x = blockX - radius; x <= blockX + radius; x++) {
            for (int z = blockZ - radius; z <= blockZ + radius; z++) {
                Block newBlock = world.getBlockAt(x, block.getY(), z);
                if (predicate.test(newBlock)) {
                    blocks.add(newBlock);
                }
            }
        }
        return blocks;
    }

    public static List<Block> getRelatives(Block block, int radius) {
        return getRelatives(block, radius, aBlock -> true);
    }

    public static List<Block> getRelativesWithoutAir(Block block, int radius) {
        return getRelatives(block, radius, aBlock -> aBlock != null && aBlock.getType() != Material.AIR);
    }

    private Locations() {
    }
}
