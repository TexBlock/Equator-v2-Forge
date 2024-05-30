package net.krlite.equator.input;

import net.krlite.equator.Equator;
import net.krlite.equator.math.geometry.flat.Box;
import net.krlite.equator.math.geometry.flat.Vector;
import net.minecraft.client.MinecraftClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.glfw.*;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <h1>Window</h1>
 * Provides access to the window's properties and callbacks.
 * @see Callbacks
 * @see GLFW
 */
public class Window {
	/**
	 * Callbacks for window events.
	 * @see Window
	 */
	public static abstract class Callbacks extends Event {
		@ApiStatus.Internal
		protected Callbacks() {}
		/**
		 * Callback for the {@link Window} close event.
		 */
		public static class Close extends Callbacks {
			/**
			 * Called when the window is closed.
			 */
			@ApiStatus.Internal
			public Close() {}
		}

		/**
		 * Callback for the {@link Window} iconify event.
		 */
		public static class Iconify extends Callbacks {
			private final boolean iconified;

			/**
			 * Called when the window is iconified.
			 * @param iconified	{@code true} if the window is iconified, {@code false} otherwise.
			 */
			@ApiStatus.Internal
			public Iconify(boolean iconified) {
				this.iconified = iconified;
			}

			public boolean isIconified() {
				return this.iconified;
			}
		}

		/**
		 * Callback for the {@link Window} maximize event.
		 */
		public static class Maximize extends Callbacks {
			private final boolean maximized;

			/**
			 * Called when the window is maximized.
			 * @param maximized	{@code true} if the window is maximized, {@code false} otherwise.
			 */
			@ApiStatus.Internal
			public Maximize(boolean maximized) {
				this.maximized = maximized;
			}

			public boolean isMaximized() {
				return this.maximized;
			}
		}

		/**
		 * Callback for the {@link Window} focus event.
		 */
		public static class Focus extends Callbacks {
			private final boolean focused;

			/**
			 * Called when the window is focused.
			 * @param focused	{@code true} if the window is focused, {@code false} otherwise.
			 */
			@ApiStatus.Internal
			public Focus(boolean focused) {
				this.focused = focused;
			}

			public boolean isFocused() {
				return this.focused;
			}
		}

		/**
		 * Callback for the {@link Window} move event, while the {@code position} is in the
		 * {@link net.krlite.equator.render.frame.FrameInfo.Convertor Scaled Coordinate}.
		 * @see net.krlite.equator.render.frame.FrameInfo.Convertor
		 */
		public static class Move extends Callbacks {
			private final Vector position;

			/**
			 * Called when the window is moved.
			 * @param position	the new position of the window, in the
			 * 					{@link net.krlite.equator.render.frame.FrameInfo.Convertor Scaled Coordinate}.
			 * @see net.krlite.equator.render.frame.FrameInfo.Convertor
			 */
			@ApiStatus.Internal
			public Move(Vector position) {
				this.position = position;
			}

			public Vector getPosition() {
				return this.position;
			}
		}

		/**
		 * Callback for the {@link Window} resize event, while the {@code window} is in the
		 * {@link net.krlite.equator.render.frame.FrameInfo.Convertor Scaled Coordinate}.
		 * @see net.krlite.equator.render.frame.FrameInfo.Convertor
		 */
		public static class Resize extends Callbacks {
			private final Box window;

			/**
			 * Called when the window is resized.
			 * @param window	The new size of the window.
			 */
			@ApiStatus.Internal
			public Resize(Box window) {
				this.window = window;
			}

			public Box getWindow() {
				return this.window;
			}
		}

		/**
		 * Callback for the {@link Window} content scale event.
		 */
		public static class ContentScale extends Callbacks {
			private final float xScaling;
			private final float yScaling;

			/**
			 * Called when the window's content scale is changed.
			 * @param xScaling	the new {@code x-scaling} of the window.
			 * @param yScaling	the new {@code y-scaling} of the window.
			 */
			@ApiStatus.Internal
			public ContentScale(float xScaling, float yScaling) {
				this.xScaling = xScaling;
				this.yScaling = yScaling;
			}

			public float getxScaling() {
				return this.xScaling;
			}

			public float getyScaling() {
				return this.yScaling;
			}
		}
	}

	public static boolean isVisible() {
		return GLFW.glfwGetWindowAttrib(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_VISIBLE) == GLFW.GLFW_TRUE;
	}

