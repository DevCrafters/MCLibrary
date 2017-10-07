package kr.rvs.mclibrary.bukkit.location;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * Created by Junhyeong Lim on 2017-10-07.
 */
public class Region {
    private final String world;
    private final Vector min;
    private final Vector max;

    public Region(Location pointA, Location pointB) {
        Validate.isTrue(pointA.getWorld().equals(pointB.getWorld()), "Two worlds are different.");

        double minX = Math.min(pointA.getX(), pointB.getX());
        double maxX = Math.max(pointA.getX(), pointB.getX());
        double minY = Math.min(pointA.getY(), pointB.getY());
        double maxY = Math.max(pointA.getY(), pointB.getY());
        double minZ = Math.min(pointA.getZ(), pointB.getZ());
        double maxZ = Math.max(pointA.getZ(), pointB.getZ());

        pointA.setX(minX);
        pointA.setY(minY);
        pointA.setZ(minZ);
        pointB.setX(maxX);
        pointB.setY(maxY);
        pointB.setZ(maxZ);

        this.world = pointA.getWorld().getName();
        this.min = pointA.toVector();
        this.max = pointB.toVector();
    }

    public boolean isIn(Location location) {
        World world = location.getWorld();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return world.getName().equals(this.world)
                && min.getX() <= x && max.getX() >= x
                && min.getY() <= y && max.getY() >= y
                && min.getZ() <= z && max.getZ() >= z;
    }

    public boolean isIn(Entity entity) {
        return isIn(entity.getLocation());
    }
}
