package net.krlite.equator.visual.animation.base;

import net.krlite.equator.visual.animation.base.events.InterpolationEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * <h1>Interpolation</h1>
 * Handles the interpolation between two values.
 * @param <I>	the type of the interpolated value.
 */
public abstract class Interpolation<I> implements Runnable {
	public static class Any<I> {
		@FunctionalInterface
		public interface Protocol<I> {
			I interpolate(I value, I target, double ratio);
		}

		private final Protocol<I> protocol;

		public Protocol<I> protocol() {
			return protocol;
		}

		public Any(Protocol<I> protocol) {
			this.protocol = protocol;
		}

		public Interpolation<I> use(I initial, double ratio) {
			return new Interpolation<>(initial, ratio) {
				@Override
				public I interpolate(I value, I target) {
					return protocol().interpolate(value, target, ratio());
				}

				@Override
				public boolean isCompleted() {
					return Objects.equals(value(), target());
				}
			};
		}
	}

	protected record States(
			double ratio,
			boolean available, boolean completed,
			@Nullable ScheduledFuture<?> future
	) {}

	// Constructors

	public Interpolation(I initial, double ratio) {
		this.value = this.last = this.target = initial;
		this.states = new States(ratio, false, false, null);
	}

	// Fields

	private I value, last, target;
	private States states;

	// Accessors

	public I value() {
		return value;
	}

	public I last() {
		return last;
	}

	public I target() {
		return target;
	}

	public double ratio() {
		return states.ratio();
	}

	protected @Nullable ScheduledFuture<?> future() {
		return states.future();
	}

	// Mutators

	protected void fetch() {
		last = value;
	}

	protected void value(I value) {
		this.value = value;
	}

	public void reset(I value) {
		value(value);
		fetch();
	}

	public void target(I target) {
		this.target = target;
		if (!isAvailable()) {
			available(true);
			play();
		}
	}

	protected void states(double ratio, boolean started, boolean completed, @Nullable ScheduledFuture<?> future) {
		states = new States(ratio, started, completed, future);
	}

	public void ratio(double ratio) {
		states(ratio, states.available(), states.completed(), states.future());
	}

	protected void available(boolean available) {
		states(states.ratio(), available, states.completed(), states.future());
	}

	protected void completed(boolean completed) {
		states(states.ratio(), states.available(), completed, states.future());
	}

	private void future(@Nullable ScheduledFuture<?> future) {
		states(states.ratio(), states.available(), states.completed(), future);
	}

	// Properties

	public boolean isPlaying() {
		return future() != null && !Objects.requireNonNull(future()).isCancelled();
	}

	public boolean isPaused() {
		return future() != null && Objects.requireNonNull(future()).isCancelled();
	}

	public boolean isAvailable() {
		return states.available();
	}

	public abstract boolean isCompleted();

	// Interface Implementations

	/**
	 * Runs this operation.
	 */
	@Override
	public void run() {
		if (!isAvailable()) return;

		MinecraftForge.EVENT_BUS.post(new InterpolationEvents.Callbacks.FrameStart(this));

		if (isCompleted() && !states.completed()) {
			completed(true);
			MinecraftForge.EVENT_BUS.post(new InterpolationEvents.Callbacks.Completion(this));
		} else completed(false);

		if (value() != null && target() != null) {
			fetch();
			value(interpolate(value(), target()));
		}

		MinecraftForge.EVENT_BUS.post(new InterpolationEvents.Callbacks.FrameEnd(this));
	}

	public abstract I interpolate(I value, I target);

	// Functions

	protected void play() {
		future(AnimationThreadPoolExecutor.join(this, 0));
	}

	public void pause() {
		if (isPlaying()) {
			MinecraftForge.EVENT_BUS.post(new InterpolationEvents.Callbacks.Pause(this));
			Objects.requireNonNull(future()).cancel(true);
		}
	}

	public void resume() {
		if (isPaused()) {
			MinecraftForge.EVENT_BUS.post(new InterpolationEvents.Callbacks.Resume(this));
			future(AnimationThreadPoolExecutor.join(this, 0));
		}
	}

	public void onCompletion(Runnable runnable) {
		MinecraftForge.EVENT_BUS.<InterpolationEvents.Callbacks.Completion>addListener(EventPriority.HIGHEST, event -> {
			if (event.getInterpolation() == this) runnable.run();
		});
	}

	public void onPause(Runnable runnable) {
		MinecraftForge.EVENT_BUS.<InterpolationEvents.Callbacks.Pause>addListener(EventPriority.HIGHEST, event -> {
			if (event.getInterpolation() == this) runnable.run();
		});
	}

	public void onResume(Runnable runnable) {
		MinecraftForge.EVENT_BUS.<InterpolationEvents.Callbacks.Resume>addListener(EventPriority.HIGHEST, event -> {
			if (event.getInterpolation() == this) runnable.run();
		});
	}

	public void onFrameStart(Runnable runnable) {
		MinecraftForge.EVENT_BUS.<InterpolationEvents.Callbacks.FrameStart>addListener(EventPriority.HIGHEST, event -> {
			if (event.getInterpolation() == this) runnable.run();
		});
	}

	public void onFrameEnd(Runnable runnable) {
		MinecraftForge.EVENT_BUS.<InterpolationEvents.Callbacks.FrameEnd>addListener(EventPriority.HIGHEST, event -> {
			if (event.getInterpolation() == this) runnable.run();
		});
	}
}