	public static boolean isIconified() {
		return GLFW.glfwGetWindowAttrib(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE;
	}

	public static boolean isMaximized() {
		return GLFW.glfwGetWindowAttrib(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_MAXIMIZED) == GLFW.GLFW_TRUE;
	}

	public static boolean isFocused() {
		return GLFW.glfwGetWindowAttrib(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
	}

	public static boolean isHovered() {
		return GLFW.glfwGetWindowAttrib(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_HOVERED) == GLFW.GLFW_TRUE;
	}

	private static final AtomicBoolean initialized = new AtomicBoolean(false);

	public static void initCallbacks(long window) {
		if (!initialized.compareAndSet(false, true)) {
			Equator.LOGGER.warn("Window callbacks have already been initialized!");
			return;
		}
		initCloseCallback(window);
		initIconifyCallback(window);
		initMaximizeCallback(window);
		initFocusCallback(window);
		initMoveCallback(window);
		initResizeCallback(window);
		initContentScaleCallback(window);
	}

	static void initCloseCallback(long window) {
		GLFWWindowCloseCallback closeCallback = new GLFWWindowCloseCallback() {
			private final GLFWWindowCloseCallbackI delegate = GLFW.glfwSetWindowCloseCallback(window, null);

			@Override
			public void invoke(long window) {
				MinecraftForge.EVENT_BUS.post(new Callbacks.Close());

				if (delegate != null) {
					delegate.invoke(window);
				}
			}
		};

		GLFW.glfwSetWindowCloseCallback(window, closeCallback);
	}

	static void initIconifyCallback(long window) {
		GLFWWindowIconifyCallback iconifyCallback = new GLFWWindowIconifyCallback() {
			private final GLFWWindowIconifyCallbackI delegate = GLFW.glfwSetWindowIconifyCallback(window, null);

			@Override
			public void invoke(long window, boolean iconified) {
				MinecraftForge.EVENT_BUS.post(new Callbacks.Iconify(iconified));

				if (delegate != null) {
					delegate.invoke(window, iconified);
				}
			}
		};

		GLFW.glfwSetWindowIconifyCallback(window, iconifyCallback);
	}

	static void initMaximizeCallback(long window) {
		GLFWWindowMaximizeCallback maximizeCallback = new GLFWWindowMaximizeCallback() {
			private final GLFWWindowMaximizeCallbackI delegate = GLFW.glfwSetWindowMaximizeCallback(window, null);

			@Override
			public void invoke(long window, boolean maximized) {
				MinecraftForge.EVENT_BUS.post(new Callbacks.Maximize(maximized));

				if (delegate != null) {
					delegate.invoke(window, maximized);
				}
			}
		};

		GLFW.glfwSetWindowMaximizeCallback(window, maximizeCallback);
	}

	static void initFocusCallback(long window) {
		GLFWWindowFocusCallback focusCallback = new GLFWWindowFocusCallback() {
			private final GLFWWindowFocusCallbackI delegate = GLFW.glfwSetWindowFocusCallback(window, null);

			@Override
			public void invoke(long window, boolean focused) {
				MinecraftForge.EVENT_BUS.post(new Callbacks.Focus(focused));

				if (delegate != null) {
					delegate.invoke(window, focused);
				}
			}
		};

		GLFW.glfwSetWindowFocusCallback(window, focusCallback);
	}

	static void initMoveCallback(long window) {
		GLFWWindowPosCallback moveCallback = new GLFWWindowPosCallback() {
			private final GLFWWindowPosCallbackI delegate = GLFW.glfwSetWindowPosCallback(window, null);

			@Override
			public void invoke(long window, int x, int y) {
				MinecraftForge.EVENT_BUS.post(new Callbacks.Move(Vector.fromCartesian(x, y).fitFromScreen()));

				if (delegate != null) {
					delegate.invoke(window, x, y);
				}
			}
		};

		GLFW.glfwSetWindowPosCallback(window, moveCallback);
	}

	static void initResizeCallback(long window) {
		GLFWWindowSizeCallback resizeCallback = new GLFWWindowSizeCallback() {
			private final GLFWWindowSizeCallbackI delegate = GLFW.glfwSetWindowSizeCallback(window, null);

			@Override
			public void invoke(long window, int width, int height) {
				MinecraftForge.EVENT_BUS.post(new Callbacks.Resize(Box.fromCartesian(width, height).fitFromScreen()));

				if (delegate != null) {
					delegate.invoke(window, width, height);
				}
			}
		};

		GLFW.glfwSetWindowSizeCallback(window, resizeCallback);
	}

	static void initContentScaleCallback(long window) {
		GLFWWindowContentScaleCallback contentScaleCallback = new GLFWWindowContentScaleCallback() {
			private final GLFWWindowContentScaleCallbackI delegate = GLFW.glfwSetWindowContentScaleCallback(window, null);

			@Override
			public void invoke(long window, float xScaling, float yScaling) {
				MinecraftForge.EVENT_BUS.post(new Callbacks.ContentScale(xScaling, yScaling));

				if (delegate != null) {
					delegate.invoke(window, xScaling, yScaling);
				}
			}
		};

		GLFW.glfwSetWindowContentScaleCallback(window, contentScaleCallback);
	}
}
