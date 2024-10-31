package me.udnek.itemscoreu.util.NMS;

import me.udnek.itemscoreu.nms.NmsUtils;
import me.udnek.itemscoreu.util.Reflex;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import org.bukkit.craftbukkit.v1_21_R2.CraftEquipmentSlot;
import org.bukkit.inventory.EquipmentSlot;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.List;

public class NMSTest {

    public static void registerAttribute(String attributeName, double defaultValue, double minValue, double maxValue) {

        Registry<Attribute> registry = BuiltInRegistries.ATTRIBUTE;
        for (Field field : registry.getClass().getDeclaredFields()) {
            if (field.getType() == boolean.class){
                field.setAccessible(true);
                try { field.setBoolean(registry, false); } catch (IllegalAccessException e) {throw new RuntimeException(e);}
            }
        }

        Attribute attribute = (Attribute) net.minecraft.core.Registry.register(
                BuiltInRegistries.ATTRIBUTE,
                "generic." + attributeName,
                new RangedAttribute(
                        "attribute.name.generic." + attributeName,
                        defaultValue,
                        minValue,
                        maxValue
                )
        ).setSyncable(false);

        BuiltInRegistries.ATTRIBUTE.freeze();
    }

/*    public static void registerTrimPattern(){
        org.bukkit.Registry<TrimPattern> trimPattern = org.bukkit.Registry.TRIM_PATTERN;
        RegistryKey<TrimPattern> trimPattern1 = RegistryKey.TRIM_PATTERN;
        Registries.TRIM_PATTERN.registryKey();

    }*/

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath("icu", name));
    }




    public static void editEnchantment(){
        Registry<Enchantment> registry = NmsUtils.getRegistry(Registries.ENCHANTMENT);
        Enchantment enchantment;
        DataComponentMap.Builder builder;

        // EFFECT
        builder = DataComponentMap.builder();
        EnchantmentAttributeEffect armor = new EnchantmentAttributeEffect(ResourceLocation.withDefaultNamespace("enchantment.protection.armor"), Attributes.ARMOR, LevelBasedValue.perLevel(1, 1), AttributeModifier.Operation.ADD_VALUE);
        EnchantmentAttributeEffect hp = new EnchantmentAttributeEffect(ResourceLocation.withDefaultNamespace("enchantment.protection.hp"), Attributes.MAX_HEALTH, LevelBasedValue.perLevel(2, 2), AttributeModifier.Operation.ADD_VALUE);
        builder.set(EnchantmentEffectComponents.ATTRIBUTES, List.of(armor, hp));
        // ENCHANTMENT
        enchantment = registry.getValue(Enchantments.PROTECTION);
        Reflex.setRecordFieldValue(enchantment, "effects", builder.build());

                // EFFECT
         builder = DataComponentMap.builder();
        EnchantmentAttributeEffect damage = new EnchantmentAttributeEffect(ResourceLocation.withDefaultNamespace("enchantment.sharpness.damage"), Attributes.ATTACK_DAMAGE, LevelBasedValue.perLevel(1, 0.5f), AttributeModifier.Operation.ADD_VALUE);
        builder.set(EnchantmentEffectComponents.ATTRIBUTES, List.of(damage));
        // ENCHANTMENT

        enchantment = registry.getValue(Enchantments.SHARPNESS);
         Reflex.setRecordFieldValue(enchantment, "effects", builder.build());

/*        register(
                var0,
                BLAST_PROTECTION,
                Enchantment.enchantment(
                        Enchantment.definition(
                                var3.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                2, 4, Enchantment.dynamicCost(5, 8),
                                Enchantment.dynamicCost(13, 8), 4,
                                new EquipmentSlotGroup[]{EquipmentSlotGroup.ARMOR}))
                        .exclusiveWith(var2.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                        .withEffect(
                                EnchantmentEffectComponents.DAMAGE_PROTECTION,
                                new AddValue(LevelBasedValue.perLevel(2.0F)),
                                DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_EXPLOSION)).tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY)))
                        ).withEffect(
                                EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(
                                        ResourceLocation.withDefaultNamespace("enchantment.blast_protection"),
                                        Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, LevelBasedValue.perLevel(0.15F),
                                        AttributeModifier.Operation.ADD_VALUE)));*/
    }

    // TODO: 8/18/2024 WRAP
