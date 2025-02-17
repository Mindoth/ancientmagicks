package net.mindoth.ancientmagicks.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.mindoth.ancientmagicks.AncientMagicksClient;
import net.mindoth.ancientmagicks.client.model.SimpleRobeModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class MagickArmorItem extends ArmorItem {

    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[] {
            UUID.fromString("97dabdf2-86e4-4e1f-a7a9-40ce47f6ce08"),
            UUID.fromString("1c5644db-ba8c-4ebb-a1f3-193f87acf9ef"),
            UUID.fromString("0cf2c2f2-f8cb-4437-b195-1a4e314223c8"),
            UUID.fromString("9d14a000-3e5a-45bf-8dba-5684ea315a05")
    };

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == this.type.getSlot() ? this.defaultModifiers : ImmutableMultimap.of();
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    public MagickArmorItem(AncientMagicksArmorMaterials pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        float defense = pMaterial.getDefenseForType(pType);
        float toughness = pMaterial.getToughness();
        float knockbackResistance = pMaterial.getKnockbackResistance();
        UUID uuid = ARMOR_MODIFIER_UUID_PER_SLOT[pType.getSlot().getIndex()];
        builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", defense, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", toughness, AttributeModifier.Operation.ADDITION));
        if ( knockbackResistance > 0 ) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance",
                    knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        for ( Map.Entry<Attribute, AttributeModifier> modifierEntry : pMaterial.getAdditionalAttributes().entrySet() ) {
            AttributeModifier modifier = modifierEntry.getValue();
            modifier = new AttributeModifier(uuid, modifier.getName(), modifier.getAmount(), modifier.getOperation());
            builder.put(modifierEntry.getKey(), modifier);
        }
        this.defaultModifiers = builder.build();

        this.model = DistExecutor.unsafeRunForDist(() -> () -> new LazyLoadedValue<>(this::provideArmorModelForSlot), () -> () -> null);
    }

    private final LazyLoadedValue<HumanoidModel<?>> model;

    @OnlyIn(Dist.CLIENT)
    public HumanoidModel<?> provideArmorModelForSlot() {
        return new SimpleRobeModel(Minecraft.getInstance().getEntityModels().bakeLayer(AncientMagicksClient.SIMPLE_ROBE));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return model.get();
            }
        });
    }
}
