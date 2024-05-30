package net.krlite.equator.visual.animation.base.events;

import net.krlite.equator.visual.animation.base.Interpolation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;

// Forge's creepy event system
public class InterpolationEvents {
    public static abstract class Callbacks extends Event {
        private final Interpolation<?> interpolation;

        @ApiStatus.Internal
        protected Callbacks(Interpolation<?> interpolation) {
            this.interpolation = interpolation;
        }

        public Interpolation<?> getInterpolation() {
            return this.interpolation;
        }

        public static class Completion extends Callbacks {
            @ApiStatus.Internal
            public Completion(Interpolation<?> interpolation) {
                super(interpolation);
            }
        }

        public static class Pause extends Callbacks {
            @ApiStatus.Internal
            public Pause(Interpolation<?> interpolation) {
                super(interpolation);
            }
        }

        public static class Resume extends Callbacks {
            @ApiStatus.Internal
            public Resume(Interpolation<?> interpolation) {
                super(interpolation);
            }
        }

        @Cancelable
        public static class FrameStart extends Callbacks {
            @ApiStatus.Internal
            public FrameStart(Interpolation<?> interpolation) {
                super(interpolation);
            }
        }

        public static class FrameEnd extends Callbacks {
            @ApiStatus.Internal
            public FrameEnd(Interpolation<?> interpolation) {
                super(interpolation);
            }
        }
    }
}