/*    public static void testEnchantment(){
        
        Registry<Enchantment> registry = NmsUtils.getRegistry(Registries.ENCHANTMENT);
        // unfreeze
        Reflex.setFieldValue(registry, "frozen", false);
        Reflex.setFieldValue(registry, "unregisteredIntrusiveHolders", new IdentityHashMap<>());
        // resource key
        String enchantId = "test";
        ResourceKey<Enchantment> key = key(enchantId);
        // properties
        Component description = Component.literal("test desc");
        HolderSet<Enchantment> exclusiveSet = HolderSet.direct();
        DataComponentMap effects = DataComponentMap.builder().build();

        Registry<Item> items = NmsUtils.getRegistry(Registries.ITEM);
        HolderSet.Named<Item> supportedItems = items.getTag(ItemTags.SWORD_ENCHANTABLE).orElse(null);
        HolderSet.Named<Item> primaryItems = items.getTag(ItemTags.SWORD_ENCHANTABLE).orElse(null);

        EquipmentSlotGroup[] slots = nmsSlots(new EquipmentSlot[]{ EquipmentSlot.HAND});

        // definition
        int weight = 1;
        int maxLevel = 5;
        Enchantment.Cost minCost = new Enchantment.Cost(1, 11);
        Enchantment.Cost maxCost = new Enchantment.Cost(12, 11);
        int anvilCost = 2;

        Enchantment.EnchantmentDefinition definition = Enchantment.definition(supportedItems, primaryItems, weight, maxLevel, minCost, maxCost, anvilCost, slots);
        Enchantment enchantment = new Enchantment(description, definition, exclusiveSet, effects);
        Holder.Reference<Enchantment> reference = registry.createIntrusiveHolder(enchantment);
        Registry.register(registry, key, enchantment);

        registry.freeze();
    }*/

    public static net.minecraft.world.entity.EquipmentSlotGroup[] nmsSlots(EquipmentSlot[] slots) {
        net.minecraft.world.entity.EquipmentSlotGroup[] nmsSlots = new net.minecraft.world.entity.EquipmentSlotGroup[slots.length];

        for (int index = 0; index < nmsSlots.length; index++) {
            org.bukkit.inventory.EquipmentSlot bukkitSlot = slots[index];
            nmsSlots[index] = CraftEquipmentSlot.getNMSGroup(bukkitSlot.getGroup());
        }

        return nmsSlots;
    }


    // TODO: 8/18/2024 WRAP
    public static MobEffect registerEffect(){


        Reflex.setFieldValue(BuiltInRegistries.MOB_EFFECT, "frozen", false);

        MobEffect mobEffect;
        try {
            Constructor<MobEffect> constructor = MobEffect.class.getDeclaredConstructor(MobEffectCategory.class, int.class, ParticleOptions.class);
            constructor.setAccessible(true);
            mobEffect = constructor.newInstance(MobEffectCategory.BENEFICIAL, new Color(1f, 0, 0).getRGB(), ParticleTypes.CAMPFIRE_COSY_SMOKE)
                    .addAttributeModifier(Attributes.SCALE, ResourceLocation.fromNamespaceAndPath("icu", "test"), 1, AttributeModifier.Operation.ADD_VALUE);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath("icu", "test"), mobEffect);
        BuiltInRegistries.MOB_EFFECT.freeze();

        return mobEffect;
    }


    public static void registerItem(){
        final DefaultedRegistry<Item> registry = BuiltInRegistries.ITEM;
        Reflex.setFieldValue(registry, "frozen", false);
        Reflex.setFieldValue(registry, "unregisteredIntrusiveHolders", new IdentityHashMap<>());

        Item item = new Item(new Item.Properties());
        registry.createIntrusiveHolder(item);

        Method method = Reflex.getMethod(Items.class, "registerItem", String.class, Item.class);
        Reflex.invokeMethod(null, method, "test", item);

        //Registry.register(BuiltInRegistries.ITEM,ResourceKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath("icu", "test")), new Item(new Item.Properties()));
        registry.freeze();
    }

    public static void editItem(){
        final MappedRegistry<Item> registry = (DefaultedMappedRegistry<Item>) BuiltInRegistries.ITEM;

        Item item = Items.FIREWORK_ROCKET;
        Item.Properties properties = new Item.Properties();
        properties.stacksTo(5);
        Method method = Reflex.getMethod(Item.Properties.class, "buildAndValidateComponents");
        DataComponentMap dataComponentMap = (DataComponentMap) Reflex.invokeMethod(properties, method);
        Reflex.setFieldValue(item, "components", dataComponentMap);
/*        ResourceLocation resourceLocation = registry.getKey(item);
        Map<ResourceLocation, Holder.Reference<Item>> byLocation = (Map<ResourceLocation, Holder.Reference<Item>>) Reflex.getFieldValue(registry, "byLocation");
        Holder.Reference<Item> holder = byLocation.get(resourceLocation);
        Reflex.setFieldValue(holder, "value", item);*/
/*        Method method = Reflex.getMethod(Holder.Reference.class, "bindValue", Object.class);
        Reflex.invokeMethod(holder, method, item);*/

        
/*        final DefaultedRegistry<Item> registry = BuiltInRegistries.ITEM;
        Reflex.setFieldValue(registry, "frozen", false);
        Reflex.setFieldValue(registry, "unregisteredIntrusiveHolders", new IdentityHashMap<>());

        Item item = new InstrumentItem((new Item.Properties()).stacksTo(1), InstrumentTags.GOAT_HORNS);
        registry.createIntrusiveHolder(item);

        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey(Items.FIREWORK_ROCKET);

        Method method = Reflex.getMethod(Items.class, "registerItem", String.class, Item.class);
        Reflex.invokeMethod(null, method, "firework_rocket", item);

        registry.freeze();*/
    }


