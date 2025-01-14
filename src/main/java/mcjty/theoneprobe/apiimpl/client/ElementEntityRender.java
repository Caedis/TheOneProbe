package mcjty.theoneprobe.apiimpl.client;

import mcjty.theoneprobe.api.IEntityStyle;
import mcjty.theoneprobe.rendering.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.EntityId;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ElementEntityRender {

    private static final EntityId FIXER = new EntityId();

    public static void renderPlayer(@Nonnull String entityName, @Nonnull Integer playerID, @Nonnull IEntityStyle style, int x, int y) {
        Entity entity = Minecraft.getMinecraft().world.getEntityByID(playerID);
        if (entity != null) renderEntity(style, x, y, entity);
    }

    public static void render(@Nonnull String entityName, @Nullable NBTTagCompound entityNBT, @Nonnull IEntityStyle style, int x, int y) {
        if (!entityName.isEmpty()) {
            Entity entity = null;
            if (entityNBT != null) {
                entity = EntityList.createEntityFromNBT(entityNBT, Minecraft.getMinecraft().world);
            } else {
                EntityEntry value = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(fixEntityId(entityName)));
                if (value != null) entity = value.newInstance(Minecraft.getMinecraft().world);
            }
            if (entity != null) renderEntity(style, x, y, entity);
        }
    }

    /**
     * This method attempts to fix an old-style (1.10.2) entity Id and convert it to the
     * string representation of the new ResourceLocation. The 1.10 version of this function will just return
     * the given id
     * This does not work for modded entities.
     * @param id an old-style entity id as used in 1.10
     * @return the fixed entity ID
     */
    @Nonnull
    public static String fixEntityId(@Nonnull String id) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("id", id);
        nbt = FIXER.fixTagCompound(nbt);
        return nbt.getString("id");
    }

    private static void renderEntity(@Nonnull IEntityStyle style, int x, int y, @Nonnull Entity entity) {
        float height = entity.height;
        height = (float) ((height - 1) * .7 + 1);
        float s = style.getScale() * ((style.getHeight() * 14.0f / 25) / height);

        RenderHelper.renderEntity(entity, x, y, s);
    }

}
