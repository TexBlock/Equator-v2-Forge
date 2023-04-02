package net.krlite.equator.test;

import net.krlite.equator.math.ScreenAdapter;
import net.krlite.equator.util.InputEvents;
import net.krlite.equator.visual.animation.Interpolation;
import net.krlite.equator.visual.color.AccurateColor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class CanvasScreen extends Screen {
	public CanvasScreen() {
		super(Text.of("Canvas"));
	}

	private final Interpolation interpolation = new Interpolation(0, 1);

	{
		Interpolation.InterpolationCallbacks.Complete.EVENT.register(i -> i.targetValue(1 - i.targetValue()));

		InputEvents.InputCallbacks.Mouse.EVENT.register((event, button, mods) -> {
			if (event == InputEvents.InputEvent.MOUSE_PRESSED)
				interpolation.switchPauseResume();
		});
	}

	@Override
	protected void init() {
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
		renderBackground(matrixStack);

		//System.out.println("Interpolation value: " + interpolation.value() + ", " + interpolation.targetValue());
		ScreenAdapter.scaledScreen().scale(interpolation.value()).ready(AccurateColor.CYAN).render(matrixStack);
	}
}