/*    public static ServerPlayer getPlayer(Player player){
        return ((CraftPlayer) player).getHandle();
    }

    public static void sendPacket(Player player, Packet<ClientGamePacketListener> packet){
        getPlayer(player).connection.send(packet);
    }

    public static net.minecraft.world.entity.Entity getEntity(Entity entity){
        return ((CraftEntity) entity).getHandle();
    }

    public static ServerLevel getServerLevel(Location location){
        return getServerLevel(location.getWorld());
    }
    public static ServerLevel getServerLevel(World world){
        return ((CraftWorld) world).getHandle();
    }

    public static BlockPos getBlockPos(Location location){
        return new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }


    public static MapColor getMapColorOfMaterial(Material material){
        Block block = CraftMagicNumbers.getBlock(material);
        MapColor mapColor = block.defaultMapColor();
        return mapColor;
    }

    public static int getColorOfMaterial(Material material){
        return getMapColorOfMaterial(material).col;
    }

    public static int getColorOnMap(Location location){
        BlockPos blockPos = getBlockPos(location);
        ServerLevel serverLevel = getServerLevel(location);
        BlockState blockState = serverLevel.getBlockState(blockPos);
        MapColor mapColor = blockState.getMapColor(serverLevel, blockPos);
        return mapColor.col;
    }


    public static int getGrassColor(Location location){
        ServerLevel serverLevel = getServerLevel(location.getWorld());
        Holder<Biome> biome = serverLevel.getBiome(new BlockPos((int) location.x(), (int) location.y(), (int) location.z()));
        int grassColor = biome.value().getGrassColor(0, 0);
        return grassColor;
    }

    public static void printFeatures(Location location){
        ServerLevel serverLevel = getServerLevel(location);
        Biome biome = serverLevel.getBiome(new BlockPos((int) location.x(), (int) location.y(), (int) location.z())).value();
        for (HolderSet<PlacedFeature> placedFeatureHolderSet : biome.getGenerationSettings().features()){
            for (Holder<PlacedFeature> placedFeatureHolder : placedFeatureHolderSet) {
                Feature<?> feature = placedFeatureHolder.value().feature().value().feature();
            }
        }
    }


    public static void startRiptiding(Player player, int duration){
        ServerPlayer serverPlayer = getPlayer(player);
        serverPlayer.startAutoSpinAttack(duration);
    }


    public static void sendExplosionPacket(Player player){
        ClientboundExplodePacket explodePacket = new ClientboundExplodePacket(
                player.getLocation().x(),
                player.getLocation().y(),
                player.getLocation().z(),
                5f,
                new ArrayList<BlockPos>(),
                new Vec3(0, 1, 0)
        );

        sendPacket(player, explodePacket);
    }

    public static void setCameraEntity(Player player, Entity entity){
        ClientboundSetCameraPacket cameraPacket = new ClientboundSetCameraPacket(
            getEntity(entity)
        );
        sendPacket(player, cameraPacket);

    }

    public static void sendChunkBiomeUpdate(Player player, Chunk chunk){
        ClientboundChunksBiomesPacket.ChunkBiomeData chunkBiomeData = new ClientboundChunksBiomesPacket.ChunkBiomeData(
                new ChunkPos(chunk.getX(), chunk.getZ()),
                new byte[]{0, 0, 0}
        );
        ClientboundChunksBiomesPacket biomesPacket = new ClientboundChunksBiomesPacket(
                new ArrayList<>(Collections.singleton(chunkBiomeData))
        );
        sendPacket(player, biomesPacket);
    }


    public static void getAllFeatures(Location location){
        ServerLevel serverLevel = getServerLevel(location);
        Biome biome = serverLevel.getBiome(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())).value();
        BiomeGenerationSettings generationSettings = biome.getGenerationSettings();
        Iterator<Structure> structureIterator = Registry.STRUCTURE.iterator();


        while (structureIterator.hasNext()){
            Structure structure = structureIterator.next();
            Bukkit.getLogger().info(structure.getKey().asString());

        }

    }

    public static Attribute TEST_ATTRIBUTES(){

*//*
        for (Field field : BuiltInRegistries.ATTRIBUTE.getClass().getDeclaredFields()) {
            Bukkit.getLogger().info(field.toString());
        }
*//*

        try {
            Field field = BuiltInRegistries.ATTRIBUTE.getClass().getDeclaredField("l");
            field.setAccessible(true);
            field.set(BuiltInRegistries.ATTRIBUTE, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        Attribute attribute = (Attribute) net.minecraft.core.Registry.register(
                BuiltInRegistries.ATTRIBUTE,
                "generic.test",
                new RangedAttribute(
                        "attribute.name.generic.test",
                        0,
                        0,
                        1024
                )
        );

        BuiltInRegistries.ATTRIBUTE.freeze();

        for (Map.Entry<ResourceKey<Attribute>, Attribute> entry : BuiltInRegistries.ATTRIBUTE.entrySet()) {
            Bukkit.getLogger().info(entry.getValue().getDescriptionId());
        }


        Bukkit.getLogger().info(BuiltInRegistries.ATTRIBUTE.getOptional(ResourceLocation.tryParse("generic.max_health")).get().getDescriptionId());
        Bukkit.getLogger().info(BuiltInRegistries.ATTRIBUTE.getOptional(ResourceLocation.tryParse("generic.test")).get().getDescriptionId());

        ItemStack itemStack = new ItemStack(Items.ACACIA_BOAT);
        itemStack.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), "asd", 4, AttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);

        return attribute;

    }


    public static boolean registerEntity() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {


        CraftServer server = ((CraftServer) Bukkit.getServer());
        DedicatedServer dedicatedServer = server.getServer();
        WritableRegistry<EntityType<?>> entityTypeRegistry = (WritableRegistry<EntityType<?>>)
                dedicatedServer.registryAccess().registryOrThrow(Registries.ENTITY_TYPE);

// Unfreeze registry
        Bukkit.getLogger().info("Unfreezing entity type registry (1/2)...");
// l = private boolean frozen
        Field frozen = MappedRegistry.class.getDeclaredField("l");
        frozen.setAccessible(true);
        frozen.set(entityTypeRegistry, false);

        Bukkit.getLogger().info("Unfreezing entity type registry (2/2)...");
// m = private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
        Field unregisteredHolderMap = MappedRegistry.class.getDeclaredField("m");
        unregisteredHolderMap.setAccessible(true);
        unregisteredHolderMap.set(BuiltInRegistries.ENTITY_TYPE, new HashMap<>());

// Build entity
        EntityType.Builder<net.minecraft.world.entity.Entity> builder = EntityType.Builder.of(TestEntity::new, MobCategory.MISC)
                .sized(0F, 0F)
                .clientTrackingRange(1);

        Bukkit.getLogger().info("Building base villager");
        EntityType<net.minecraft.world.entity.Entity> villager = builder.build("base_villager");

// Create intrusive holder
        entityTypeRegistry.createIntrusiveHolder(villager);

// Register custom entity
        ResourceKey<EntityType<?>> newKey = ResourceKey.create(Registries.ENTITY_TYPE, new ResourceLocation("settlements", "base_villager"));
        entityTypeRegistry.register(newKey, villager, Lifecycle.stable());

// a = private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder builder)
        Method register = EntityType.class.getDeclaredMethod("a", String.class, EntityType.Builder.class);
        register.setAccessible(true);
        register.invoke(null, "base_villager", builder);


        Bukkit.getLogger().info("Re-freezing entity type registry...");
        frozen.set(entityTypeRegistry, true);
        unregisteredHolderMap.set(BuiltInRegistries.ENTITY_TYPE, null);


*//*        EntityType.Builder<net.minecraft.world.entity.Entity> entityBuilder = EntityType.Builder.of(Zoglin::new, MobCategory.AMBIENT)
                .sized(1f, 1f).clientTrackingRange(8).updateInterval(10);
        net.minecraft.core.Registry.register(BuiltInRegistries.ENTITY_TYPE, "test", entityBuilder.build("test"));
        return true;*//*


        return true;
    }

    public static void sendEffectPacket(Player player){

        MobEffect[] effects = new MobEffect[]{
                ItemsCoreU.testEffect,
                MobEffects.CONFUSION,
                MobEffects.DIG_SPEED
        };

        for (MobEffect effect : effects) {
            ClientboundUpdateMobEffectPacket packet = new ClientboundUpdateMobEffectPacket(
                    getPlayer(player).getId(),
                    new MobEffectInstance(effect, 60, 3, false, true, true)
            );
            sendPacket(player, packet);
        }


    }

    public static MobEffect registerEffect(){

        try {
            Field field = BuiltInRegistries.MOB_EFFECT.getClass().getDeclaredField("l");
            field.setAccessible(true);
            field.set(BuiltInRegistries.MOB_EFFECT, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }


*//*        for (Field field : PotionEffectType.class.getDeclaredFields()) {
            Bukkit.getLogger().info(field.toString());
        }*//*



        MobEffect mobEffect = (MobEffect) net.minecraft.core.Registry.registerMapping(BuiltInRegistries.MOB_EFFECT, 34, "test", MobEffects.ABSORPTION);
        //PotionEffectType.registerPotionEffectType(new CraftPotionEffectType(mobEffect));

        BuiltInRegistries.MOB_EFFECT.freeze();

        return mobEffect;
    }





/*        List<HolderSet<PlacedFeature>> holderSetListFeatures = generationSettings.features();
        for (HolderSet<PlacedFeature> holderSetFeature : holderSetListFeatures) {
            for (Holder<PlacedFeature> placedFeatureHolder : holderSetFeature) {
                PlacedFeature placedFeature = placedFeatureHolder.value();
                Iterator<ConfiguredFeature<?, ?>> iterator = placedFeature.getFeatures().iterator();
                while (iterator.hasNext()){
                    ConfiguredFeature<?, ?> configuredFeature = iterator.next();
                    Feature<?> feature = configuredFeature.feature();
                    Bukkit.getLogger().info(feature.getClass().getSimpleName());

                }
            }
        }*/

/*    public static void locateNearestStructure(Location location, Structure structure, int radius, boolean findUnexplored){
        LocateCommand;
        ServerLevel serverLevel = getServerLevel(location.getWorld());
        HolderSet<net.minecraft.world.level.levelgen.structure.Structure> holderSet =
                HolderSet.direct(Holder.direct(
                        (structure.getStructureType())
                        ));

        Pair nearestMapStructure = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(
                serverLevel,
                holderSet,
                getBlockPos(location),
                radius,
                findUnexplored);

        Items
    }*/
/*    public static int getSpecialEffects(Location location){
        ServerLevel serverLevel = getServerLevel(location.getWorld());
        Biome biome = serverLevel.getBiome(new BlockPos((int) location.x(), (int) location.y(), (int) location.z())).value();
        biome.get
        return grassColor;
    }*/
}