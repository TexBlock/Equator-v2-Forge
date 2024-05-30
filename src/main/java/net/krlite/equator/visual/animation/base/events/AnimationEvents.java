package net.krlite.equator.visual.animation.base.events;

import net.krlite.equator.visual.animation.base.Animation;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus;

// Forge's creepy event system
public class AnimationEvents {
    public static abstract class Callbacks extends Event {
        private final Animation<?> animation;

        @ApiStatus.Internal
        protected Callbacks(Animation<?> animation) {
            this.animation = animation;
        }

        public Animation<?> getAnimation() {
            return this.animation;
        }

        @Cancelable
        public static class Play extends Callbacks {
            @ApiStatus.Internal
            public Play(Animation<?> animation) {
                super(animation);
            }
        }

        public static class Termination extends Callbacks {
            @ApiStatus.Internal
            public Termination(Animation<?> animation) {
                super(animation);
            }
        }

        public static class Pause extends Callbacks {
            @ApiStatus.Internal
            public Pause(Animation<?> animation) {
                super(animation);
            }
        }

        public static class Resume extends Callbacks {
            @ApiStatus.Internal
            public Resume(Animation<?> animation) {
                super(animation);
            }
        }

        public static class Loop extends Callbacks {
            @ApiStatus.Internal
            public Loop(Animation<?> animation) {
                super(animation);
            }
        }

        @Cancelable
        public static class FrameStart extends Callbacks {
            @ApiStatus.Internal
            public FrameStart(Animation<?> animation) {
                super(animation);
            }
        }

        public static class FrameEnd extends Callbacks {
            @ApiStatus.Internal
            public FrameEnd(Animation<?> animation) {
                super(animation);
            }
        }
    }
}
