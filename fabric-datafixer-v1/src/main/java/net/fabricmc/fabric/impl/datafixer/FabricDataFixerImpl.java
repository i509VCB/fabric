package net.fabricmc.fabric.impl.datafixer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.Dynamic;

import net.fabricmc.fabric.api.datafixer.DataFixerUtils;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.datafixers.DataFixTypes;
import net.minecraft.datafixers.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public final class FabricDataFixerImpl implements DataFixerUtils {

    private final Map<String, DataFixerEntry> modFixers = new HashMap<>();
    private boolean locked;

    private FabricDataFixerImpl() {
    }

    public static final FabricDataFixerImpl INSTANCE = new FabricDataFixerImpl();

    public void addFixerVersions(CompoundTag compoundTag) {
        for (Entry<String, DataFixerEntry> entry : modFixers.entrySet()) {
            compoundTag.putInt(entry.getKey() + "_DataVersion", entry.getValue().runtimeDataVersion);
        }
    }

    @Override
    public int getModDataVersion(CompoundTag compoundTag, String modid) {
        return compoundTag.containsKey(modid + "_DataVersion", NbtType.NUMBER) ? compoundTag.getInt(modid + "_DataVersion") : 0;
    }

    @Override
    public Optional<DataFixer> getDataFixer(String modid) {
        return Optional.ofNullable(modFixers.get(modid).modFixer);
    }

    @Override
    public DataFixer registerFixer(String modid, int runtimeDataVersion, DataFixer datafixer) {
        Preconditions.checkNotNull(modid, "modid cannot be null");
        Preconditions.checkArgument(runtimeDataVersion > -1, "dataVersion must be finite");

        modFixers.put(modid, new DataFixerEntry(datafixer, runtimeDataVersion));

        return datafixer;
    }

    public CompoundTag updateWithAllFixers(DataFixTypes dataFixTypes, CompoundTag compoundTag) {
        
        CompoundTag currentTag = compoundTag;

        for (Entry<String, DataFixerEntry> entry : modFixers.entrySet()) {
            String currentModid = entry.getKey();
            int modidCurrentDynamicVersion = getModDataVersion(compoundTag, currentModid);
            DataFixerEntry dataFixerEntry = entry.getValue();
            
            currentTag = (CompoundTag) dataFixerEntry.modFixer.update(dataFixTypes.getTypeReference(), new Dynamic<Tag>(NbtOps.INSTANCE, currentTag), modidCurrentDynamicVersion, dataFixerEntry.runtimeDataVersion).getValue();
        }
        return currentTag;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    /**
     * @deprecated for implementation only.
     */
    public void lock(boolean toggle) {
        this.locked = toggle;
    }

    final class DataFixerEntry {
        private DataFixer modFixer;
        private int runtimeDataVersion;

        DataFixerEntry(DataFixer fix, int runtimeDataVersion) {
            this.modFixer = fix;
            this.runtimeDataVersion = runtimeDataVersion;
        }
    }
}
