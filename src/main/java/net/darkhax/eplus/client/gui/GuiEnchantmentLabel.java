package net.darkhax.eplus.client.gui;

import net.darkhax.eplus.handler.ConfigurationHandler;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.enchantment.Enchantment;

public class GuiEnchantmentLabel extends Gui {
    
    private final ContainerAdvancedTable container;
    public final Enchantment enchantment;
    public final int initialLevel;
    public int currentLevel;
    
    public int startingXPos;
    public int startingYPos;
    public int xPos;
    public int yPos;
    public final int height = 18;
    private final int width = 143;
    
    private int sliderX;
    public boolean dragging = false;
    
    public boolean visible = true;
    public boolean locked = false;
    
    /**
     * Creates a new enchantment label. This is used to represent an enchantment in the GUI.
     * 
     * @param container The container used.
     * @param enchant The enchantment to represent.
     * @param level The current level of the enchantment to depict.
     * @param x The X position of the label.
     * @param y The Y position of the label.
     */
    public GuiEnchantmentLabel(ContainerAdvancedTable container, Enchantment enchant, int level, int x, int y) {
        
        this.container = container;
        this.enchantment = enchant;
        this.currentLevel = level;
        this.initialLevel = level;
        this.xPos = this.startingXPos = x;
        this.yPos = this.startingYPos = y;
        this.sliderX = this.xPos + 1;
    }
    
    /**
     * Handles the rendering of an enchantment label.
     * 
     * @param font The font renderer. Allows rendering font.
     */
    public void draw (FontRenderer font) {
        
        if (!this.visible)
            return;
        
        final int indexX = this.dragging ? this.sliderX : this.currentLevel <= this.enchantment.getMaxLevel() ? (int) (this.xPos + 1 + (this.width - 6) * (this.currentLevel / (double) this.enchantment.getMaxLevel())) : this.xPos + 1 + this.width - 6;
        
        drawRect(indexX, this.yPos + 1, indexX + 5, this.yPos - 1 + this.height, 0xff000000);
        font.drawString(this.getDisplayName(), this.xPos + 5, this.yPos + this.height / 4, 0x55aaff00);
        drawRect(this.xPos, this.yPos + 1, this.xPos + this.width, this.yPos - 1 + this.height, this.locked ? 0x44aaffff : 0x44aa55ff);
    }
    
    /**
     * Used to get the translated name of the enchantment. If the enchantment is of level 0,
     * the level bit is cut off.
     * 
     * @return The name to display for the label.
     */
    public String getDisplayName () {
        
        String name = this.enchantment.getTranslatedName(this.currentLevel);
        
        if (this.currentLevel == 0)
            if (name.lastIndexOf(" ") == -1)
                name = this.enchantment.getName();
            else
                name = name.substring(0, name.lastIndexOf(" "));
            
        return name;
    }
    
    /**
     * Updates the state of the slider. This is used to handle changing the current level of
     * the enchantment being represented.
     * 
     * @param xPos The xPos of the slider.
     * @param prevX The previous slider position.
     */
    public void updateSlider (int xPos, int prevX) {
        
        if (this.locked)
            return;
        this.sliderX = prevX + xPos;
        
        if (this.sliderX <= prevX)
            this.sliderX = prevX;
        
        if (this.sliderX >= prevX + this.width + 20)
            this.sliderX = prevX + this.width + 20;
        
        final float index = xPos / (float) (this.width + 10);
        final int tempLevel = (int) Math.floor(this.initialLevel > this.enchantment.getMaxLevel() ? this.initialLevel * index : this.enchantment.getMaxLevel() * index);
        
        if (tempLevel >= this.initialLevel || ConfigurationHandler.allowDisenchanting && !this.container.getItem().isItemDamaged())
            this.currentLevel = tempLevel;
        
        if (this.currentLevel <= 0)
            this.currentLevel = 0;
    }
    
    /**
     * Sets whether or not the label should be shown.
     * 
     * @param isVisible Whether or not the label is visible.
     */
    public void setVisible (boolean isVisible) {
        
        this.visible = isVisible;
    }
}