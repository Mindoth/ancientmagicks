package net.mindoth.ancientmagicks.client.screen;

import com.google.common.collect.Lists;
import net.mindoth.ancientmagicks.AncientMagicks;
import net.mindoth.ancientmagicks.item.SpellItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class SpellScreen extends AncientMagicksScreen {

    private final ItemStack stack;
    private final List<ItemStack> itemList;

    private final int arrowYOffset = 77;
    private Button leftArrow;
    private final int leftArrowXOffset = -18 - 120;

    protected SpellScreen(ItemStack stack, List<ItemStack> itemList) {
        super(Component.literal(""));
        this.stack = stack;
        this.itemList = itemList;
    }

    public static void open(ItemStack stack, List<ItemStack> itemList) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        if ( MINECRAFT.screen instanceof SpellBookScreen && stack != ItemStack.EMPTY ) MINECRAFT.setScreen(new SpellScreen(stack, itemList));
    }

    @Override
    protected void init() {
        super.init();

        int x = minecraft.getWindow().getGuiScaledWidth() / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2;

        this.leftArrow = addRenderableWidget(Button.builder(Component.literal(""), this::handleBackArrow)
                .bounds(x + this.leftArrowXOffset, y + this.arrowYOffset, 18, 10)
                .build());
    }

    private void handleBackArrow(Button button) {
        SpellBookScreen.open(this.itemList);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);

        int x = minecraft.getWindow().getGuiScaledWidth() / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() / 2;

        //Background
        renderBackground(graphics);
        drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/spell_book_screen.png"),
                x - 140, y - 90, 0, 0, 280, 180, 280, 180, graphics);

        //Back arrow
        if ( this.leftArrow.visible ) this.leftArrow.renderTexture(graphics, new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/page_arrows.png"),
                x + this.leftArrowXOffset, y + this.arrowYOffset, 18, 0, 10, 18, 10, 36, 20);

        ItemStack stack = getPossibleContainedSpell(this.stack);
        if ( stack.getItem() instanceof SpellItem spell ) {

            int xPos = x - 100;
            int yPos = y - 32 + this.font.lineHeight;

            //Square
            drawTexture(new ResourceLocation(AncientMagicks.MOD_ID, "textures/gui/square.png"),
                    xPos - 12, yPos - 12, 0, 0, 88, 88, 88, 88, graphics);


            //String parsing
            String id = spell.toString();
            String modId = ForgeRegistries.ITEMS.getKey(spell).toString().split(":")[0];

            //Spell name
            Component name = Component.translatable("item." + modId + "." + id).setStyle(Style.EMPTY.withBold(true));
            graphics.drawString(this.font, name, xPos + 32 - (this.font.width(name) / 2), yPos - 24 - this.font.lineHeight, 0, false);

            //Spell icon
            AncientMagicksScreen.drawTexture(new ResourceLocation(modId, "textures/item/spell/" + id + ".png"),
                    xPos, yPos, 0, 0, 64, 64, 64, 64, graphics);


            //Info text
            int xInfo = x + 10;
            int yInfo = y - 72;
            List<Component> componentList = Lists.newArrayList();

            //Title
            Component title = Component.translatable("tooltip.ancientmagicks.stats").setStyle(Style.EMPTY.withBold(true));
            componentList.add(title);

            //Tier
            Component tier = Component.translatable("tooltip.ancientmagicks.tier")
                    .append(String.valueOf(spell.spellTier));
            componentList.add(tier);

            //Mana cost
            Component manaCost;
            if ( spell.isChannel() ) {
                manaCost = Component.translatable("tooltip.ancientmagicks.mana_cost")
                        .append(String.valueOf(spell.manaCost * 2)).append(Component.literal("/s"));
            }
            else {
                manaCost = Component.translatable("tooltip.ancientmagicks.mana_cost")
                        .append(String.valueOf(spell.manaCost));
            }
            componentList.add(manaCost);

            //Cooldown
            Component cooldown = Component.translatable("tooltip.ancientmagicks.cooldown")
                    .append(spell.cooldown / 20 + "s");
            componentList.add(cooldown);

            //Description
            Component descriptionTitle = Component.translatable("tooltip.ancientmagicks.description").setStyle(Style.EMPTY.withBold(true));
            componentList.add(descriptionTitle);
            String spellDesc = Component.translatable("tooltip." + modId + "." + spell).getString();
            int charLimit = 20;
            if ( spellDesc.length() < charLimit ) {
                Component desc = Component.literal(spellDesc);
                componentList.add(desc);
            }
            else {
                List<String> lines = putTextToLines(spellDesc, charLimit);
                lines.forEach(s -> componentList.add(Component.literal(s)));
            }

            for ( int i = 0; i < componentList.size(); i++ ) {
                graphics.drawString(this.font, componentList.get(i), xInfo, yInfo + (this.font.lineHeight * i), 0, false);
            }
        }
    }

    private static @NotNull List<String> putTextToLines(String spellDesc, int charLimit) {
        List<String> words = Arrays.stream(spellDesc.split(" ")).toList();
        List<String> lines = Lists.newArrayList();
        StringBuilder desc = new StringBuilder();
        for ( int i = 0; i < words.size(); i++ ) {
            boolean isLast = i == words.size() - 1;
            String word = words.get(i);
            String tempString = desc.isEmpty() ? word : " " + word;
            boolean isOverLimit = (desc + tempString).length() >= charLimit;
            if ( isOverLimit ) {
                lines.add(desc.toString());
                desc.setLength(0);
                desc.append(word);
            }
            else desc.append(tempString);
            if ( isLast ) lines.add(desc.toString());
        }
        return lines;
    }
}
